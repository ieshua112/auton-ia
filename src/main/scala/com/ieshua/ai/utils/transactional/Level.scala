package com.ieshua.ai.utils.transactional

sealed trait Level

object Level {
  case object ReadCommitted  extends Level
  case object RepeatableRead extends Level
  case object Serializable   extends Level
}
