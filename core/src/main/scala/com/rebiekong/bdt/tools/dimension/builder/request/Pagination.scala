package com.rebiekong.bdt.tools.dimension.builder.request

import com.fasterxml.jackson.annotation.JsonProperty

class Pagination {
  @JsonProperty("order_by")
  var orderBy: java.util.List[OrderBy] = _
  @JsonProperty("page_size")
  var pageSize: Int = 20
  @JsonProperty("page")
  var page: Int = 1

}
