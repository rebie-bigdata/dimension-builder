package com.rebiekong.bdt.tools.dimension.builder.request.models

import com.fasterxml.jackson.annotation.JsonProperty
import com.rebiekong.bdt.tools.dimension.builder.request.{OrderBy, Pagination}
import com.rebiekong.bdt.tools.dimension.builder.request.field.Field
import com.rebiekong.bdt.tools.dimension.builder.request.where.Where

import scala.collection.convert.wrapAll._
import scala.collection.mutable.ListBuffer

class RequestMeasureModel {

  @JsonProperty("fields")
  var fields: java.util.List[Field] = _
  @JsonProperty("where")
  var where: Where = _
  @JsonProperty("group_by")
  var groupBy: java.util.List[String] = _
  @JsonProperty("pagination")
  var pagination: Pagination = _

  def setWhere(where: Where): Unit = {
    this.where = where
  }

  def setGroupBy(groupBy: java.util.List[String]): Unit = {
    this.groupBy = groupBy
  }

  def setFields(fields: java.util.List[Field]): Unit = {
    this.fields = fields
  }

  def getFields(): java.util.List[Field] = {
    this.fields
  }
}

object RequestMeasureModel {


  def builder = new RequestMeasureModelBuilder

  class RequestMeasureModelBuilder {

    private var where: Where = _
    private var limit: Int = 20
    private val fields: ListBuffer[Field] = ListBuffer.empty[Field]
    private val groupBy: ListBuffer[String] = ListBuffer.empty[String]
    private val orderBy: ListBuffer[OrderBy] = ListBuffer.empty[OrderBy]

    def setWhere(where: Where): RequestMeasureModelBuilder = {
      this.where = where
      this
    }

    def setLimit(limit: Int): RequestMeasureModelBuilder = {
      this.limit = limit
      this
    }

    def addField(field: Field): RequestMeasureModelBuilder = {
      this.fields.append(field)
      this
    }

    def addGroupBy(field: String): RequestMeasureModelBuilder = {
      this.groupBy.append(field)
      this
    }

    def addOrderBy(orderBy: String, isAsc: Boolean = true): RequestMeasureModelBuilder = {
      val o = new OrderBy
      o.setFieldName(orderBy)
      o.setAsc(isAsc)
      this.orderBy.append(o)
      this
    }

    def create: RequestMeasureModel = {
      val pagination = new Pagination
      pagination.orderBy = orderBy
      pagination.pageSize = limit
      val r = new RequestMeasureModel()
      r.setWhere(where)
      r.setFields(fields)
      r.setGroupBy(groupBy)
      r.pagination = pagination
      r
    }
  }


}
