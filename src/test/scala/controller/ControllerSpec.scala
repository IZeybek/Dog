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
      "move a player by 5" in {
        controller.createSetPlayer(List("Player1", "Player2", "Player3", "Player4"))
        val cardList: List[Card] = Card("5", "move", "blue") :: Nil
        controller.setHandCards(playerNum = 3, cardList)
        controller.useCardLogic(playerNum = List(3), pieceNum = 0, cardNum = 0).getPosition(0) should be(5)
      }
      "move a player by 0" in {
        controller.createSetPlayer(List("Player1", "Player2", "Player3", "Player4"))
        val cardList: List[Card] = Card("0", "move", "blue") :: Nil
        controller.setHandCards(playerNum = 3, cardList)
        controller.useCardLogic(playerNum = List(3), pieceNum = 0, cardNum = 0).getPosition(0) should be(0)
      }
      "swap two players" in {
        controller.createSetPlayer(List("Player1", "Player2", "Player3", "Player4"))
        val cardList: List[Card] = Card("5", "move", "blue") :: Card("swap", "swap", "red") :: Nil
        controller.setHandCards(playerNum = 3, List(cardList(0)))
        controller.setHandCards(playerNum = 2, List(cardList(1)))
        controller.useCardLogic(playerNum = List(3), pieceNum = 0, cardNum = 0)
        controller.useCardLogic(playerNum = List(2, 3), pieceNum = 2, cardNum = 0)
      }
      "play a Card" in {
        controller.createSetPlayer(List("Player1", "Player2", "Player3", "Player4"))
        controller.setHandCards(playerNum = 0, List(Card("5", "move", "blue")))
        val handCards: List[Card] = controller.player(0).cardList
        val playedCard: Card = controller.playCard(0, 0)
        playedCard.color should be(handCards(0).color)
      }
      "draw Cards" in {
        controller.drawFewCards(10).foreach(x => be(x.isInstanceOf[Card]))
      }
    }
  }
}
