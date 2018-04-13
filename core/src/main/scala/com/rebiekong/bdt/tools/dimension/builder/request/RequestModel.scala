package com.rebiekong.bdt.tools.dimension.builder.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.rebiekong.bdt.tools.dimension.builder.request.field.Field
import com.rebiekong.bdt.tools.dimension.builder.request.where.Where

import scala.collection.convert.wrapAll._
import scala.collection.mutable.ListBuffer

class RequestModel {

  @JsonProperty("fields")
  var fields: java.util.List[Field] = _
  @JsonProperty("where")
  var where: Where = _
  @JsonProperty("group_by")
  var groupBy: java.util.List[String] = _
  @JsonProperty("order_by")
  var orderBy: java.util.List[OrderBy] = _
  @JsonProperty("limit")
  var limit: Int = _

  def setLimit(limit: Int): Unit = {
    this.limit = limit
  }

  def setWhere(where: Where): Unit = {
    this.where = where
  }

  def setGroupBy(groupBy: java.util.List[String]): Unit = {
    this.groupBy = groupBy
  }

  def setOrderBy(orderBy: java.util.List[OrderBy]): Unit = {
    this.orderBy = orderBy
  }

  def setFields(fields: java.util.List[Field]): Unit = {
    this.fields = fields
  }

  def getFields():java.util.List[Field]={
    this.fields
  }
}

object RequestModel {


  def builder = new RequestModelBuilder

  class RequestModelBuilder {

    private var where: Where = _
    private var limit: Int = 20
    private val fields: ListBuffer[Field] = ListBuffer.empty[Field]
    private val groupBy: ListBuffer[String] = ListBuffer.empty[String]
    private val orderBy: ListBuffer[OrderBy] = ListBuffer.empty[OrderBy]

    def setWhere(where: Where): RequestModelBuilder = {
      this.where = where
      this
    }

    def setLimit(limit: Int): RequestModelBuilder = {
      this.limit = limit
      this
    }

    def addField(field: Field): RequestModelBuilder = {
      this.fields.append(field)
      this
    }

    def addGroupBy(field: String): RequestModelBuilder = {
      this.groupBy.append(field)
      this
    }

    def addOrderBy(orderBy: String, isAsc: Boolean = true): RequestModelBuilder = {
      val o = new OrderBy
      o.setFieldName(orderBy)
      o.setAsc(isAsc)
      this.orderBy.append(o)
      this
    }

    def create: RequestModel = {
      val r = new RequestModel()
      r.setWhere(where)
      r.setFields(fields)
      r.setGroupBy(groupBy)
      r.setOrderBy(orderBy)
      r.setLimit(limit)
      r
    }
  }


}
