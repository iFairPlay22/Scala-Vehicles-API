package api_test

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import api.vehicles.controller.VehicleController
import api.vehicles.dto.GetVehiclesDTO
import api.vehicles.mapper.VehicleResponseMapper
import commons.system.database._CassandraTestSystem
import org.scalatest.matchers.should.Matchers
import database.repositories.VehicleRepository
import domain.data.vehicles.VehicleDomain
import io.circe.generic.codec.DerivedAsObjectCodec.deriveCodec
import org.scalatest.concurrent.ScalaFutures

class ApiSpecs extends _CassandraTestSystem with Matchers with ScalaFutures with SpecsData {

  private val vehiclesRepository: VehicleRepository = new VehicleRepository()
  private val vehiclesRoutes: Route = new VehicleController().routes

  def testVehiclesList(expectedVehicles: VehicleDomain*): Unit =
    Get(f"/vehicles") ~> vehiclesRoutes ~> check {

      status.shouldEqual(StatusCodes.OK)

      val maybeResponse: GetVehiclesDTO = decodeResponse[GetVehiclesDTO](responseAs[String])
      maybeResponse.vehicles.shouldEqual(expectedVehicles.map(VehicleResponseMapper.map))
    }

  f"Vehicles API" should {

    f"return empty vehicles list" in {

      testVehiclesList()

    }

    f"return vehicles list with 1 element" in {

      await(vehiclesRepository.insertOrEdit(vehicle = vehicle1))
      testVehiclesList(vehicle1)

    }

    f"return vehicles list with 2 elements" in {

      await(vehiclesRepository.insertOrEdit(vehicle = vehicle1))
      await(vehiclesRepository.insertOrEdit(vehicle = vehicle2))
      testVehiclesList(vehicle1, vehicle2)

    }

    f"return vehicles list with 3 elements" in {

      await(vehiclesRepository.insertOrEdit(vehicle = vehicle1))
      await(vehiclesRepository.insertOrEdit(vehicle = vehicle3))
      await(vehiclesRepository.insertOrEdit(vehicle = vehicle2))
      testVehiclesList(vehicle1, vehicle2, vehicle3)

    }

    f"return vehicles list with 2 elements (no duplicates)" in {

      await(vehiclesRepository.insertOrEdit(vehicle = vehicle1))
      await(vehiclesRepository.insertOrEdit(vehicle = vehicle2))
      await(vehiclesRepository.insertOrEdit(vehicle = vehicle2))
      testVehiclesList(vehicle1, vehicle2)

    }
  }

}
