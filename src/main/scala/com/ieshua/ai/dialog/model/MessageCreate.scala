package com.ieshua.ai.dialog.model

import io.circe.Json
import io.circe.generic.extras.{Configuration, ConfiguredJsonCodec}

@ConfiguredJsonCodec(decodeOnly = true)
final case class MessageCreate(
  role:        String,
  contentText: String,
  summary  :   Option[String] = None,
  lang:        Option[String] = None,
  contentJson: Option[Json] = None,
  metadata:    Option[Json] = None
)

object MessageCreate {
  implicit val configuration: Configuration = Configuration.default.withDefaults
}
