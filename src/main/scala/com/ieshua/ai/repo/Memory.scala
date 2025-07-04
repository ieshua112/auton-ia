package com.ieshua.ai.repo

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths, StandardOpenOption}

import zio._

object Memory {
  private val memoryFile = Paths.get("memory.log")

  def remember(input: String): Task[Unit] =
    ZIO.attempt {
      val line = s"${java.time.Instant.now()} | $input\n"
      Files.write(
        memoryFile,
        line.getBytes(StandardCharsets.UTF_8),
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND
      )
    }

  def recall: Task[String] =
    ZIO.attempt {
      if (Files.exists(memoryFile))
        new String(Files.readAllBytes(memoryFile), StandardCharsets.UTF_8)
      else ""
    }
}
