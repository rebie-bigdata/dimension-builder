package com.rebiekong.bdt.tools.dimension.builder.core.dimension

import com.rebiekong.bdt.tools.dimension.builder.core.builder.{DimensionBuilder, DiscreteDimensionBuilder, RangeDimensionBuilder}

class Dimension(val name: String) {
  def haveValue: Boolean = false
}

object Dimension {

  object builder {

    object value {
      def range[T]: RangeDimensionBuilder[T] = new RangeDimensionBuilder[T]

      def discrete[T]: DiscreteDimensionBuilder[T] = new DiscreteDimensionBuilder[T]
    }

    def base: DimensionBuilder = new DimensionBuilder ()
  }

}

