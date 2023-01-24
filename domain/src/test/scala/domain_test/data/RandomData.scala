package domain_test.data

import domain.data.positions.{ LatLongEntity, PixelsEntity, QuadKeyEntity, TilesEntity }
import domain.data.vehicles.VehicleEntity
import domain.util.RandomUtils
import jdk.jshell.spi.ExecutionControl.NotImplementedException

object RandomData {

  def randomValidLatLongEntity(): LatLongEntity =
    LatLongEntity(
      latitude  = RandomUtils.randomInt(start = 0, end = 90),
      longitude = RandomUtils.randomInt(start = -180, end = 180)
    )

  def randomValidVehicleEntity(): VehicleEntity =
    VehicleEntity(
      id                 = RandomUtils.randomInt(),
      routeId            = RandomUtils.randomInt(),
      runId              = RandomUtils.randomInt(),
      heading            = RandomUtils.randomInt(),
      predictable        = RandomUtils.randomBool(),
      latLong            = randomValidLatLongEntity(),
      secondsSinceReport = RandomUtils.randomInt()
    )

  def randomValidPixelsEntity(): PixelsEntity =
    PixelsEntity(
      pixelX = RandomUtils.randomInt(),
      pixelY = RandomUtils.randomInt()
    )

  def randomValidQuadKeyEntity(): QuadKeyEntity =
    QuadKeyEntity(RandomUtils.randomString(10))

  def randomValidTilesEntity(): TilesEntity = TilesEntity(
    tileX = RandomUtils.randomInt(),
    tileY = RandomUtils.randomInt()
  )

  def randomInvalidLatLongEntity(): LatLongEntity =
    RandomUtils.randomInt(0, 4) match {
      case 0 =>
        // Invalid latitude (too low)
        LatLongEntity(
          latitude  = RandomUtils.randomInt(start = Int.MinValue, end = 0),
          longitude = RandomUtils.randomInt(start = -180, end = 180)
        )
      case 1 =>
        // Invalid latitude (too high)
        LatLongEntity(
          latitude  = RandomUtils.randomInt(start = 180 + 1, end = Int.MaxValue),
          longitude = RandomUtils.randomInt(start = -180, end = 180)
        )
      case 2 =>
        // Invalid longitude (too low)
        LatLongEntity(
          latitude  = RandomUtils.randomInt(start = -180, end = 180),
          longitude = RandomUtils.randomInt(start = Int.MinValue, end = -180)
        )
      case 3 =>
        // Invalid longitude (too high)
        LatLongEntity(
          latitude  = RandomUtils.randomInt(start = -180, end = 180),
          longitude = RandomUtils.randomInt(start = 180 + 1, end = Int.MaxValue)
        )
      case _ => throw new NotImplementedException("Forgot to implement a case in match")
    }

  def randomInvalidVehicleEntity(): VehicleEntity =
    RandomUtils.randomInt(0, 3) match {
      case 0 =>
        // A negative number in a mandatory positive field
        val negative_number = RandomUtils.randomInt(Int.MinValue, 0)
        val r               = RandomUtils.randomInt(0, 5)

        VehicleEntity(
          id                 = if (r == 0) negative_number else RandomUtils.randomInt(),
          routeId            = if (r == 1) negative_number else RandomUtils.randomInt(),
          runId              = if (r == 2) negative_number else RandomUtils.randomInt(),
          heading            = if (r == 3) negative_number else RandomUtils.randomInt(),
          predictable        = RandomUtils.randomBool(),
          latLong            = randomValidLatLongEntity(),
          secondsSinceReport = if (r == 4) negative_number else RandomUtils.randomInt()
        )
      case 1 =>
        // An invalid latitude
        VehicleEntity(
          id                 = RandomUtils.randomInt(),
          routeId            = RandomUtils.randomInt(),
          runId              = RandomUtils.randomInt(),
          heading            = RandomUtils.randomInt(),
          predictable        = RandomUtils.randomBool(),
          latLong            = randomInvalidLatLongEntity(),
          secondsSinceReport = RandomUtils.randomInt()
        )
      case _ => throw new NotImplementedException("Forgot to implement a case in match")
    }

  def randomInvalidPixelsEntity(): PixelsEntity =
    RandomUtils.randomInt(0, 4) match {
      case 0 =>
        // Negative pixel x
        PixelsEntity(
          pixelX = RandomUtils.randomInt(Int.MinValue, 0),
          pixelY = RandomUtils.randomInt()
        )
      case 1 =>
        // Negative pixel y
        PixelsEntity(
          pixelX = RandomUtils.randomInt(),
          pixelY = RandomUtils.randomInt(Int.MinValue, 0)
        )
      case 2 =>
        // Negative pixel x & y
        PixelsEntity(
          pixelX = RandomUtils.randomInt(Int.MinValue, 0),
          pixelY = RandomUtils.randomInt(Int.MinValue, 0)
        )
      case _ => throw new NotImplementedException("Forgot to implement a case in match")
    }

  def randomInvalidQuadKeyEntity(): QuadKeyEntity =
    QuadKeyEntity("")

  def randomInvalidTilesEntity(): TilesEntity =
    RandomUtils.randomInt(0, 4) match {
      case 0 =>
        // Negative pixel x
        TilesEntity(
          tileX = RandomUtils.randomInt(Int.MinValue, 0),
          tileY = RandomUtils.randomInt()
        )
      case 1 =>
        // Negative pixel y
        TilesEntity(
          tileX = RandomUtils.randomInt(),
          tileY = RandomUtils.randomInt(Int.MinValue, 0)
        )
      case 2 =>
        // Negative pixel x & y
        TilesEntity(
          tileX = RandomUtils.randomInt(Int.MinValue, 0),
          tileY = RandomUtils.randomInt(Int.MinValue, 0)
        )
      case _ => throw new NotImplementedException("Forgot to implement a case in match")
    }
}
