package com.ieshua.ai.memory

import com.ieshua.ai.memory.model.MemoryCreate
import com.ieshua.ai.memory.model.Memory

trait MemoriesRepo[F[_]] {
  def create(create: MemoryCreate): F[Memory]
  def get(id: Long): F[Option[Memory]]
  def list: F[List[Memory]]
}
