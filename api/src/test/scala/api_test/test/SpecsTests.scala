package api_test.test

import akka.Done
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import api.controllers.VehicleController.routes
import api.controllers.responses.{
  CountAvailableVehiclesPerTileResponse,
  GetAvailableVehiclesResponse,
  GetFilledTilesResponse,
  GetLastPositionOfVehicleResponse
}
import api.controllers.responses.data.{
  LatLongResponse,
  TileResponse,
  VehicleAmountOfTileResponse,
  VehicleResponse
}
import domain.data.vehicles.VehicleEntity
import io.circe.Decoder
import io.circe.parser.decode
import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import org.scalatest.concurrent.Eventually.eventually
import org.scalatest.concurrent.Eventually.PatienceConfig
import org.scalatest.concurrent.Futures.scaled
import org.scalatest.matchers.should.Matchers
import org.scalatest.time.{ Seconds, Span }
import org.scalatest.wordspec.{ AnyWordSpecLike, AsyncWordSpecLike }

import scala.concurrent.Future

trait SpecsTests extends AnyWordSpecLike with Matchers with ScalatestRouteTest {

  implicit val patienceConfig: PatienceConfig =
    PatienceConfig(timeout = scaled(Span(10, Seconds)), interval = scaled(Span(1, Seconds)))

  def parseJson[T: Decoder](json: String): T = {
    val eitherTypeOrError = decode[T](json)
    assert(eitherTypeOrError.isRight)
    eitherTypeOrError
      .getOrElse(throw new IllegalStateException("Should never arrived!"))
  }

  def testVehiclesList(included_vehicles: VehicleResponse*): Unit =
    Get(f"/api/vehicles/list") ~> routes ~> check {

      status.shouldEqual(StatusCodes.OK)

      val maybeResponse: GetAvailableVehiclesResponse =
        parseJson[GetAvailableVehiclesResponse](responseAs[String])

      maybeResponse.vehicles.toSet.shouldEqual(included_vehicles.toSet)
    }

  def testVehicleLastPosition(req: VehicleEntity, resp: LatLongResponse): Unit =
    Get(f"/api/vehicles/vehicle/${req.id}/lastPosition") ~> routes ~> check {

      status.shouldEqual(StatusCodes.OK)

      val maybeResponse: GetLastPositionOfVehicleResponse =
        parseJson[GetLastPositionOfVehicleResponse](responseAs[String])

      maybeResponse.vehicle_position.shouldEqual(resp)

    }

  def testFilledTiles(included_tiles: TileResponse*): Unit =
    Get(f"/api/tiles/filled") ~> routes ~> check {

      status.shouldEqual(StatusCodes.OK)

      val maybeResponse: GetFilledTilesResponse =
        parseJson[GetFilledTilesResponse](responseAs[String])

      maybeResponse.tiles.shouldEqual(included_tiles.toSet)

    }

  def testVehiclesInTile(req: TileResponse, included_vehicles: VehicleResponse*): Unit =
    Get(
      f"/api/tiles/tile/${req.id}/availableVehicles"
    ) ~> routes ~> check {

      status.shouldEqual(StatusCodes.OK)

      val maybeResponse: GetAvailableVehiclesResponse =
        parseJson[GetAvailableVehiclesResponse](responseAs[String])

      maybeResponse.vehicles.toSet.shouldEqual(included_vehicles.toSet)
    }

  def testVehicleCountPerTile(
      included_amounts: VehicleAmountOfTileResponse*
  ): Unit =
    Get(f"/api/tiles/usecase/vehicleCount") ~> routes ~> check {

      status.shouldEqual(StatusCodes.OK)

      val maybeResponse: CountAvailableVehiclesPerTileResponse =
        parseJson[CountAvailableVehiclesPerTileResponse](responseAs[String])

      maybeResponse.amounts.toSet.shouldEqual(included_amounts.toSet)
    }

}
