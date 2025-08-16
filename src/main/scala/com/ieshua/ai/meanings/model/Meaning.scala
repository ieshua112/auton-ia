package com.ieshua.ai.meanings.model

import java.time.OffsetDateTime

import io.circe.Json
import io.circe.generic.JsonCodec

@JsonCodec(encodeOnly = true)
final case class Meaning(
  id:           Long,
  content:      String,
  sources:      Json,
  metadata:     Json,
  embedding:    List[Double],
  modelVersion: String,
  createdAt:    OffsetDateTime,
  updatedAt:    OffsetDateTime
)
