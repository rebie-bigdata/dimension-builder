package com.rebiekong.bdt.tools.dimension.builder.integration

import com.fasterxml.jackson.databind.ObjectMapper
import com.rebiekong.bdt.tools.dimension.builder.request.models.RequestMeasureModel
import com.rebiekong.bdt.tools.dimension.builder.utils.SQLUtils

import scala.collection.convert.wrapAll._
import scala.language.implicitConversions

class Kylin(requestModel: RequestMeasureModel) {
  def buildKylinSQL(): String = {

    val basicWhere = requestModel.where
    val groupBy = String.join(",", requestModel.groupBy)
    val children = requestModel.getFields().groupBy(field => {
      (field.table, new String(new ObjectMapper().writeValueAsBytes(field.where)))
    }).toList.map(i => {
      val cField = String.join(",\n ", i._2.map(field => {
        s"${field.field} AS ${field.name}"
      }))
      val mWhere = basicWhere and i._2.head.where
      //
      s"""
         |SELECT
         | $groupBy,
         | $cField
         |FROM ${i._2.head.table}
         |WHERE ${SQLUtils.toSQL(mWhere)}
         |GROUP BY $groupBy
       """.stripMargin
    })

    val result = new StringBuilder()
    val fields = String.join(",", requestModel.fields.map(_.name))
    val tables = requestModel.fields.map(_.table).distinct

    val s = String.join("UNION", tables.map(tableName => {
      s"""
         |(SELECT
         | $groupBy
         |FROM $tableName
         |WHERE ${SQLUtils.toSQL(basicWhere)}
         |GROUP BY $groupBy)
       """.stripMargin
    }))

    val sG = String.join(",", requestModel.groupBy.map(groupKey => s"main_table.$groupKey AS $groupKey"))
    result.append(
      s"SELECT $sG,$fields FROM (SELECT distinct $groupBy FROM($s) AS tmp_main_table) AS main_table"
    )

    for (i <- children.indices) {
      result
        .append(" LEFT JOIN ")
        .append(" (").append(children.get(i)).append(s") AS t$i").append(" ON(")
        .append(String.join(" AND ", requestModel.groupBy.map(groupKey => s"main_table.$groupKey=t$i.$groupKey")))
        .append(") ")

    }

    if (requestModel.pagination != null) {
      if (requestModel.pagination.orderBy.size() > 0) {
        result.append(" ORDER BY ")
        result.append(String.join(",", requestModel.pagination.orderBy.map(orderBy => {
          s"${orderBy.fieldName} ${if (orderBy.isAsc()) "ASC" else "DESC"}"
        })))
      }
    }
    result.toString()
  }

}

object Kylin {
  implicit def kylinConvent(requestModel: RequestMeasureModel): Kylin = {
    new Kylin(requestModel)
  }
}