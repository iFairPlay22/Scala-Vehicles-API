package domain.vehicles

import domain.Entity
import domain.positions.LatLongEntity

case class VehicleEntity(
    id: Int,
    routeId: Int,
    runId: Int,
    heading: Int,
    predictable: Boolean,
    latLong: LatLongEntity,
    secondsSinceReport: Int
) extends Entity {
  require(0 <= id, "vehicle id must be positive or zero")
  require(0 <= routeId, "vehicle route id must be positive or zero")
  require(0 <= runId, "vehicle run id must be positive or zero")
  require(0 <= heading, "vehicle heading must be positive or zero")
  require(latLong != null, "position must be non null")
  require(0 <= id, "vehicle seconds since report must be positive or zero")
}
