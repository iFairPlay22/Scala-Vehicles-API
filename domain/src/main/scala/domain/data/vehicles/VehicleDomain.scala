package domain.data.vehicles

import commons.data._Domain
import commons.utils._RandomGen

case class VehicleDomain(id: Int, name: String) extends _Domain {
  require(0 <= id, "vehicle id must be positive or zero")
  require(name.nonEmpty, "vehicle name must be non empty")
  require(
    3 <= name.length() && name.length() <= 10,
    "vehicle name must have a length between 3 and 10")
}

object VehicleDomain extends _RandomGen[VehicleDomain] {
  override def randomValid(): VehicleDomain =
    VehicleDomain(id = randomInt(), name = randomString(randomInt(3, 10)))

  override def randomInvalid(): VehicleDomain =
    randomInt(0, 2) match {
      case 0 =>
        randomValid()
          .copy(id = randomInt(start = -100, end = -1))
      case 1 =>
        randomValid()
          .copy(name = randomString(randomInt(0, 2)))
      case 2 =>
        randomValid()
          .copy(name = randomString(randomInt(11, 100)))
      case _ => throw new NotImplementedError()
    }
}
