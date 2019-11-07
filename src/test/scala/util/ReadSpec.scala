package util

import org.scalatest.{Matchers, WordSpec}

class ReadSpec extends WordSpec with Matchers {
  "Read" should {
    val b = Board()
    val r = Read(b)
    "create a new Board" in {
      r.readIn("src/feld.txt") should be(b)
      r.prettyPrint()
    }
  }
}
