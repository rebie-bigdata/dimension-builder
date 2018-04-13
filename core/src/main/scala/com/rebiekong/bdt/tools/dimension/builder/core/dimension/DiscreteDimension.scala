package com.rebiekong.bdt.tools.dimension.builder.core.dimension


class DiscreteDimension[T](name: String, val value: T, val matchType: Boolean) extends Dimension(name) {
  override def haveValue: Boolean = value != null
}