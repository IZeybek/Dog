package controller

import model._
import org.scalatest.{Matchers, WordSpec}

class ControllerSpec extends WordSpec with Matchers {

  "A Controller" when {
    "initialized" should {
      val controller: Controller = new Controller()
      "create a board " in {
        controller.createBoard should be(controller.getBoard)
      }
      "print board" in {
        controller.toStringBoard should be(controller.toStringHouse + controller.board.toString())
      }
      "create a player" in {
        val players: Array[Player] = controller.createPlayer(Array("Player1", "Player2", "Player3", "Player4"))
        players(0).toString should be("Player1")
        players(1).toString should be("Player2")
        players(2).toString should be("Player3")
        players(3).toString should be("Player4")
      }
      "set players" in {
        val players: Array[Player] = controller.setPlayer(Array("Player1", "Player2", "Player3", "Player4"))
        players(0).toString should be("Player1")
        players(1).toString should be("Player2")
        players(2).toString should be("Player3")
        players(3).toString should be("Player4")
      }
      "move a player by 4" in {
        controller.setPlayer(Array("Player1", "Player2", "Player3", "Player4"))
        controller.movePlayer(3, 0, 4) should be(-1)
      }
      "move a player by 0" in {
        controller.setPlayer(Array("Player1", "Player2", "Player3", "Player4"))
        controller.movePlayer(3, 0, 4) should be(-1)
      }
      "draw a card" in {
        controller.drawCard.isInstanceOf[CardTrait] should be(true)
      }
    }
  }
}
