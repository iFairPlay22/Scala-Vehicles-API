package api_test.data

import api.controllers.responses.data.{ LatLongResponse, TileResponse, VehicleResponse }
import domain.vehicles.VehicleEntity

trait SpecsData {

  // Random vehicles
  val randomData1: (VehicleEntity, VehicleResponse, LatLongResponse, TileResponse) =
    Random.randomVehicleDataWithSpecs(id = 1, latitude = 69.6516345, longitude = -18.9558585)
  val randomData2: (VehicleEntity, VehicleResponse, LatLongResponse, TileResponse) =
    Random.randomVehicleDataWithSpecs(id = 2, latitude = 40.7127281, longitude = -74.0060152)
  val randomData3: (VehicleEntity, VehicleResponse, LatLongResponse, TileResponse) =
    Random.randomVehicleDataWithSpecs(id = 3, latitude = 39.906217, longitude = 116.3912757)
  val randomData4: (VehicleEntity, VehicleResponse, LatLongResponse, TileResponse) =
    Random.randomVehicleDataWithSpecs(
      id        = 4,
      latitude  = randomData3._3.latitude,
      longitude = randomData3._3.longitude
    ) // same position as randomData3
  val randomData5: (VehicleEntity, VehicleResponse, LatLongResponse, TileResponse) =
    Random.randomVehicleDataWithSpecs(
      id        = randomData2._2.id,
      latitude  = 22.9110137,
      longitude = 43.2093727
    ) // same id as randomData2

  // Vehicles entity
  val testVehicleEntity1: VehicleEntity = randomData1._1
  val testVehicleEntity2: VehicleEntity = randomData2._1
  val testVehicleEntity3: VehicleEntity = randomData3._1
  val testVehicleEntity4: VehicleEntity = randomData4._1
  val testVehicleEntity5: VehicleEntity = randomData5._1

  // Vehicles responses
  val testVehicleResponse1: VehicleResponse = randomData1._2
  val testVehicleResponse2: VehicleResponse = randomData2._2
  val testVehicleResponse3: VehicleResponse = randomData3._2
  val testVehicleResponse4: VehicleResponse = randomData4._2
  val testVehicleResponse5: VehicleResponse = randomData5._2

  // LagLong responses
  val testVehicleLatLongResponse1: LatLongResponse = randomData1._3
  val testVehicleLatLongResponse2: LatLongResponse = randomData2._3
  val testVehicleLatLongResponse3: LatLongResponse = randomData3._3
  val testVehicleLatLongResponse4: LatLongResponse = randomData4._3
  val testVehicleLatLongResponse5: LatLongResponse = randomData5._3

  // Tiles responses
  val testVehicleTileResponse1: TileResponse = randomData1._4
  val testVehicleTileResponse2: TileResponse = randomData2._4
  val testVehicleTileResponse3: TileResponse = randomData3._4
  val testVehicleTileResponse4: TileResponse = randomData4._4
  val testVehicleTileResponse5: TileResponse = randomData5._4

}
