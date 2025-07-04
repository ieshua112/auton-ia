package com.ieshua.ai.logic

object Agent {
  def act(action: String): String =
    s"[Действие] Выполняется симуляция действия: '$action' — (пока нет подключения к реальному API)"
}
