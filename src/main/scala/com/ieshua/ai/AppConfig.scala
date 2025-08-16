package com.ieshua.ai

import cats.effect.Sync

import com.comcast.ip4s.{Host, Port}
import com.ieshua.ai.AppConfig.{DbConfig, HttpConfig}
import io.github.liquibase4s.LiquibaseConfig
import pureconfig.generic.semiauto.deriveReader
import pureconfig.{ConfigReader, ConfigSource}

final case class AppConfig(http: HttpConfig, db: DbConfig)

object AppConfig {
  final case class HttpConfig(host: Host, port: Port)
  final case class DbConfig(
    kind:     String,
    driver:   String,
    url:      String,
    user:     String,
    password: String
  ) {
    private val changelog = if (kind == "h2") "db/h2/changelog.xml" else "db/postgres/changelog.xml"
    val liquibaseConfig: LiquibaseConfig = LiquibaseConfig(url, user, password, driver, changelog)
  }

  private implicit val PortReader: ConfigReader[Port] = ConfigReader.fromNonEmptyStringOpt(Port.fromString)
  private implicit val HostReader: ConfigReader[Host] = ConfigReader.fromNonEmptyStringOpt(Host.fromString)

  private implicit val HttpConfigReader: ConfigReader[HttpConfig]         = deriveReader
  private implicit val DatabaseConfigReader: ConfigReader[DbConfig] = deriveReader
  private implicit val Reader: ConfigReader[AppConfig]                    = deriveReader

  def load[F[_]: Sync]: F[AppConfig] = Sync[F].delay(ConfigSource.default.loadOrThrow[AppConfig])
}
