package dog.controller.controllerBaseImpl

import dog.controller.Component.controllerBaseImpl.Controller
import dog.controller.{InputCard, InputCardMaster}
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
        controller.createNewBoard(16) should be(controller.board)
        controller.gameState.board should be(controller.board)
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
        val players: Vector[Player] = controller.createPlayers(List("Player1", "Player2", "Player3", "Player4"), 4).players._1
        players(0).toString should be("Player1")
        players(1).toString should be("Player2")
        players(2).toString should be("Player3")
        players(3).toString should be("Player4")
      }
      "manage the round" in {
        controller.createNewBoard(28)
        controller.createPlayers(List("Player1", "Player2", "Player3", "Player4"), 4)
        val cardList: List[CardTrait] = Card("3", "move", "blue") :: Card("5", "move", "blue") :: Nil
        controller.givePlayerCards(playerNum = 0, cardList).cardList should be(cardList)
        controller.manageRound(InputCardMaster.UpdateCardInput()
          .withActualPlayer(0)
          .withOtherPlayer(-1)
          .withPieceNum(List(0, 0))
          .withCardNum((0, 0))
          .withSelectedCard(controller.removeSelectedCard(0, 0))
          .withMoveBy(0)
          .buildCardInput()) should be(s"Player ${controller.gameState.players._1(controller.gameState.players._2).consoleColor}${controller.gameState.players._1(controller.gameState.players._2).nameAndIdx}${Console.RESET}'s turn\n")
      }
      "move a player by 3 and 5" in {
        controller.createNewBoard(28)
        controller.createPlayers(List("Player1", "Player2", "Player3", "Player4"), 4)


        val cardList: List[CardTrait] = Card("3", "move", "blue") :: Card("5", "move", "blue") :: Nil
        controller.givePlayerCards(playerNum = 3, cardList).cardList should be(cardList)
        val inputCard1: InputCard = InputCardMaster.UpdateCardInput()
          .withActualPlayer(3)
          .withOtherPlayer(-1)
          .withPieceNum(List(0))
          .withCardNum((0, 0))
          .withSelectedCard(cardList.head)
          .buildCardInput()
        controller.manageRound(inputCard1)
        controller.gameState.players._1(3).piece(0).pos should be(24)
        controller.gameState.players._1(3).piecePosition(0) should be(24)
        controller.gameState.players._1(3).getPieceNum(24) should be(0)
        controller.gameState.players._1(3).getPieceNum(25) should be(-1)
        controller.gameState.board.cell(24).isFilled should be(true)
        controller.gameState.board.cell(24).getColor should be("red")

        controller.gameState.board.getPieceIndex(24) should be(0)

        val inputCard2: InputCard = InputCardMaster.UpdateCardInput()
          .withActualPlayer(3)
          .withOtherPlayer(0)
          .withPieceNum(List(1))
          .withCardNum((0, 0))
          .withSelectedCard(cardList(1))
          .buildCardInput()

        val newState = controller.useCardLogic(inputCard2)
        newState._3 should be(0)
        controller.manageRound(inputCard2)
        controller.gameState.players._1(3).piece(1).pos should be(26)

      }
      "move a player by 0" in {
        controller.createNewBoard(28)
        controller.createPlayers(List("Player1", "Player2", "Player3", "Player4"), 4)
        val cardList: List[CardTrait] = Card("0", "move", "blue") :: Nil
        controller.givePlayerCards(playerNum = 3, cardList).getCard(0) should be(cardList.head)
        val inputCard1 = InputCardMaster.UpdateCardInput()
          .withActualPlayer(3)
          .withOtherPlayer(-1)
          .withPieceNum(List(0))
          .withCardNum((0, 0))
          .withSelectedCard(cardList.head)
          .buildCardInput()
        controller.manageRound(inputCard1)
        controller.gameState.players._1(3).piece(0).pos should be(21)
      }
      "override a player" in {
        controller.createNewBoard(28)
        controller.createPlayers(List("Player1", "Player2", "Player3", "Player4"), 4)
        val cardList: List[CardTrait] = Card("1", "move", "blue") :: Card("8", "move", "blue") :: Nil

        controller.givePlayerCards(playerNum = 3, cardList).cardList should be(cardList)
        val inputCard1 = InputCardMaster.UpdateCardInput()
          .withActualPlayer(0)
          .withPieceNum(List(0))
          .withCardNum((0, 0))
          .withSelectedCard(cardList.head)
          .buildCardInput()

        val newState = controller.useCardLogic(inputCard1)
        newState._3 should be(0)
        val inputCard2 = InputCardMaster.UpdateCardInput()
          .withActualPlayer(3)
          .withOtherPlayer(0)
          .withPieceNum(List(0))
          .withCardNum((0, 0))
          .withSelectedCard(cardList(1))
          .buildCardInput()

        val newState2 = controller.useCardLogic(inputCard2)
        controller.manageRound(inputCard2)
        newState2._3 should be(0)
        val p1 = controller.gameState.players._1(0)
        val p2 = controller.gameState.players._1(3)
        p1.piece(0).pos should be(0)
        p2.piece(0).pos should be(1)

      }
      "swap two players" in {
        controller.createNewBoard(28) should be(new Board(28))
        controller.createPlayers(List("Player1", "Player2", "Player3", "Player4"), 4).players._1.length should be(4)
        val cardListP2: List[CardTrait] = Card("swap", "swap", "red") :: Nil
        val cardListP3: List[CardTrait] = Card("5", "move", "blue") :: Card("3", "move", "blue") :: Card("6", "move", "blue") :: Nil

        //set cards
        controller.givePlayerCards(playerNum = 2, cardListP2).cardList should be(cardListP2)
        controller.givePlayerCards(playerNum = 3, cardListP3).cardList should be(cardListP3)

        //init input
        val inputCard1 = InputCardMaster.UpdateCardInput()
          .withActualPlayer(3)
          .withPieceNum(List(0))
          .withCardNum((0, 0))
          .withSelectedCard(cardListP3.head)
          .buildCardInput()
        val inputCard2 = InputCardMaster.UpdateCardInput()
          .withActualPlayer(2)
          .withPieceNum(List(2))
          .withCardNum((0, 0))
          .withSelectedCard(cardListP3.head)
          .buildCardInput()
        val inputCard3 = InputCardMaster.UpdateCardInput()
          .withActualPlayer(3)
          .withPieceNum(List(2))
          .withCardNum((0, 0))
          .withSelectedCard(cardListP3(2))
          .buildCardInput()
        //use CardLogic
        controller.manageRound(inputCard1)
        controller.manageRound(inputCard2)
        controller.manageRound(inputCard3)


        controller.gameState.players._1(3).piece(2).pos should be(27)
        controller.gameState.players._1(2).piece(2).pos should be(19)
        val inputCard4 = InputCardMaster.UpdateCardInput()
          .withActualPlayer(2)
          .withOtherPlayer(3)
          .withPieceNum(List(2, 2))
          .withCardNum((0, 0))
          .withSelectedCard(cardListP2.head)
          .buildCardInput()

        println("player 3: piece 2 pos: " + controller.gameState.players._1(3).piece(2).pos)
        println("player 2: piece 2 pos: " + controller.gameState.players._1(2).piece(2).pos)
        val newState = controller.useCardLogic(inputCard4)
        newState._3 should be(0)
        controller.manageRound(inputCard4)
        println("player 3: piece 2 pos: " + controller.gameState.players._1(3).piece(2).pos + " should be 19")
        println("player 2: piece 2 pos: " + controller.gameState.players._1(2).piece(2).pos + " should be 27")
        //check position
        controller.gameState.players._1(3).piece(2).pos should be(19)
        controller.gameState.players._1(2).piece(2).pos should be(27)

        //check position on board
        controller.gameState.board.cell(19).p.get.nameAndIdx._1 should be("Player4")
        controller.gameState.board.cell(27).p.get.nameAndIdx._1 should be("Player3")

        controller.board.cell(19).p.get.nameAndIdx._1 should be("Player4")
        controller.board.cell(27).p.get.nameAndIdx._1 should be("Player3")
      }
      "swap two players when no player is on the field" in {
        controller.createNewBoard(28)
        controller.createPlayers(List("Player1", "Player2", "Player3", "Player4"), 4)
        val cardListP1: List[CardTrait] = Card("swap", "swap", "red") :: Nil

        //set cards
        controller.givePlayerCards(playerNum = 1, cardListP1).cardList should be(cardListP1)
        val inputCard1 = InputCardMaster.UpdateCardInput()
          .withActualPlayer(1)
          .withOtherPlayer(2)
          .withPieceNum(List(2, 3))
          .withCardNum((0, 0))
          .withSelectedCard(cardListP1.head)
          .buildCardInput()
        //use CardLogic

        val newState = controller.useCardLogic(inputCard1)
        newState._3 should be(-1)

        //check if player stays the same
        controller.gameState.players._1(1).piece(2).pos should be(7)
      }
      "play a Card" in {
        controller.createNewBoard(28)
        controller.createPlayers(List("Player1", "Player2", "Player3", "Player4"), 4)
        controller.givePlayerCards(playerNum = 0, List(Card("5", "move", "blue")))
        val handCards: List[CardTrait] = controller.gameState.players._1(0).cardList
        controller.gameState.players._1(0).cardList should not be empty

        controller.removeSelectedCard(0, 0) should be(handCards.head)

        controller.gameState.players._1(0).cardList should be(empty)
      }
      "create a Card Deck" in {
        val cardDeck: (Vector[CardTrait], Int) = controller.createCardDeck(List(0, 0))
        cardDeck._1.length should be(cardDeck._2)
      }
      "draw Cards" in {
        controller.drawCards(10).foreach(x => be(x.isInstanceOf[CardTrait]))
      }
      "draw Card from Deck" in {
        controller.drawCardFromDeck.isInstanceOf[CardTrait] should be(true)
      }
    }
  }
}
