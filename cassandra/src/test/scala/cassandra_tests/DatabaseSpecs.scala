package cassandra_tests

import cassandra._CassandraTestSystem
import cassandra.vehicles.repositories.VehicleRepository
import commons.actor._ActorSystem
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.should.Matchers

import java.time.LocalDate

class DatabaseSpecs
    extends _ActorSystem
    with _CassandraTestSystem
    with Matchers
    with ScalaFutures
    with SpecsData {

  private val vehiclesRepository: VehicleRepository = new VehicleRepository()
  private val today: LocalDate = LocalDate.now

  f"return empty vehicles list" in {

    val vehicles = await(vehiclesRepository.selectAllByDate(today))

    vehicles shouldBe Seq()

  }

  f"return vehicles list of length 1" in {

    await(vehiclesRepository.insertOrEdit(vehicle = vehicle1))

    val vehicles = await(vehiclesRepository.selectAllByDate(today))
    vehicles shouldBe Seq(vehicle1)

  }

  f"return vehicles list of length 2" in {

    await(vehiclesRepository.insertOrEdit(vehicle = vehicle1))
    await(vehiclesRepository.insertOrEdit(vehicle = vehicle2))

    val vehicles = await(vehiclesRepository.selectAllByDate(today))
    vehicles shouldBe Seq(vehicle1, vehicle2)

  }

  f"return vehicles list of length 3" in {

    await(vehiclesRepository.insertOrEdit(vehicle = vehicle1))
    await(vehiclesRepository.insertOrEdit(vehicle = vehicle3))
    await(vehiclesRepository.insertOrEdit(vehicle = vehicle2))

    val vehicles = await(vehiclesRepository.selectAllByDate(today))
    vehicles shouldBe Seq(vehicle1, vehicle2, vehicle3)

  }

  f"return vehicles list of length (no duplicates)" in {

    await(vehiclesRepository.insertOrEdit(vehicle = vehicle1))
    await(vehiclesRepository.insertOrEdit(vehicle = vehicle2))
    await(vehiclesRepository.insertOrEdit(vehicle = vehicle2))

    val vehicles = await(vehiclesRepository.selectAllByDate(today))
    vehicles shouldBe Seq(vehicle1, vehicle2)

  }
}
