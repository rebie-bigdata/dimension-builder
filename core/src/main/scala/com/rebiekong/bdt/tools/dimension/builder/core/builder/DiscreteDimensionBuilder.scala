package com.rebiekong.bdt.tools.dimension.builder.core.builder

import com.rebiekong.bdt.tools.dimension.builder.core.dimension.DiscreteDimension

class DiscreteDimensionBuilder[T] extends DimensionBuilder {
  private var value: T = _
  private var matchType: Boolean = true

  def setValue(value: T): DiscreteDimensionBuilder[T] = {
    this.value = value
    this
  }

  def setMatchType(matchType: Boolean): DiscreteDimensionBuilder[T] = {
    this.matchType = matchType
    this
  }

  override def create: DiscreteDimension[T] = new DiscreteDimension[T](getName, value, matchType)
}