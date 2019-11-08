package model

import model.Main.Board
import org.scalatest.{Matchers, WordSpec}

class BoardSpec extends WordSpec with Matchers {
  "A Board" when {
    "initialized" should {
      val b = Board(Array(25, 25), Array(null))
      "have an Array" in {
        b.cells should not be null
      }
      "have a Dimension" in {
        assert(b.xy(0) > 0 && b.xy(1) > 0)
      }
    }
  }
}
