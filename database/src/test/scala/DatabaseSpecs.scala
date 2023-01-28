import akka.Done
import data.TestData
import database.repositories.VehicleRepository
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.{ AnyWordSpec, AsyncWordSpec }

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import database.Main.{ databaseExecutionContext, databaseSystem }

class DatabaseSpecs extends AsyncWordSpec with Matchers with BeforeAndAfterAll with TestData {

  override def beforeAll(): Unit = {
    super.beforeAll()
    database.Main.init()
  }

  "data = []" should {

    "truncate()" in {

      Await
        .ready(VehicleRepository.truncateTable(), Duration.Inf)
        .map { eitherErrorOrTruncate =>
          eitherErrorOrTruncate.isRight.shouldEqual(right   = true)
          eitherErrorOrTruncate.right.get.shouldEqual(right = Done)
        }

    }

    "be empty with selectAll()" in {

      Await
        .ready(VehicleRepository.selectAll(), Duration.Inf)
        .map { eitherErrorOrVehicles =>
          eitherErrorOrVehicles.isRight.shouldEqual(right = true)
          eitherErrorOrVehicles.right.get.toSet.shouldEqual(Set())
        }

    }

    "be empty with selectOne(v1)" in {

      Await
        .ready(VehicleRepository.selectOne(id = v1.id), Duration.Inf)
        .map { eitherErrorOrVehicle =>
          eitherErrorOrVehicle.isRight.shouldEqual(right           = true)
          eitherErrorOrVehicle.right.get.isEmpty.shouldEqual(right = true)
        }

    }

    "be empty with selectOne(v2)" in {

      Await
        .ready(VehicleRepository.selectOne(id = v2.id), Duration.Inf)
        .map { eitherErrorOrVehicle =>
          eitherErrorOrVehicle.isRight.shouldEqual(right           = true)
          eitherErrorOrVehicle.right.get.isEmpty.shouldEqual(right = true)
        }

    }

  }

  "data = [v1]" should {

    "add(v1)" in {

      Await
        .ready(VehicleRepository.insertOrEdit(v1), Duration.Inf)
        .map { eitherErrorOrUpdate =>
          eitherErrorOrUpdate.isRight.shouldEqual(right   = true)
          eitherErrorOrUpdate.right.get.shouldEqual(right = Done)
        }

    }

    "be [v1] with selectAll()" in {

      Await
        .ready(VehicleRepository.selectAll(), Duration.Inf)
        .map { eitherErrorOrVehicles =>
          eitherErrorOrVehicles.isRight.shouldEqual(right = true)
          eitherErrorOrVehicles.right.get.toSet.shouldEqual(Set(v1))
        }

    }

    "be v1 with selectOne(v1)" in {

      Await
        .ready(VehicleRepository.selectOne(id = v1.id), Duration.Inf)
        .map { eitherErrorOrVehicle =>
          eitherErrorOrVehicle.isRight.shouldEqual(right             = true)
          eitherErrorOrVehicle.right.get.isDefined.shouldEqual(right = true)
          eitherErrorOrVehicle.right.get.get.shouldEqual(v1)
        }

    }

    "be empty with selectOne(v2)" in {

      Await
        .ready(VehicleRepository.selectOne(id = v2.id), Duration.Inf)
        .map { eitherErrorOrVehicle =>
          eitherErrorOrVehicle.isRight.shouldEqual(right           = true)
          eitherErrorOrVehicle.right.get.isEmpty.shouldEqual(right = true)
        }

    }

  }

  "data = [v1 => v3, v2]" should {

    "add(v2, v3)" in {

      Await
        .ready(VehicleRepository.insertOrEdit(List(v2, v3)), Duration.Inf)
        .map { eitherErrorOrUpdate =>
          eitherErrorOrUpdate.isRight.shouldEqual(right   = true)
          eitherErrorOrUpdate.right.get.shouldEqual(right = Done)
        }

    }

    "be [v1 => v3, v2] with selectAll()" in {

      Await
        .ready(VehicleRepository.selectAll(), Duration.Inf)
        .map { eitherErrorOrVehicles =>
          eitherErrorOrVehicles.isRight.shouldEqual(right = true)
          eitherErrorOrVehicles.right.get.toSet.shouldEqual(Set(v3, v2))
        }

    }

    "be v3 with selectOne(v1)" in {

      Await
        .ready(VehicleRepository.selectOne(id = v1.id), Duration.Inf)
        .map { eitherErrorOrVehicle =>
          eitherErrorOrVehicle.isRight.shouldEqual(right             = true)
          eitherErrorOrVehicle.right.get.isDefined.shouldEqual(right = true)
          eitherErrorOrVehicle.right.get.get.shouldEqual(v3)
        }

    }

    "be v2 with selectOne(v2)" in {

      Await
        .ready(VehicleRepository.selectOne(id = v2.id), Duration.Inf)
        .map { eitherErrorOrVehicle =>
          eitherErrorOrVehicle.isRight.shouldEqual(right = true)
          eitherErrorOrVehicle.right.get.get.shouldEqual(v2)
        }

    }

  }

  override def afterAll(): Unit =
    super.afterAll()
    // database.Main.terminate()

}
