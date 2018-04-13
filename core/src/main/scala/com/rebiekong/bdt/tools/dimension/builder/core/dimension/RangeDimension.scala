package com.rebiekong.bdt.tools.dimension.builder.core.dimension

class RangeDimension[T](name: String, val lowerBound: T, val upperBound: T) extends Dimension(name) {
  override def haveValue: Boolean = upperBound != null || lowerBound != null
}
