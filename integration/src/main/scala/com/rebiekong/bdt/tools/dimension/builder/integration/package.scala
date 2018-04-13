package com.rebiekong.bdt.tools.dimension.builder

import com.rebiekong.bdt.tools.dimension.builder.request.RequestModel

import scala.language.implicitConversions

package object integration {

  implicit def kylinConvent(requestModel: RequestModel): KylinSQL = {
    new KylinSQL(requestModel)
  }
}
