package controller

import controller.controllerComponent.Controller
import org.scalatest.{Matchers, WordSpec}

class ControllerSpec extends WordSpec with Matchers {

  "A Controller" when {
    "created" should {
      val controller: ControllerTrait = new Controller()
      "print Board" when {
        "Board is created" in {
          controller.createBoard(4)

        }
      }
      "create Board" in {
        controller.createBoard(4)
      }
    }
  }
}
