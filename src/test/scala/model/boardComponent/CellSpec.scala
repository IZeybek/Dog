package model.boardComponent

import model.Cell
import org.scalatest.{Matchers, WordSpec}

class CellSpec extends WordSpec with Matchers {
  "A Cell" when {
    "created" should {
      val cellSpec = Cell(42, true)
      "be filled" in {
        cellSpec.filled should be(true)
      }
      "have idx" in {
        cellSpec.getPos should be(42)
      }
      "return if its filled" in {
        cellSpec.isFilled should be(true)
      }
      "have a position" in {
        cellSpec.idx should be(42)
      }
      "print itself out" in {
        cellSpec.toString should be("[x]")
      }
    }
  }
}
