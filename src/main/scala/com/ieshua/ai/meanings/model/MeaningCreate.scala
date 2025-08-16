package com.ieshua.ai.meanings.model

import io.circe.Json
import io.circe.generic.JsonCodec
import io.circe.generic.extras.Configuration

// TODO  ввести типы
@JsonCodec(decodeOnly = true)
final case class MeaningCreate(
  content:      String,
  // TODO модель?
  sources:      Json = Json.arr(),
  // TODO модель?
  metadata:     Json = Json.obj(),
  embedding:    List[Double] = Nil,
  // TODO при добавлении по АПИ весии нет. В отличии от Memory добавление по АПИ это валидный бизнес кейс (вроде бы)
  modelVersion: String
)

object MeaningCreate {
  implicit val configuration: Configuration = Configuration.default.withDefaults
}
