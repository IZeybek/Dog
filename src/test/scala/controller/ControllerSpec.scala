package controller

import org.scalatest.{Matchers, WordSpec}

class ControllerSpec extends WordSpec with Matchers {

  "A Controller" when {
    "created" should{
      var controller: ControllerTrait = new Controller
      "create Board" in{
        controller.createBoard() should be (true)
      }
      "player set" in {
        controller.setPlayer() should be (true)
      }
    }
  }
}
