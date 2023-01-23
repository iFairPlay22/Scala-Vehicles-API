package api_test

import akka.http.scaladsl.testkit.ScalatestRouteTest
import api.controllers.responses.data.VehicleAmountOfTileResponse
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import api_test.data.SpecsData
import api_test.config.SpecsConfig
import api_test.test.SpecsTests

import scala.concurrent.duration.Duration
import scala.concurrent.Await

class AppSpecs
    extends AnyWordSpecLike
    with Matchers
    with BeforeAndAfterAll
    with ScalatestRouteTest
    with SpecsConfig
    with SpecsTests
    with SpecsData {

  override def beforeAll(): Unit = {
    super.beforeAll()
    setupAndStartEnvironment()
  }

  f"data = []" should {

    f"empty vehicles table" in {
      Await.ready(resetVehicleEnvironment(), Duration.Inf)
    }

    f"get vehicles list" in {
      testVehiclesList()
    }

    f"get tiles filled" in {
      testFilledTiles()
    }

    f"get 1st tile ${testVehicleTileResponse1} vehicles list" in {
      testVehiclesInTile(testVehicleTileResponse1)
    }

    f"get vehicles list count by tile" in {
      testVehicleCountPerTile()
    }
  }

  f"data = [v1]" should {

    f"add v1" in {
      Await.ready(addVehiclesInEnvironment(testVehicleEntity1), Duration.Inf)
    }

    f"get vehicles list" in {
      testVehiclesList(testVehicleResponse1)
    }

    f"get 1st vehicle last position" in {
      testVehicleLastPosition(testVehicleEntity1, testVehicleLatLongResponse1)
    }

    f"get tiles filled" in {
      testFilledTiles(testVehicleTileResponse1)
    }

    f"get 1st tile ${testVehicleTileResponse1} vehicles list" in {
      testVehiclesInTile(testVehicleTileResponse1, testVehicleResponse1)
    }

    f"get vehicles list count by tile" in {
      testVehicleCountPerTile(VehicleAmountOfTileResponse(testVehicleTileResponse1, 1))
    }
  }

  f"data = [v1, v2, v3]" should {

    f"add v2, v3" in {

      Await.ready(
        addVehiclesInEnvironment(testVehicleEntity2, testVehicleEntity3),
        Duration.Inf
      )

    }

    f"get vehicles list" in {
      testVehiclesList(testVehicleResponse1, testVehicleResponse2, testVehicleResponse3)
    }

    f"get 1st vehicle last position" in {
      testVehicleLastPosition(testVehicleEntity1, testVehicleLatLongResponse1)
    }

    f"get 2nd vehicle last position" in {
      testVehicleLastPosition(testVehicleEntity2, testVehicleLatLongResponse2)
    }

    f"get 3rd vehicle last position" in {
      testVehicleLastPosition(testVehicleEntity3, testVehicleLatLongResponse3)
    }

    f"get tiles filled" in {
      testFilledTiles(
        testVehicleTileResponse1,
        testVehicleTileResponse2,
        testVehicleTileResponse3
      )
    }

    f"get 1st tile ${testVehicleTileResponse1} vehicles list" in {
      testVehiclesInTile(testVehicleTileResponse1, testVehicleResponse1)
    }

    f"get 2nd tile ${testVehicleTileResponse2} vehicles list" in {
      testVehiclesInTile(testVehicleTileResponse2, testVehicleResponse2)
    }

    f"get 3rd tile ${testVehicleTileResponse3} vehicles list" in {
      testVehiclesInTile(testVehicleTileResponse3, testVehicleResponse3)
    }

    f"get vehicles list count by tile" in {
      testVehicleCountPerTile(
        VehicleAmountOfTileResponse(testVehicleTileResponse1, 1),
        VehicleAmountOfTileResponse(testVehicleTileResponse2, 1),
        VehicleAmountOfTileResponse(testVehicleTileResponse3, 1)
      )
    }
  }

  f"data = [v1, v2 => v5, v3, v4]" should {

    f"add v4, v5" in {

      Await.ready(
        addVehiclesInEnvironment(testVehicleEntity4, testVehicleEntity5),
        Duration.Inf
      )

    }

    f"get vehicles list" in {
      testVehiclesList(
        testVehicleResponse1,
        testVehicleResponse5, // Replaced v2 because same id
        testVehicleResponse3,
        testVehicleResponse4
      )
    }

    f"get 1st vehicle last position" in {
      testVehicleLastPosition(testVehicleEntity1, testVehicleLatLongResponse1)
    }

    f"get 2nd => 5th vehicle last position" in {
      testVehicleLastPosition(
        testVehicleEntity5,
        testVehicleLatLongResponse5
      ) // We replaced testVehicleLatLongResponse2 by testVehicleLatLongResponse5 because same id so there was an override
    }

    f"get 3rd vehicle last position" in {
      testVehicleLastPosition(testVehicleEntity3, testVehicleLatLongResponse3)
    }

    f"get 4th vehicle last position" in {
      testVehicleLastPosition(testVehicleEntity4, testVehicleLatLongResponse4)
    }

    f"get tiles filled" in {
      testFilledTiles(
        testVehicleTileResponse1,
        testVehicleTileResponse5, // We replaced testVehicleLatLongResponse2 by testVehicleLatLongResponse5 because same id so there was an override
        testVehicleTileResponse3
        // We did not add testVehicleTileResponse4, because it has the same tile as testVehicleTileResponse3
      )
    }

    f"get 1st tile ${testVehicleTileResponse1} vehicles list" in {
      testVehiclesInTile(testVehicleTileResponse1, testVehicleResponse1)
    }

    f"get 2nd tile ${testVehicleTileResponse5} vehicles list" in {
      testVehiclesInTile(
        testVehicleTileResponse5,
        testVehicleResponse5
      ) // We replaced testVehicleLatLongResponse2 by testVehicleLatLongResponse5 because same id so there was an override
    }

    f"get 3rd tile ${testVehicleTileResponse3} vehicles list" in {
      testVehiclesInTile(
        testVehicleTileResponse3,
        testVehicleResponse3,
        testVehicleResponse4
      ) // We added testVehicleTileResponse4, because it has the same tile as testVehicleTileResponse3
    }

    f"get vehicles list count by tile" in {
      testVehicleCountPerTile(
        VehicleAmountOfTileResponse(testVehicleTileResponse1, 1),
        VehicleAmountOfTileResponse(
          testVehicleTileResponse5,
          1
        ), // We replaced testVehicleLatLongResponse2 by testVehicleLatLongResponse5 because same id so there was an override
        VehicleAmountOfTileResponse(
          testVehicleTileResponse3,
          2
        ) // We did a +1, because we have now testVehicleTileResponse3 and testVehicleTileResponse4
      )
    }
  }

  override def afterAll(): Unit = {
    super.afterAll()
    stopEnvironment()
  }
}
