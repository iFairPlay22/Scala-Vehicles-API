package domain.positions

import domain.Entity

case class PixelsEntity(pixelX: Int, pixelY: Int) extends Entity {
  require(0 <= pixelX, "pixel x must be positive or zero")
  require(0 <= pixelY, "pixel y must be positive or zero")
}
