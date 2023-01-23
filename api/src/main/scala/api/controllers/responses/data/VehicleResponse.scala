package api.controllers.responses.data

import api.controllers.responses.Response

case class VehicleResponse(
    id: Int,
    routeId: Int,
    runId: Int,
    heading: Int,
    predictable: Boolean,
    latitude: Double,
    longitude: Double,
    secondsSinceReport: Int
) extends Response
