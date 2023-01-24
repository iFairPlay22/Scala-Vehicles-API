package data

import domain.data.positions.LatLongEntity
import domain.data.vehicles.VehicleEntity

trait TestData {

  val v1: VehicleEntity = VehicleEntity(
    id                 = 0,
    routeId            = 0,
    runId              = 0,
    heading            = 0,
    predictable        = true,
    latLong            = LatLongEntity(0, 0),
    secondsSinceReport = 0
  )

  val v2: VehicleEntity = VehicleEntity(
    id                 = 1,
    routeId            = 1,
    runId              = 1,
    heading            = 1,
    predictable        = false,
    latLong            = LatLongEntity(1, 1),
    secondsSinceReport = 1
  )

  val v3: VehicleEntity = VehicleEntity(
    id                 = v1.id,
    routeId            = 2,
    runId              = 2,
    heading            = 2,
    predictable        = false,
    latLong            = LatLongEntity(2, 2),
    secondsSinceReport = 2
  ) // Same id as v1

}
