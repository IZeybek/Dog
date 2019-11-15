package controller

import controller.controllerComponent.Controller
import model.boardComponent.Board
import org.scalatest.{Matchers, WordSpec}

class ControllerSpec extends WordSpec with Matchers {

  "A Controller" when {
    "initialized" should {
      val controller: Controller = new Controller(Array("Player1", "Player2", "Player3", "Player4"))
      "create a board " in {
        val b: Board = Board()
        controller.createBoard should be(b)
      }
      "create a player" in {
        controller.createPlayer(Array("PlayerSpec", "SpecPlayer"))
        "and move a player" in {
          controller.move(controller.player(0), 6, 2) should be(true)
        }
      }
      "print board" in {
        controller.printBoard() should be(controller.board.toString())
      }
    }
  }
}
