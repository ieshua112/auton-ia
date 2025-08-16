package com.ieshua.ai.memory.model

import java.time.OffsetDateTime

import io.circe.Json
import io.circe.generic.JsonCodec

@JsonCodec(encodeOnly = true)
final case class Memory(
  id:              Long,
  kind:            String,
  content:         Json,
  relatedMeanings: List[Long],
  weight:          Double,
  lastAccessed:    OffsetDateTime,
  timesSeen:       Int,
  modelVersion:    String,
  createdAt:       OffsetDateTime,
  updatedAt:       OffsetDateTime
)
