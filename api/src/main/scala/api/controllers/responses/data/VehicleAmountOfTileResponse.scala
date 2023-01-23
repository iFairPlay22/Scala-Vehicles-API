package api.controllers.responses.data

import api.controllers.responses.Response

case class VehicleAmountOfTileResponse(
    tile: TileResponse,
    amount: Int
) extends Response {}
