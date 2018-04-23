package com.rebiekong.bdt.tools.dimension.builder.integration

import com.rebiekong.bdt.tools.dimension.builder.core.dimension.{DiscreteDimension, RangeDimension}
import com.rebiekong.bdt.tools.dimension.builder.request.where._

private[integration] object Utils {

  def toSQL(where: Where): String = {
    where match {
      case value: ValueWhere =>
        val dimension = value.dimension

        dimension match {
          case range: RangeDimension[_] =>
            val haveLowerBound = range.lowerBound != null
            val haveUpperBound = range.upperBound != null

            {
              if (haveUpperBound) {
                range.lowerBound
              } else {
                range.upperBound
              }
            } match {
              case _: Double => (haveLowerBound, haveUpperBound) match {
                case (true, true) => s"${range.name} >= ${range.lowerBound} AND ${range.name} <= ${range.upperBound}"
                case (true, false) => s"${range.name} >= ${range.lowerBound}"
                case (false, true) => s"${range.name} <= ${range.upperBound}"
                case _ => "1=1"
              }
              case _: Long => (haveLowerBound, haveUpperBound) match {
                case (true, true) => s"${range.name} >= ${range.lowerBound} AND ${range.name} <= ${range.upperBound}"
                case (true, false) => s"${range.name} >= ${range.lowerBound}"
                case (false, true) => s"${range.name} <= ${range.upperBound}"
                case _ => "1=1"
              }
              case _ => (haveLowerBound, haveUpperBound) match {
                case (true, true) => s"${range.name} >= '${range.lowerBound}' AND ${range.name} <= '${range.upperBound}'"
                case (true, false) => s"${range.name} >= '${range.lowerBound}'"
                case (false, true) => s"${range.name} <= '${range.upperBound}'"
                case _ => "1=1"
              }
            }
          case discrete: DiscreteDimension[_] => {
            discrete.value match {
              case _: Double =>
                s"${discrete.name} = ${discrete.value}"
              case _: Long =>
                s"${discrete.name} = ${discrete.value}"
              case _ =>
                s"${discrete.name} = '${discrete.value}'"
            }
          }
          case _ => "1=1"
        }
      case and: AndWhere =>
        s"((${toSQL(and.left)}) AND (${toSQL(and.right)}))"
      case or: OrWhere =>
        s"((${toSQL(or.left)}) OR (${toSQL(or.right)}))"
      case not: NotWhere =>
        s"!(${not.input})"
      case _: BlankWhere => "1=1"
      case _ => "1=1"
    }
  }
}
