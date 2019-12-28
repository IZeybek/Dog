package dog.controller.controllerBaseImpl

import dog.controller.Component.controllerBaseImpl.Controller
import dog.controller.InputCardMaster
import dog.model.BoardComponent.BoardTrait
import dog.model.BoardComponent.boardBaseImpl.Board
import dog.model.CardComponent.CardTrait
import dog.model.CardComponent.cardBaseImpl.Card
import dog.model.Player
import org.scalatest.{Matchers, WordSpec}

class ControllerSpec extends WordSpec with Matchers {

  "A Controller" when {
    "initialized" should {
      val board = new Board(30)
      val controller: Controller = new Controller(board)
      "create a board " in {
        controller.createNewBoard(16) should be(controller.getBoard)
        controller.gameState.board should be(controller.getBoard)
      }
      "create a random Board" in {
        controller.createRandomBoard(10)
        val board: BoardTrait = controller.gameState.board
        controller.toStringBoard should be(controller.toStringHouse + board.toString)
      }
      "print board" in {
        val board: BoardTrait = controller.gameState.board
        controller.toStringBoard should be(controller.toStringHouse + board.toString)
      }
      "create a player" in {
        val players: Vector[Player] = controller.createPlayers(List("Player1", "Player2", "Player3", "Player4")).players._1
        players(0).toString should be("Player1")
        players(1).toString should be("Player2")
        players(2).toString should be("Player3")
        players(3).toString should be("Player4")
      }
      "set players" in {
        val players: Vector[Player] = controller.createPlayers(List("Player1", "Player2", "Player3", "Player4")).players._1
        players(0).toString should be("Player1")
        players(1).toString should be("Player2")
        players(2).toString should be("Player3")
        players(3).toString should be("Player4")
      }
      "manage the round" in {
        controller.createPlayers(List("Player1", "Player2", "Player3", "Player4"))
        controller.createNewBoard(20)
        val cardList: List[CardTrait] = Card("3", "move", "blue") :: Card("5", "move", "blue") :: Nil
        controller.testDistributeCardsToPlayer(playerNum = 0, cardList).cardList should be(cardList)
        controller.manageRound(InputCardMaster.UpdateCardInput()
          .withOtherPlayer(0)
          .withPieceNum(List(0, 0))
          .withCardNum((0, 0))
          .withSelectedPlayerList(List(0))
          .withSelectedCard(controller.getSelectedCard(0, (0, 0)))
          .withMoveBy(0)
          .buildCardInput()) should be(s"Player ${controller.gameState.players._1(controller.gameState.players._2).consoleColor}${controller.gameState.players._1(controller.gameState.players._2).name}${Console.RESET}'s turn\n")
      }
      "move a player by 5" in {
        controller.createPlayers(List("Player1", "Player2", "Player3", "Player4"))
        controller.createNewBoard(20)
        val cardList: List[CardTrait] = Card("3", "move", "blue") :: Card("5", "move", "blue") :: Nil
        controller.testDistributeCardsToPlayer(playerNum = 3, cardList).cardList should be(cardList)
        val inputCard1 = InputCardMaster.UpdateCardInput()
          .withOtherPlayer(-1)
          .withPieceNum(List(0))
          .withCardNum((0, 0))
          .withSelectedPlayerList(List(3))
          .withSelectedCard(cardList.head)
          .buildCardInput()
        controller.useCardLogic(inputCard1) should be(0)
        controller.gameState.players._1(3).piece(0).position should be(3)
        val inputCard2 = InputCardMaster.UpdateCardInput()
          .withOtherPlayer(0)
          .withPieceNum(List(1))
          .withCardNum((0, 0))
          .withSelectedPlayerList(List(3))
          .withSelectedCard(cardList(1))
          .buildCardInput()
        controller.useCardLogic(inputCard2) should be(0)
        controller.gameState.players._1(3).piece(1).position should be(5)

      }
      "move a player by 0" in {
        controller.createPlayers(List("Player1", "Player2", "Player3", "Player4"))
        controller.createNewBoard(20)
        val cardList: List[CardTrait] = Card("0", "move", "blue") :: Nil
        controller.testDistributeCardsToPlayer(playerNum = 3, cardList).getCard(0) should be(cardList.head)
        val inputCard1 = InputCardMaster.UpdateCardInput()
          .withOtherPlayer(0)
          .withPieceNum(List(0))
          .withCardNum((0, 0))
          .withSelectedPlayerList(List(3))
          .withSelectedCard(cardList.head)
          .buildCardInput()
        controller.useCardLogic(inputCard1) should be(0)
        controller.gameState.players._1(3).piece(0).position should be(0)
      }
      "override a player" in {
        controller.createPlayers(List("Player1", "Player2", "Player3", "Player4"))
        controller.createNewBoard(20)
        val cardList: List[CardTrait] = Card("5", "move", "blue") :: Card("5", "move", "blue") :: Nil
        var p: Player = controller.gameState.players._1(3)
        controller.testDistributeCardsToPlayer(playerNum = 3, cardList)
        val inputCard1 = InputCardMaster.UpdateCardInput()
          .withOtherPlayer(0)
          .withPieceNum(List(0))
          .withCardNum((0, 0))
          .withSelectedPlayerList(List(3))
          .withSelectedCard(cardList.head)
          .buildCardInput()
        controller.useCardLogic(inputCard1) should be(0)
        val inputCard2 = InputCardMaster.UpdateCardInput()
          .withOtherPlayer(0)
          .withPieceNum(List(1))
          .withCardNum((0, 0))
          .withSelectedPlayerList(List(3))
          .withSelectedCard(cardList(1))
          .buildCardInput()
        controller.useCardLogic(inputCard2) should be(0)
        p = controller.gameState.players._1(3)

        p.getPosition(0) should be(5)
        p.getPosition(1) should be(5)

      }
      "swap two players" in {
        controller.createPlayers(List("Player1", "Player2", "Player3", "Player4"))
        controller.createNewBoard(20)
        val cardListP2: List[CardTrait] = Card("swap", "swap", "red") :: Nil
        val cardListP3: List[CardTrait] = Card("5", "move", "blue") :: Card("3", "move", "blue") :: Card("9", "move", "blue") :: Nil

        //set cards
        controller.testDistributeCardsToPlayer(playerNum = 2, cardListP2).cardList should be(cardListP2)
        controller.testDistributeCardsToPlayer(playerNum = 3, cardListP3).cardList should be(cardListP3)

        //init input
        val inputCard1 = InputCardMaster.UpdateCardInput()
          .withOtherPlayer(0)
          .withPieceNum(List(0))
          .withCardNum((0, 0))
          .withSelectedPlayerList(List(3))
          .withSelectedCard(cardListP3.head)
          .buildCardInput()
        val inputCard2 = InputCardMaster.UpdateCardInput()
          .withOtherPlayer(0)
          .withPieceNum(List(1))
          .withCardNum((0, 0))
          .withSelectedPlayerList(List(3))
          .withSelectedCard(cardListP3(1))
          .buildCardInput()
        val inputCard3 = InputCardMaster.UpdateCardInput()
          .withOtherPlayer(0)
          .withPieceNum(List(2))
          .withCardNum((0, 0))
          .withSelectedPlayerList(List(3))
          .withSelectedCard(cardListP3(2))
          .buildCardInput()
        //use CardLogic
        controller.useCardLogic(inputCard1) should be(0)
        controller.useCardLogic(inputCard2) should be(0)
        controller.useCardLogic(inputCard3) should be(0)

        controller.gameState.players._1(3).getPosition(2) should be(9)
        val inputCard4 = InputCardMaster.UpdateCardInput()
          .withOtherPlayer(0)
          .withPieceNum(List(2, 2))
          .withCardNum((0, 0))
          .withSelectedPlayerList(List(2, 3))
          .withSelectedCard(cardListP2.head)
          .buildCardInput()
        controller.useCardLogic(inputCard4) should be(0)

        //check position
        controller.gameState.players._1(3).getPosition(2) should be(0)
        controller.gameState.players._1(2).getPosition(2) should be(9)

        //check position on board
        controller.gameState.board.cell(9).p.get.name should be("Player3")
        controller.gameState.board.cell(0).p.get.name should be("Player4")

        controller.board.cell(9).p.get.name should be("Player3")
        controller.board.cell(0).p.get.name should be("Player4")
      }
      "swap two players when no player is on the field" in {
        controller.createPlayers(List("Player1", "Player2", "Player3", "Player4"))
        controller.createNewBoard(20)
        val cardListP1: List[CardTrait] = Card("swap", "swap", "red") :: Nil

        //set cards
        controller.testDistributeCardsToPlayer(playerNum = 1, cardListP1).cardList should be(cardListP1)
        val inputCard1 = InputCardMaster.UpdateCardInput()
          .withOtherPlayer(0)
          .withPieceNum(List(2, 3))
          .withCardNum((0, 0))
          .withSelectedPlayerList(List(1, 2))
          .withSelectedCard(cardListP1.head)
          .buildCardInput()
        //use CardLogic
        controller.useCardLogic(inputCard1) should be(-1)

        //check if player stays the same
        controller.gameState.players._1(1).getPosition(2) should be(0)
      }
      "play a Card" in {
        controller.createPlayers(List("Player1", "Player2", "Player3", "Player4"))
        controller.createNewBoard(20)
        controller.testDistributeCardsToPlayer(playerNum = 0, List(Card("5", "move", "blue")))
        val handCards: List[CardTrait] = controller.gameState.players._1(0).cardList
        controller.gameState.players._1(0).cardList should not be empty

        controller.getSelectedCard(0, cardAndOption = (0, 0)) should be(handCards.head)

        controller.gameState.players._1(0).cardList should be(empty)
      }
      "create a Card Deck" in {
        val cardDeck: (Vector[CardTrait], Int) = controller.createCardDeck(List(0, 0))
        cardDeck._1.length should be(cardDeck._2)
      }
      "draw Cards" in {
        controller.drawFewCards(10).foreach(x => be(x.isInstanceOf[CardTrait]))
      }
      "draw Card from Deck" in {
        controller.drawCardFromDeck.isInstanceOf[CardTrait] should be(true)
      }
    }
  }
}
