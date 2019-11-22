package model

import org.scalatest.{Matchers, WordSpec}

class CellSpec extends WordSpec with Matchers {
  "A Cell" when {
    "created" should {
      val cellSpecTrue = Cell(42, filled = true, null)
      val cellSpecFalse = Cell(24, filled = false, null)
      "be filled" in {
        cellSpecTrue.filled should be(true)
      }
      "not be filled" in {
        cellSpecFalse.filled should be(false)
      }
      "have idx" in {
        cellSpecTrue.getPos should be(42)
      }
      "return if its filled" in {
        cellSpecTrue.isFilled should be(true)
      }
      "have a position" in {
        cellSpecTrue.idx should be(42)
      }
      "print itself out" in {
        cellSpecTrue.toString.startsWith("[") should be(true)
      }
    }
  }
}
