package com.rebiekong.bdt.tools.dimension.builder.request

import com.fasterxml.jackson.annotation.JsonProperty

class OrderBy {

  @JsonProperty("field")
  var fieldName: String = _
  @JsonProperty("is_asc")
  var asc: Boolean = true

  def setFieldName(fieldName: String): Unit = {
    this.fieldName = fieldName
  }

  def setAsc(isAsc: Boolean): Unit = {
    this.asc = isAsc
  }

  def getFieldName(): String = {
    this.fieldName
  }

  def isAsc(): Boolean = {
    this.asc
  }

}
