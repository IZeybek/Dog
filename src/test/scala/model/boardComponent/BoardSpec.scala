package model.boardComponent

import org.scalatest.{Matchers, WordSpec}

class BoardSpec extends WordSpec with Matchers {
  "A Board" when {
    "initialized" should {
      val b: Board = Read.readIn("src/feld.txt")
      "have an Array" in {
        b.cells should not be null
      }
      "have a Dimension" in {
        val greaterNull = (i: Int) => i > 0
        greaterNull(b.xy(0)) && greaterNull(b.xy(1)) should be(true)
      }
    }
  }
}
