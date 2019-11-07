package model

import model.Main.Cell
import org.scalatest.{Matchers, WordSpec}

class CellSpec extends WordSpec with Matchers {
  "A Cell" when {
    "created" should {
      val createdCell = Cell(0, Array(0, 0), filled = true)
      "be filled" in {
        createdCell.filled should be(true)
      }
      "have an abs pos" in {
        assert(createdCell.absPos >= 0 && createdCell.absPos < 70)
      }
      "be given an Array" in {
        assert(createdCell.xy(0) >= 0 && createdCell.xy(0) <= 25 && createdCell.xy(1) >= 0 && createdCell.xy(1) <= 25)
      }
      "isFilled" in {
        createdCell.isFilled should be(true)
      }
      "fill" in {
        createdCell.fill(false) should be(false)
      }
    }
  }
}
