package com.rebiekong.bdt.tools.dimension.builder.request.models

import com.fasterxml.jackson.annotation.JsonProperty
import com.rebiekong.bdt.tools.dimension.builder.request.{OrderBy, Pagination}
import com.rebiekong.bdt.tools.dimension.builder.request.field.Field
import com.rebiekong.bdt.tools.dimension.builder.request.where.Where

import scala.collection.convert.wrapAll._
import scala.collection.mutable.ListBuffer

class RequestItemModel {

  @JsonProperty("field")
  var field: Field = _
  @JsonProperty("where")
  var where: Where = _
  @JsonProperty("pagination")
  var pagination: Pagination = _
}

object RequestItemModel {


  def builder = new RequestItemModelBuilder

  class RequestItemModelBuilder {

    private var where: Where = _
    private var pageSize: Int = 20
    private var page: Int = 1
    private val orderBy: ListBuffer[OrderBy] = ListBuffer.empty[OrderBy]
    private var field: Field = _


    def setWhere(where: Where): RequestItemModelBuilder = {
      this.where = where
      this
    }

    def setField(field: Field): RequestItemModelBuilder = {
      this.field = field
      this
    }

    def setPageSize(pageSize: Int): RequestItemModelBuilder = {
      this.pageSize = pageSize
      this
    }

    def setPage(page: Int): RequestItemModelBuilder = {
      this.page = page
      this
    }

    def addOrderBy(orderBy: String, isAsc: Boolean = true): RequestItemModelBuilder = {
      val o = new OrderBy
      o.setFieldName(orderBy)
      o.setAsc(isAsc)
      this.orderBy.append(o)
      this
    }

    def create: RequestItemModel = {
      val pagination = new Pagination
      pagination.orderBy = orderBy
      pagination.page = page
      pagination.pageSize = pageSize
      val r = new RequestItemModel()
      r.where = where
      r.field = field
      r.pagination = pagination
      r
    }
  }


}
