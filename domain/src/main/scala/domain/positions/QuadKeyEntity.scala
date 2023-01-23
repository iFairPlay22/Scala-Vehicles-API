package domain.positions

import domain.Entity

case class QuadKeyEntity(value: String) extends Entity {

  require(value.nonEmpty, "quad key value must be non empty")

}
