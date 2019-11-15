package model.boardComponent

import org.scalatest.{Matchers, WordSpec}

class CellSpec extends WordSpec with Matchers {
  "A Cell" when {
    "created" should {
      val cellSpec = new Cell(0)
      "be filled" in {
        cellSpec.filled should be(true)
      }
      "print itself out" in {
        cellSpec.toString should be("[x]")
      }
      "be updated" in {
        cellSpec.fill(false) should be(false)
      }
    }
  }
}
