package model

import org.scalatest.{Matchers, WordSpec}

class BoardSpec extends WordSpec with Matchers {
  "A Board" when {
    "initialized" should {
      val b = Board()
      "have an Array" in {
        b.cells should not be null
      }
      "have a Dimension" in {
        //val checkDim = (a: Array[Int]) => a(0) > 0 && a(1) > 0
        assert(b.dim(0) > 0 && b.dim(1) > 0)
      }
    }
  }
}
