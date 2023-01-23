package api.controllers

import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import akka.http.scaladsl.server.{ Directives, Route }
import io.circe.Encoder
import api.Main.{ apiConfig, apiExecutionContext, apiLogger, apiSystem }
import database.throwables.DatabaseException

import scala.concurrent.Future

trait Controller extends Directives {
  def routes: Route

  def toJson[R: Encoder](k: Future[Either[DatabaseException, R]]): Future[String] =
    k.map {
      case Right(response) => response.asJson.spaces4SortKeys
      case Left(error) =>
        error.printStackTrace()
        apiLogger.error("An unexpected error occurred!", error)
        error.getMessage.asJson.spaces4SortKeys
    }
}
