package util

import model.BoardTrait
import org.scalatest.{Matchers, WordSpec}

class ReadSpec extends WordSpec with Matchers {
  "Read" can {
    val r = Read()
    "reading in Board" should {
      val b: BoardTrait = r.readIn("src/feld.txt")
      "have size" in {
        r.readIn("src/feld.txt").xy should be(b.xy)
      }
      "have array" in {
        r.readIn("src/feld.txt").cells should equal(b.cells)
      }
      "print out board" in {
        r.prettyPrint(b)
      }
    }
  }
}
