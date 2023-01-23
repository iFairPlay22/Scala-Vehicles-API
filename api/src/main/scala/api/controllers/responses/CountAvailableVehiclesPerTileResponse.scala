package api.controllers.responses

import api.controllers.responses.data.VehicleAmountOfTileResponse

case class CountAvailableVehiclesPerTileResponse(amounts: List[VehicleAmountOfTileResponse]) extends Response
