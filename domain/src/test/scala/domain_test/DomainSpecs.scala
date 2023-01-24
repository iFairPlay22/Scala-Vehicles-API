package domain_test

import domain.data.vehicles.VehicleAmountPerTileEntity
import domain_test.data.RandomData
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class DomainSpecs extends AnyWordSpec with Matchers {

  "Vehicle domain" should {

    "be invalid" in {
      assertThrows[IllegalArgumentException] {
        RandomData.randomInvalidLatLongEntity()
      }
    }

    "be valid" in {
      noException should be thrownBy {
        RandomData.randomValidLatLongEntity()
      }
    }

  }

  "LatLong domain" should {

    "be invalid" in {
      assertThrows[IllegalArgumentException] {
        RandomData.randomInvalidLatLongEntity()
      }
    }

    "be valid" in {
      noException should be thrownBy {
        RandomData.randomValidLatLongEntity()
      }
    }

  }

  "Pixels domain" should {

    "be invalid" in {
      assertThrows[IllegalArgumentException] {
        RandomData.randomInvalidPixelsEntity()
      }
    }

    "be valid" in {
      noException should be thrownBy {
        RandomData.randomValidPixelsEntity()
      }
    }

  }

  "QuadKey domain" should {

    "be invalid" in {
      assertThrows[IllegalArgumentException] {
        RandomData.randomInvalidQuadKeyEntity()
      }
    }

    "be valid" in {
      noException should be thrownBy {
        RandomData.randomValidQuadKeyEntity()
      }
    }

  }

  "Tiles domain" should {

    "be invalid" in {
      assertThrows[IllegalArgumentException] {
        RandomData.randomInvalidTilesEntity()
      }
    }

    "be valid" in {
      noException should be thrownBy {
        RandomData.randomValidTilesEntity()
      }
    }

  }

  "VehicleAmountPerTile domain" should {

    "be valid" in {

      noException should be thrownBy {

        var vehicleAmountPerTile: VehicleAmountPerTileEntity = VehicleAmountPerTileEntity()
        vehicleAmountPerTile.data.shouldEqual(Map[String, Int]())

        vehicleAmountPerTile = VehicleAmountPerTileEntity.addEntry(vehicleAmountPerTile, "0_0")
        vehicleAmountPerTile.data.shouldEqual(Map[String, Int]("0_0" -> 1))

        vehicleAmountPerTile = VehicleAmountPerTileEntity.addEntry(vehicleAmountPerTile, "1_1")
        vehicleAmountPerTile.data.shouldEqual(Map[String, Int]("0_0" -> 1, "1_1" -> 1))

        vehicleAmountPerTile = VehicleAmountPerTileEntity.addEntry(vehicleAmountPerTile, "1_1")
        vehicleAmountPerTile.data.shouldEqual(Map[String, Int]("0_0" -> 1, "1_1" -> 2))

        vehicleAmountPerTile = VehicleAmountPerTileEntity.addEntry(vehicleAmountPerTile, "1_1")
        vehicleAmountPerTile.data.shouldEqual(Map[String, Int]("0_0" -> 1, "1_1" -> 3))

        vehicleAmountPerTile = VehicleAmountPerTileEntity.addEntry(vehicleAmountPerTile, "2_2")
        vehicleAmountPerTile.data.shouldEqual(Map[String, Int]("0_0" -> 1, "1_1" -> 3, "2_2" -> 1))

        vehicleAmountPerTile = VehicleAmountPerTileEntity.addEntry(vehicleAmountPerTile, "0_0")
        vehicleAmountPerTile.data.shouldEqual(Map[String, Int]("0_0" -> 2, "1_1" -> 3, "2_2" -> 1))
      }
    }

  }

}
