package com.rebiekong.bdt.tools.dimension.builder.core.builder

import com.rebiekong.bdt.tools.dimension.builder.core.dimension.Dimension

class DimensionBuilder {

  private var name: String = _

  def name(name: String): this.type = {
    this.name = name
    this
  }

  protected def getName: String = {
    this.name
  }

  def create = new Dimension(name)
}