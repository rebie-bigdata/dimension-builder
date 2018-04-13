package com.rebiekong.bdt.tools.dimension.builder.request.field

import com.fasterxml.jackson.annotation.JsonProperty
import com.rebiekong.bdt.tools.dimension.builder.request.where.Where

class Field() {
  @JsonProperty("where")
  var where: Where = _
  @JsonProperty("field")
  var field: String = _
  @JsonProperty("name")
  var name: String = _
  @JsonProperty("table")
  var table: String = _

  def setName(name: String): Unit = {
    this.name = name
  }

  def setField(field: String): Unit = {
    this.field = field
  }

  def setWhere(where: Where): Unit = {
    this.where = where
  }

  def getName(name: String): String = {
    this.name
  }

  def getField(field: String): String = {
    this.field
  }

  def getWhere(where: Where): Where = {
    this.where
  }

  def setTable(table: String): Unit = {
    this.table = table
  }

  def getTable(): String = {
    this.table
  }

}
