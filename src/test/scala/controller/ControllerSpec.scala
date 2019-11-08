package controller

import org.scalatest.{Matchers, WordSpec}

class ControllerSpec extends WordSpec with Matchers {

  "A Controller" when {
    "created" should {
      val controller: ControllerTrait = new Controller
      "print Board" in {
        controller.printBoard()
      }
    }
  }
}
