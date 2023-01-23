package api.services.map

import domain.positions.{ LatLongEntity, QuadKeyEntity, TilesEntity }

object MapService {

  def getTilesFromLatLong(latLong: LatLongEntity): TilesEntity =
    TileSystem.pixelXYToTileXY(TileSystem.latLongToPixelXY(latLong))

  def getPixelsFromTiles(tiles: TilesEntity): QuadKeyEntity =
    TileSystem.tileXYToQuadKey(tiles)

  def getPixelsFromLatLong(latLong: LatLongEntity): QuadKeyEntity =
    TileSystem.tileXYToQuadKey(TileSystem.pixelXYToTileXY(TileSystem.latLongToPixelXY(latLong)))
}
