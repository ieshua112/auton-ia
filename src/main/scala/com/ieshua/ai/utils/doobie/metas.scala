package com.ieshua.ai.utils.doobie

import java.sql.Timestamp
import java.time.{OffsetDateTime, ZoneOffset}

import doobie.util.meta.Meta
import io.circe.syntax._
import io.circe.{Json, parser}

object metas {
  // Храним JSON как TEXT/CLOB (и как jsonb в PG, если нужно — можно подключить pg-имплициты отдельно)
  implicit val metaJson: Meta[Json] =
    Meta[String].timap(s => parser.parse(s).getOrElse(Json.Null))(_.noSpaces)

  // Вектор как JSON-массив чисел в TEXT/CLOB
  implicit val metaListDouble: Meta[List[Double]] =
    Meta[String]
      .timap(s => parser.parse(s).toOption.flatMap(_.as[List[Double]].toOption).getOrElse(Nil))(_.asJson.noSpaces)

  implicit val metaListLong: Meta[List[Long]] =
    Meta[String]
      .imap(s => parser.parse(s).toOption.flatMap(_.as[List[Long]].toOption).getOrElse(Nil))(_.asJson.noSpaces)

  implicit val offsetDateTimeMeta: Meta[OffsetDateTime] =
    Meta[Timestamp]
      .imap(_.toInstant.atOffset(ZoneOffset.UTC))(odt => Timestamp.from(odt.toInstant))
}
