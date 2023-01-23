package infra.requests.vehicles

import infra.requests.Request

case class VehicleRequest(
    id: Int,
    routeId: Int,
    runId: Int,
    heading: Int,
    predictable: Boolean,
    latitude: Double,
    longitude: Double,
    secondsSinceReport: Int
) extends Request {
  require(0 <= id, "vehicle input id must be positive or zero")
  require(0 <= routeId, "vehicle input route id must be positive or zero")
  require(0 <= runId, "vehicle input run id must be positive or zero")
  require(0 <= heading, "vehicle input heading must be positive or zero")
  require(
    0 <= latitude && latitude <= 90,
    "vehicle input latitude must be between 0 and 90 %s".format(latitude)
  )
  require(
    -180 <= longitude && longitude <= 180,
    "vehicle input longitude must be between -180 and 180"
  )
  require(0 <= id, "vehicle input seconds since report must be positive or zero")
}
