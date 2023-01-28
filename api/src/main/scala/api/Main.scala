package api

import akka.Done
import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.{ Http, HttpExt }
import akka.http.scaladsl.server.Route
import api.controllers.VehicleController
import com.typesafe.config.Config

import scala.concurrent.duration.Duration
import scala.concurrent.{ Await, ExecutionContextExecutor, Future }
import scala.io.StdIn

object Main {

  // System constants
  implicit val apiSystem: ActorSystem                              = ActorSystem("api-system")
  final implicit val apiExecutionContext: ExecutionContextExecutor = apiSystem.dispatcher
  final val apiLogger         = Logging(apiSystem, "api-logger")
  final val apiConfig: Config = apiSystem.settings.config

  // Server
  private val SERVER_HOST: String =
    apiConfig.getString("api.server-host")
  private val SERVER_PORT: Int =
    apiConfig.getInt("api.server-port")

  apiLogger.info(f"Creating an HTTP server in $SERVER_HOST:$SERVER_PORT")

  final implicit val apiHttp: HttpExt = Http()

  final implicit val apiServer: Future[Http.ServerBinding] = apiHttp
    .newServerAt(SERVER_HOST, SERVER_PORT)
    .bind(Route.seal(VehicleController.routes))

  // Database
  database.Main.init()

  def init(): Future[Done] = Future(Done)

  def terminate(): Future[Done] =
    Future(Done)
      .andThen(_ => apiLogger.info("Calling api.Main.terminate()"))
      .flatMap(_ =>
        apiServer
          .flatMap(_.unbind())
      )

  def main(args: Array[String]): Unit = {
    /*
    StdIn.readLine()
    Await.ready(
      terminate(),
      Duration.Inf
    )
     */
  }
}
