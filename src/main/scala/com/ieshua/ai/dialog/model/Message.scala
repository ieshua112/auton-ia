package com.ieshua.ai.dialog.model

import java.time.OffsetDateTime

import io.circe.Json
import io.circe.generic.JsonCodec

@JsonCodec(encodeOnly = true)
final case class Message(
  id:          Long,
  dialogId:    Long,
  role:        String,
  contentText: String,
  summary:     Option[String],
  lang:        Option[String],
  contentJson: Option[Json],
  metadata:    Option[Json],
  createdAt:   Option[OffsetDateTime]
)
