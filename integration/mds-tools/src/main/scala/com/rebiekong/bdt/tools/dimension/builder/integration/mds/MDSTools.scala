package com.rebiekong.bdt.tools.dimension.builder.integration.mds

import com.rebiekong.bdt.mds.commons.SearchModel
import com.rebiekong.bdt.tools.dimension.builder.core.dimension.{Dimension, DiscreteDimension, RangeDimension}
import com.rebiekong.bdt.tools.dimension.builder.request.models.RequestItemModel
import com.rebiekong.bdt.tools.dimension.builder.request.where._

import scala.collection.mutable
import scala.collection.convert.wrapAll._
import scala.language.implicitConversions

class MDSTools(requestItemModel: RequestItemModel) {
  def getMDSModel: SearchModel = {
    SearchModel.build()
      .setIdType(requestItemModel.field.name)
      .setPage(requestItemModel.pagination.page)
      .setPageSize(requestItemModel.pagination.pageSize)
      .setDimensions(getKv(requestItemModel.where, new mutable.HashMap[String, String]()))
      .create()
  }


  def getKv(where: Where, map: mutable.HashMap[String, String]): mutable.HashMap[String, String] = {
    where match {
      case and: AndWhere => getKv(and.left, getKv(and.right, map))
      case _: OrWhere => throw new Exception("no impl")
      case _: NotWhere => throw new Exception("no impl")
      case value: ValueWhere =>
        value.dimension match {
          case discrete: DiscreteDimension[_] =>
            map.put(discrete.name, discrete.value.toString)
          case _: RangeDimension[_] =>
            throw new Exception("no impl")
        }
    }
    map
  }

}

object MDSTools {
  implicit def mdsConvent(requestItemModel: RequestItemModel): MDSTools = {
    new MDSTools(requestItemModel)
  }
}