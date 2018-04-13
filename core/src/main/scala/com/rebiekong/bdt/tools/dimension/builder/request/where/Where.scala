package com.rebiekong.bdt.tools.dimension.builder.request.where

import com.fasterxml.jackson.databind.annotation.{JsonDeserialize, JsonSerialize}
import com.rebiekong.bdt.tools.dimension.builder.core.dimension.Dimension
import com.rebiekong.bdt.tools.dimension.builder.request.serde.WhereSerde

@JsonSerialize(using = classOf[WhereSerde.Serializer])
@JsonDeserialize(using = classOf[WhereSerde.Deserializer])
abstract class Where {

  def and(another: Where) = new AndWhere(this, another)

  def or(another: Where) = new OrWhere(this, another)

  def not() = new NotWhere(this)

}

object Where {
  def create(dimension: Dimension): ValueWhere = {
    if (!dimension.haveValue) {
      throw new IllegalArgumentException("dimension must have value")
    }
    new ValueWhere(dimension)
  }
}