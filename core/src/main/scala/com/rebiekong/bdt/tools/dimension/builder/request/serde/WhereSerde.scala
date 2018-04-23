package com.rebiekong.bdt.tools.dimension.builder.request.serde

import com.fasterxml.jackson.core.{JsonGenerator, JsonParser}
import com.fasterxml.jackson.databind._
import com.fasterxml.jackson.databind.node.JsonNodeType
import com.rebiekong.bdt.tools.dimension.builder.core.dimension.{Dimension, DiscreteDimension, RangeDimension}
import com.rebiekong.bdt.tools.dimension.builder.request.where._

object WhereSerde {

  class Serializer extends JsonSerializer[Where] {
    override def serialize(where: Where, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider) = {
      jsonGenerator.writeStartObject()
      where match {
        case and: AndWhere =>
          jsonGenerator.writeStringField("type", "and")
          jsonGenerator.writeObjectField("left", and.left)
          jsonGenerator.writeObjectField("right", and.right)
        case or: OrWhere =>
          jsonGenerator.writeStringField("type", "or")
          jsonGenerator.writeObjectField("left", or.left)
          jsonGenerator.writeObjectField("right", or.right)
        case not: NotWhere =>
          jsonGenerator.writeStringField("type", "not")
          jsonGenerator.writeObjectField("input", not.input)
        case _: BlankWhere =>
          jsonGenerator.writeStringField("type", "blank")
        case value: ValueWhere =>
          jsonGenerator.writeStringField("type", "value")
          jsonGenerator.writeStringField("field", value.dimension.name)
          value.dimension match {
            case discreteDimension: DiscreteDimension[_] =>
              jsonGenerator.writeStringField("sub_type", "discrete")
              jsonGenerator.writeBooleanField("match_type", discreteDimension.matchType)
              jsonGenerator.writeStringField("match_type", discreteDimension.value.toString)
            case rangeDimension: RangeDimension[_] =>
              jsonGenerator.writeStringField("sub_type", "range")
              jsonGenerator.writeStringField("upper_bound", rangeDimension.upperBound.toString)
              jsonGenerator.writeStringField("lower_bound", rangeDimension.lowerBound.toString)
          }
      }
      jsonGenerator.writeEndObject()
    }
  }

  class Deserializer extends JsonDeserializer[Where] {
    override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): Where = {
      parseNode(jsonParser.getCodec.readTree(jsonParser))
    }

    def parseNode(node: JsonNode): Where = {
      val whereType = node.get("type").asText()
      whereType.toLowerCase match {
        case "and" =>
          val left = parseNode(node.get("left"))
          val right = parseNode(node.get("right"))
          left and right
        case "or" =>
          val left = parseNode(node.get("left"))
          val right = parseNode(node.get("right"))
          left or right
        case "not" =>
          val input = parseNode(node.get("input"))
          input.not()
        case "blank" =>
          new BlankWhere
        case "value" =>
          // sub type判断
          val subType = node.get("sub_type").asText()
          val dimension: Dimension = subType.toLowerCase() match {
            case "range" =>
              if (node.get("lower_bound").getNodeType.equals(JsonNodeType.NUMBER)) {
                if (node.get("lower_bound").asDouble(Double.NaN).isValidInt) {
                  val builder = Dimension.builder.value.range[Long]
                    .name(node.get("field").asText())
                  if (node.has("lower_bound")) {
                    builder.setLowerBound(node.get("lower_bound").asLong())
                  }
                  if (node.has("upper_bound")) {
                    builder.setUpperBound(node.get("upper_bound").asLong())
                  }
                  builder.create
                } else {
                  val builder = Dimension.builder.value.range[Double]
                    .name(node.get("field").asText())
                  if (node.has("lower_bound")) {
                    builder.setLowerBound(node.get("lower_bound").asDouble())
                  }
                  if (node.has("upper_bound")) {
                    builder.setUpperBound(node.get("upper_bound").asDouble())
                  }
                  builder.create
                }
              } else {
                val builder = Dimension.builder.value.range[String]
                  .name(node.get("field").asText())
                if (node.has("lower_bound")) {
                  builder.setLowerBound(node.get("lower_bound").asText())
                }
                if (node.has("upper_bound")) {
                  builder.setUpperBound(node.get("upper_bound").asText())
                }
                builder.create
              }
            case "discrete" =>
              if (node.get("value").getNodeType.equals(JsonNodeType.NUMBER)) {
                if (node.get("value").asDouble(Double.NaN).isValidInt) {
                  Dimension.builder.value.discrete[Long]
                    .name(node.get("field").asText())
                    .setValue(node.get("value").asLong())
                    .create
                } else {
                  Dimension.builder.value.discrete[Double]
                    .name(node.get("field").asText())
                    .setValue(node.get("value").asDouble())
                    .create
                }

              } else {
                Dimension.builder.value.discrete[String]
                  .name(node.get("field").asText())
                  .setValue(node.get("value").asText(null))
                  .create
              }
          }
          Where.create(dimension)
      }
    }
  }

}
