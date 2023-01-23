package api.controllers.responses

import api.controllers.responses.data.TileResponse

case class GetFilledTilesResponse(tiles: Set[TileResponse]) extends Response
