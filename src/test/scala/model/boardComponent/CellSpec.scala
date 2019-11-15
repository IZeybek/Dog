package model.boardComponent

import org.scalatest.{Matchers, WordSpec}

class CellSpec extends WordSpec with Matchers {
  "A Cell" when {
    "created" should {
      val cellSpec = Cell(0,true)
      "be filled" in {
        cellSpec.filled should be(true)
      }
      "print itself out" in {
        cellSpec.toString should be("[x]")
      }
      "be updated" in {
        cellSpec.fill(false) should be(false)
        "be filled" in {
          cellSpec.filled should be(false)
        }
      }
    }
  }
}
