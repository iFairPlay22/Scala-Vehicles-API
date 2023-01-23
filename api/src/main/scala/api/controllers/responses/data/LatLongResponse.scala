package api.controllers.responses.data

import api.controllers.responses.Response

case class LatLongResponse(
    latitude: Double,
    longitude: Double
) extends Response
