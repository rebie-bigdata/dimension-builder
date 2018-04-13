package com.rebiekong.bdt.tools.dimension.builder.core.builder

import com.rebiekong.bdt.tools.dimension.builder.core.dimension.RangeDimension

class RangeDimensionBuilder[T] extends DimensionBuilder {
  private var upperBound: T = _
  private var lowerBound: T = _

  def setUpperBound(upperBound: T): RangeDimensionBuilder[T] = {
    this.upperBound = upperBound
    this
  }

  def setLowerBound(lowerBound: T): RangeDimensionBuilder[T] = {
    this.lowerBound = lowerBound
    this
  }

  override def create: RangeDimension[T] = new RangeDimension[T](getName, lowerBound, upperBound)
}