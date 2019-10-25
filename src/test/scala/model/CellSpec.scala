package model

import org.scalatest.{Matchers, WordSpec}

class CellSpec extends WordSpec with Matchers {
  "A Cell" when {
    "created" should {
      val createdCell = Cell(0, Array(0, 0), true)
      "be filled" in {
        createdCell.filled should be(true)
      }
    }
  }
}
