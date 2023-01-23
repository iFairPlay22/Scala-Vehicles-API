package domain.positions

import domain.Entity

case class LatLongEntity(latitude: Double, longitude: Double) extends Entity {
  require(0 <= latitude && latitude <= 90, "vehicle latitude must be between 0 and 90")
  require(-180 <= longitude && longitude <= 180, "vehicle longitude must be between -180 and 180")
}
