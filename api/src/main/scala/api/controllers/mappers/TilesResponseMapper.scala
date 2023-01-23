package api.controllers.mappers

import api.controllers.responses.GetFilledTilesResponse
import api.controllers.responses.data.TileResponse
import domain.positions.TilesEntity

object TilesResponseMapper {
  def entityToResponse(tiles: Set[TilesEntity]): GetFilledTilesResponse =
    GetFilledTilesResponse(
      tiles.map(
        tile =>
          TileResponse(
            TilesEntity.tilesToKey(tile)
        ))
    )
}
