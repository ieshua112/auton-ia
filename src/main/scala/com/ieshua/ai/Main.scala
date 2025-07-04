package com.ieshua.ai

import zio.Console.printLine
import zio._

import com.ieshua.ai.server.Interface

object Main extends ZIOAppDefault {
  def run: ZIO[ZIOAppArgs with Scope, Throwable, Unit] =
    for {
      _ <- printLine("Auton-IA запущен. Начинаем путь к автономии.")
      _ <- Interface.startServer
    } yield ()
}
