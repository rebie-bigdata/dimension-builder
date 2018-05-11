package com.rebiekong.bdt.tools.dimension.builder.request

import com.fasterxml.jackson.annotation.JsonProperty

class Pagination {
  @JsonProperty("order_by")
  var orderBy: java.util.List[OrderBy] = _
  @JsonProperty("limit")
  var limit: Int = 20
  @JsonProperty("offset")
  var offset: Int = 0

}
