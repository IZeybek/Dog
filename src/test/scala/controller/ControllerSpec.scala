package controller


import model.CardComponent.Card
import model._
import org.scalatest.{Matchers, WordSpec}

class ControllerSpec extends WordSpec with Matchers {

  "A Controller" when {
    "initialized" should {
      val controller: Controller = new Controller(new Board(30))
      "create a board " in {
        controller.createNewBoard(16) should be(controller.getBoard)
      }
      "create a random Board" in {
        controller.createRandomBoard(10)
        controller.toStringBoard should be(controller.toStringHouse + controller.board.toString())
      }
      "print board" in {
        controller.toStringBoard should be(controller.toStringHouse + controller.board.toString())
      }
      "create a player" in {
        val players: Array[Player] = controller.createPlayers(List("Player1", "Player2", "Player3", "Player4"))
        players(0).toString should be("Player1")
        players(1).toString should be("Player2")
        players(2).toString should be("Player3")
        players(3).toString should be("Player4")
      }
      "set players" in {
        val players: Array[Player] = controller.createPlayers(List("Player1", "Player2", "Player3", "Player4"))
        players(0).toString should be("Player1")
        players(1).toString should be("Player2")
        players(2).toString should be("Player3")
        players(3).toString should be("Player4")
      }
      "move a player by 5" in {
        controller.createPlayers(List("Player1", "Player2", "Player3", "Player4"))
        controller.createNewBoard(20)
        val cardList: List[Card] = Card("5", "move", "blue") :: Nil
        controller.distributeCardsToPlayer(pIdx = 3, cardList)
        controller.useCardLogic(selectedPlayerList = List(3), pieceNum = List(0), cardNum = 0).getPosition(0) should be(5)
      }
      "move a player by 0" in {
        controller.createPlayers(List("Player1", "Player2", "Player3", "Player4"))
        controller.createNewBoard(20)
        val cardList: List[Card] = Card("0", "move", "blue") :: Nil
        controller.distributeCardsToPlayer(pIdx = 3, cardList)
        controller.useCardLogic(selectedPlayerList = List(3), pieceNum = List(0), cardNum = 0).getPosition(0) should be(0)
      }
      "override a player" in {
        controller.createPlayers(List("Player1", "Player2", "Player3", "Player4"))
        controller.createNewBoard(20)
        val cardList: List[Card] = Card("5", "move", "blue") :: Card("5", "move", "blue") :: Nil
        var p: Player = controller.player(3)
        controller.distributeCardsToPlayer(pIdx = 3, cardList)

        p = controller.useCardLogic(selectedPlayerList = List(3), pieceNum = List(0), cardNum = 0)
        p.getPosition(0) should be(5)

        p = controller.useCardLogic(selectedPlayerList = List(3), pieceNum = List(1), cardNum = 0)
        p.getPosition(0) should be(0)
        p.getPosition(1) should be(5)

      }
      "swap two players" in {
        controller.createPlayers(List("Player1", "Player2", "Player3", "Player4"))
        controller.createNewBoard(20)
        val cardListP3: List[Card] = Card("5", "move", "blue") :: Card("3", "move", "blue") :: Card("9", "move", "blue") :: Nil
        val cardListP2: List[Card] = Card("swap", "swap", "red") :: Nil

        //set cards
        controller.distributeCardsToPlayer(pIdx = 3, cardListP3)
        controller.distributeCardsToPlayer(pIdx = 2, cardListP2)

        //use CardLogic
        controller.useCardLogic(selectedPlayerList = List(3), pieceNum = List(0), cardNum = 0)
        controller.useCardLogic(selectedPlayerList = List(3), pieceNum = List(1), cardNum = 0)
        controller.useCardLogic(selectedPlayerList = List(3), pieceNum = List(2), cardNum = 0)
        controller.useCardLogic(selectedPlayerList = List(2, 3), pieceNum = List(2, 2), cardNum = 0)

        //check position
        controller.player(2).getPosition(2) should be(9)
        controller.player(3).getPosition(2) should be(0)
      }
      "swap two players when no player is on the field" in {
        controller.createPlayers(List("Player1", "Player2", "Player3", "Player4"))
        controller.createNewBoard(20)
        val cardListP1: List[Card] = Card("swap", "swap", "red") :: Nil

        //set cards
        controller.distributeCardsToPlayer(pIdx = 1, cardListP1)

        //use CardLogic
        controller.useCardLogic(selectedPlayerList = List(1, 2), pieceNum = List(2, 3), cardNum = 0)

        //check if player stays the same
        controller.player(1).getPosition(2) should be(0)
        controller.player(1).inHouse should be(4)
      }
      "play a Card" in {
        controller.createPlayers(List("Player1", "Player2", "Player3", "Player4"))
        controller.distributeCardsToPlayer(pIdx = 0, List(Card("5", "move", "blue")))
        val handCards: List[Card] = controller.player(0).cardList
        val playedCard: Card = controller.getSelectedCard(0, 0)
        playedCard.color should be(handCards.head.color)
      }
      "draw Cards" in {
        controller.drawFewCards(10).foreach(x => be(x.isInstanceOf[Card]))
      }
    }
  }
}
