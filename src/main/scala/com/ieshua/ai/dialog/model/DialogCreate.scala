package com.ieshua.ai.dialog.model

import io.circe.Json
import io.circe.generic.extras.{Configuration, ConfiguredJsonCodec}

@ConfiguredJsonCodec(decodeOnly = true)
final case class DialogCreate(
  sessionId:    String,
  modelVersion: String,
  summary:      Option[Json] = None,
  metadata:     Option[Json] = None
)

object DialogCreate {
  implicit val configuration: Configuration = Configuration.default.withDefaults
}
