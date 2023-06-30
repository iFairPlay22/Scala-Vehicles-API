package cassandra_tests

import domain.vehicles.VehicleDomain

trait SpecsData {

  // Random valid vehicles
  val vehicle1: VehicleDomain = VehicleDomain.randomValid().copy(id = 1)
  val vehicle2: VehicleDomain = VehicleDomain.randomValid().copy(id = 2)
  val vehicle3: VehicleDomain = VehicleDomain.randomValid().copy(id = 3)

}
