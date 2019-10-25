package util

import model.Board
import org.scalatest.{Matchers, WordSpec}

class ReadSpec extends WordSpec with Matchers {
  "Read" should {
    val b = Board()
    val r = Read()
    "create a new Board" in {
      r.readIn("src/feld.txt", b) should be(b)
      r.prettyPrint(b)
    }
  }
}
