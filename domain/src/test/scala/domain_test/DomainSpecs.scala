package domain_test

import domain.data.vehicles.VehicleDomain
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class DomainSpecs extends AnyWordSpec with Matchers {

  "Vehicle domain" should {

    Range
      .inclusive(0, 50)
      .foreach(r => {

        s"be invalid $r" in {
          assertThrows[IllegalArgumentException] {
            VehicleDomain.randomInvalid()
          }
        }

        s"be valid $r" in {
          noException shouldBe thrownBy {
            VehicleDomain.randomValid()
          }
        }

      })

  }

}
