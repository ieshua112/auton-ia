package com.ieshua.ai.dialog.model

import java.time.OffsetDateTime
import io.circe.Json
import io.circe.generic.JsonCodec

@JsonCodec(encodeOnly = true)
final case class Dialog(
  id:           Long,
  sessionId:    String,
  startedAt:    Option[OffsetDateTime],
  endedAt:      Option[OffsetDateTime],
  modelVersion: String,
  summary:      Json,
  metadata:     Json
)
