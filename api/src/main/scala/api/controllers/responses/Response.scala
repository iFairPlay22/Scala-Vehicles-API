package api.controllers.responses

import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._

// the responses it may send to the server
abstract class Response
