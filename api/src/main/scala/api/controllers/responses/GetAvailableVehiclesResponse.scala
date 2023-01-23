package api.controllers.responses

import api.controllers.responses.data.VehicleResponse

case class GetAvailableVehiclesResponse(vehicles: Seq[VehicleResponse]) extends Response
