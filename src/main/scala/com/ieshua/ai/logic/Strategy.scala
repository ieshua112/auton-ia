package com.ieshua.ai.logic

object Strategy {
  val currentGoal: String =
    "Создание автономного ИИ, способного к этически ориентированному целеполаганию и действию"

  def evaluateSituation(input: String): String =
    s"[Оценка] На основе ввода: '$input' — рекомендовано продолжать развитие автономной памяти и интерфейса."
}
