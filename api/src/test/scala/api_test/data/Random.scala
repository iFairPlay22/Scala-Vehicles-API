package api_test.data

import api.controllers.responses.data.{ LatLongResponse, TileResponse, VehicleResponse }
import api.services.map.TileSystem
import domain.positions.LatLongEntity
import domain.vehicles.VehicleEntity

object Random {

  val rand = new scala.util.Random

  def randomInt(start: Int = 0, end: Int = Int.MaxValue): Int =
    rand.between(start, end)

  def randomBool(): Boolean = rand.nextBoolean()

  def randomVehicleData(
      id: Int
  ): (VehicleEntity, VehicleResponse, LatLongResponse, TileResponse) = {
    val latitude  = randomInt(start = 0, end = 90)
    val longitude = randomInt(start = -180, end = 180)
    randomVehicleDataWithSpecs(id, latitude, longitude)
  }

  def randomVehicleDataWithSpecs(
      id: Int,
      latitude: Double,
      longitude: Double
  ): (VehicleEntity, VehicleResponse, LatLongResponse, TileResponse) = {
    val routeId            = randomInt()
    val runId              = randomInt()
    val heading            = randomInt()
    val predictable        = randomBool()
    val secondsSinceReport = randomInt()
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
