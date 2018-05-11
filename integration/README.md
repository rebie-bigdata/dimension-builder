## integration

#### kylin
```
import com.rebiekong.bdt.tools.dimension.builder.integration._
import com.rebiekong.bdt.tools.dimension.builder.request.RequestModel

val requestModel:RequestModel = _
val sql = requestModel.buildKylinSQL()
```