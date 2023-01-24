package api_test.data

import api.controllers.responses.data.{ LatLongResponse, TileResponse, VehicleResponse }
import api.services.map.TileSystem
import domain.data.positions.LatLongEntity
import domain.data.vehicles.VehicleEntity
import domain.util.RandomUtils

object RandomData {

  def randomVehicleData(
      id: Int
  ): (VehicleEntity, VehicleResponse, LatLongResponse, TileResponse) = {
    val latitude  = RandomUtils.randomInt(start = 0, end = 90)
    val longitude = RandomUtils.randomInt(start = -180, end = 180)
    randomVehicleDataWithSpecs(id, latitude, longitude)
  }

  def randomVehicleDataWithSpecs(
      id: Int,
      latitude: Double,
      longitude: Double
  ): (VehicleEntity, VehicleResponse, LatLongResponse, TileResponse) = {
    val routeId            = RandomUtils.randomInt()
    val runId              = RandomUtils.randomInt()
    val heading            = RandomUtils.randomInt()
    val predictable        = RandomUtils.randomBool()
    val secondsSinceReport = RandomUtils.randomInt()
    val tilesEntity =
      TileSystem.pixelXYToTileXY(TileSystem.latLongToPixelXY(LatLongEntity(latitude, longitude)))
    (
      VehicleEntity(
        id                 = id,
        routeId            = routeId,
        runId              = runId,
        heading            = heading,
        predictable        = predictable,
        latLong            = LatLongEntity(latitude, longitude),
        secondsSinceReport = secondsSinceReport
      ),
      VehicleResponse(
        id                 = id,
        routeId            = routeId,
        runId              = runId,
        heading            = heading,
        predictable        = predictable,
        latitude           = latitude,
        longitude          = longitude,
        secondsSinceReport = secondsSinceReport
      ),
      LatLongResponse(
        latitude  = latitude,
        longitude = longitude
      ),
      TileResponse(
        id = tilesEntity.tileX + "_" + tilesEntity.tileY
      )
    )
  }

}
