package controller


import model.CardComponent.Card
import model._
import org.scalatest.{Matchers, WordSpec}

class ControllerSpec extends WordSpec with Matchers {

  "A Controller" when {
    "initialized" should {
      val controller: Controller = new Controller()
      "create a board " in {
        controller.setNewBoard(16) should be(controller.getBoard)
      }
      "create a random Board" in {
        controller.createRandomBoard(10)
        controller.toStringBoard should be(controller.toStringHouse + controller.board.toString())
      }
      "print board" in {
        controller.toStringBoard should be(controller.toStringHouse + controller.board.toString())
      }
      "create a player" in {
        val players: Array[Player] = controller.createSetPlayer(List("Player1", "Player2", "Player3", "Player4"))
        players(0).toString should be("Player1")
        players(1).toString should be("Player2")
        players(2).toString should be("Player3")
        players(3).toString should be("Player4")
      }
      "set players" in {
        val players: Array[Player] = controller.createSetPlayer(List("Player1", "Player2", "Player3", "Player4"))
        players(0).toString should be("Player1")
        players(1).toString should be("Player2")
        players(2).toString should be("Player3")
        players(3).toString should be("Player4")
      }
      "move a player by 4" in {
        controller.createSetPlayer(List("Player1", "Player2", "Player3", "Player4"))
        controller.movePlayer(3, 0, 4).getPosition(0) should be(4)
      }
      "move a player by 0" in {
        controller.createSetPlayer(List("Player1", "Player2", "Player3", "Player4"))
        controller.movePlayer(3, 0, 0).getPosition(0) should be(0)
      }
      "play a Card" in {
        controller.createSetPlayer(List("Player1", "Player2", "Player3", "Player4"))
        controller.initPlayerHandCards(10)
        controller.playCard(0, 0).color should be(controller.player(0).cardList(0).color)
      }
      "draw Cards" in {
        controller.drawFewCards(10).foreach(x => be(x.isInstanceOf[Card]))
      }
    }
  }
}
