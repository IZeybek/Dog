package controller

import controller.controllerComponent.Controller
import org.scalatest.{Matchers, WordSpec}

class ControllerSpec extends WordSpec with Matchers {

  "A Controller" when {
    "initialized" should {
      val controller: Controller = new Controller(Array("Player1", "Player2", "Player3", "Player4"))
      "create a board " in {
        controller.setNewBoard should be(controller.getBoard)
      }
      "create a player" in {
       val players =  controller.createPlayer(Array("Player1","Player2","Player3","Player4"))
        players(0).toString should be("Player1")
        players(1).toString should be("Player2")
        players(2).toString should be("Player3")
        players(3).toString should be("Player4")
      }
      "print board" in {
        controller.printBoard() should be(controller.board.toString())
      }
    }
  }
}
