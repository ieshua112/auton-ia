package com.ieshua.ai.memory.model

import io.circe.Json
import io.circe.generic.extras.{Configuration, ConfiguredJsonCodec}

@ConfiguredJsonCodec(decodeOnly = true)
final case class MemoryCreate(
  kind:            String,
  // TODO добавить модель?
  content:         Json,
  relatedMeanings: List[Long] = Nil,
  weight:          Double = 1.0,
  timesSeen:       Int = 0,
  // TODO при добавлении по АПИ версии не будет. Нужно ли нам вообще добавление по АПИ. Разве что только для теста?
  modelVersion:    String
)

object MemoryCreate {
  implicit val configuration: Configuration = Configuration.default.withDefaults
}
