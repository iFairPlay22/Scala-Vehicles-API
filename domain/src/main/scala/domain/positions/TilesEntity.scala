package domain.positions

import domain.Entity

import scala.collection.mutable

case class TilesEntity(tileX: Int, tileY: Int) extends Entity {
  require(0 <= tileX, "tile x must be positive or zero")
  require(0 <= tileY, "tile y must be positive or zero")
}

object TilesEntity {

  val separator: String = "_"

  def tilesToKey(tiles: TilesEntity): String =
    new mutable.StringBuilder()
      .addAll(tiles.tileX.toString)
      .addAll(separator)
      .addAll(tiles.tileY.toString)
      .result

  def keyToTiles(tilesKey: String): TilesEntity = {
    val split = tilesKey.split(separator)
    TilesEntity(
      split(0).toInt,
      split(1).toInt
    )
  }
}
