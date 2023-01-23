package api.controllers.responses

import api.controllers.responses.data.VehicleResponse

case class GetAvailableVehiclesInTileResponse(vehicles: Seq[VehicleResponse]) extends Response
