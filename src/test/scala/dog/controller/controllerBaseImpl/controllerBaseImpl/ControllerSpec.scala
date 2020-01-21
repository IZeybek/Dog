package dog.controller.controllerBaseImpl.controllerBaseImpl

import dog.controller.ControllerComponent.controllerBaseImpl.Controller
import dog.controller.StateComponent.{GameState, InputCard, InputCardMaster}
import dog.model.BoardComponent.BoardTrait
import dog.model.BoardComponent.boardBaseImpl.{Board, Cell}
import dog.model.CardComponent.CardTrait
import dog.model.CardComponent.cardBaseImpl.Card
import dog.model.{Piece, Player, PlayerBuilder}
import dog.util.SelectedState
import org.scalatest.{Matchers, WordSpec}

class ControllerSpec extends WordSpec with Matchers {

  "A Controller" when {
    "initialized" should {
      val controller: Controller = new Controller(new Board(30))
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
        val players: Vector[Player] = controller.createPlayers(List("Player1", "Player2", "Player3", "Player4"), 4, 6)
        println(players)
        players(0).toString should be("Player1")
        players(1).toString should be("Player2")
        players(2).toString should be("Player3")
        players(3).toString should be("Player4")
      }
      "manage the round" in {
        controller.gameStateMaster.UpdateGame().resetGame
        controller.createNewBoard(28)
        val cardList: List[CardTrait] = Card("1 11 play", "move move play", "red") :: Card("3", "move", "blue") :: Card("5", "move", "blue") :: Nil
        val player: Player = new PlayerBuilder().Builder().withPieces(Map(0 -> Piece(1), 1 -> Piece(0)), 0).withCards(cardList).build()
        val gameState: GameState = controller.gameStateMaster.UpdateGame().withPlayers(Vector(player)).withActualPlayer(0).withBoard(new Board(20).fill(Cell(Some(player)), 1)).buildGame
        controller.updateGame()
        gameState.actualPlayer.cardList.head should be(cardList.head)

        controller.selectedField(1)
        // First Round - Play a Card
        controller.manageRound(InputCardMaster.UpdateCardInput()
          .withCardNum((0, 2))
          .withSelectedCard(cardList.head)
          .buildCardInput()) should be(s"Move was not possible! Please retry again;)\n")

        controller.gameState.players._2 should be(0)
        controller.gameState.actualPlayer.piecePosition(0) should be(1)

        controller.gameState.actualPlayer.cardList.head should be(cardList(0))

        // Second Round - 3
        controller.selectedField(1)

        controller.manageRound(InputCardMaster.UpdateCardInput()

          .withOtherPlayer(-1)
          .withPieceNum(List(0, 0))
          .withCardNum((0, 0))
          .withMoveBy(3)
          .buildCardInput()) should be(s"${controller.gameState.actualPlayer.toString}'s turn\n")

        controller.gameState.players._2 should be(0)
        //        controller.gameState.actualPlayer.piecePosition(0) should be(4)

        //        controller.gameState.actualPlayer.cardList.head should be(cardList(2))

        //        // Third Round - 3
        //        controller.selectedField(4)
        //
        //        controller.manageRound(InputCardMaster.UpdateCardInput()
        //
        //          .withOtherPlayer(-1)
        //          .withPieceNum(List(0, 0))
        //          .withCardNum((0, 0))
        //          .withMoveBy(5)
        //          .buildCardInput()) should be(s"${controller.gameState.actualPlayer.toString}'s turn\n")
        //
        //        controller.gameState.players._2 should be(0)
        //        controller.gameState.actualPlayer.piecePosition(0) should be(9)
        //
        //        controller.gameState.actualPlayer.cardList.isEmpty should be(false)
        //        controller.gameState.actualPlayer.cardList.length should be(5)
      }
      "move a player by 3 and 5" in {
        controller.gameStateMaster.UpdateGame().resetGame
        controller.createNewBoard(28)
        val cardList: List[CardTrait] = Card("3", "move", "blue") :: Card("5", "move", "blue") :: Card("1 11 play", "move move play", "red") :: Nil
        val player: Player = new PlayerBuilder().Builder().withPieces(Map(0 -> Piece(1)), 0).withCards(cardList).build()
        val gameState: GameState = controller.gameStateMaster.UpdateGame().withPlayers(Vector(player)).withBoard(new Board(20).fill(Cell(Some(player)), 1)).buildGame
        controller.updateGame()
        controller.gameState.players._1(0).piece(0).pos should be(1)
        gameState.actualPlayer.piece(0).pos should be(1)
        gameState.board.cell(1).isFilled should be(true)

        val inputCard1: InputCard = InputCardMaster.UpdateCardInput()

          .withOtherPlayer(-1)
          .withPieceNum(List(0))
          .withCardNum((0, 0))
          .withSelectedCard(cardList.head)
          .buildCardInput()
        controller.selectedField(1)

        controller.manageRound(inputCard1)
        controller.gameState.players._1(0).piece(0).pos should be(4)
        controller.gameState.players._1(0).piecePosition(0) should be(4)
        controller.gameState.players._1(0).getPieceNum(4) should be(0)
        controller.gameState.players._1(0).getPieceNum(5) should be(-1)
        controller.gameState.board.cell(4).isFilled should be(true)
        controller.gameState.board.cell(4).getColor should be("blue")

        controller.gameState.board.getPieceIndex(4) should be(0)

        val inputCard2: InputCard = InputCardMaster.UpdateCardInput()

          .withOtherPlayer(0)
          .withPieceNum(List(0))
          .withCardNum((0, 0))
          .withSelectedCard(cardList(1))
          .buildCardInput()

        controller.selectedField(4)

        val newState = controller.useCardLogic(inputCard2)
        newState._3 should be(0)
        controller.manageRound(inputCard2)
        controller.gameState.players._1(0).piece(0).pos should be(9)

      }
      "move a player by 0" in {
        controller.createNewBoard(28)
        controller.createPlayers(List("Player1", "Player2", "Player3", "Player4"), 4, 6)
        val cardList: List[CardTrait] = Card("0", "move", "blue") :: Nil
        controller.givePlayerCards(playerNum = 3, cardList).getCard(0) should be(cardList.head)
        val inputCard1 = InputCardMaster.UpdateCardInput()
          .withOtherPlayer(-1)
          .withPieceNum(List(0))
          .withCardNum((0, 0))
          .withSelectedCard(cardList.head)
          .buildCardInput()
        controller.manageRound(inputCard1)
        controller.gameState.players._1(3).piece(0).pos should be(21)
      }
      "override a player" in {
        val cardList1: List[CardTrait] = Card("8", "move", "blue") :: Nil
        val cardList2: List[CardTrait] = Card("1 11 play", "move move play", "red") :: Nil
        val player1: Player = new PlayerBuilder().Builder().withName(("Charlie", 0)).withPieces(Map(0 -> Piece(1)), 0).withCards(cardList1).build()
        val player2: Player = new PlayerBuilder().Builder().withName(("Anne", 1)).withPieces(Map(0 -> Piece(9)), 0).withCards(cardList2).build()
        val board: BoardTrait = new Board(20).fill(Cell(Some(player1)), 1).fill(Cell(Some(player2)), 9)

        board.cell(1).checkIfPlayer(player1) should be(true)
        board.cell(1).checkIfPlayer(player2) should be(false)
        board.cell(9).checkIfPlayer(player2) should be(true)
        board.cell(9).checkIfPlayer(player1) should be(false)

        controller.gameStateMaster.UpdateGame().withPlayers(Vector(player1, player2)).withActualPlayer(0).withBoard(board).buildGame
        controller.updateGame()

        val inputCard: InputCard = InputCardMaster.UpdateCardInput()
          .withOtherPlayer(-1)
          .withPieceNum(List(0))
          .withCardNum((0, 0))
          .withMoveBy(8)
          .withSelectedCard(cardList1.head)
          .buildCardInput()

        controller.selectedField(1)

        println(controller.manageRound(inputCard))
        println(controller.toStringBoard)
        val gameState: GameState = controller.gameState
        val (boardTrait, players) = (gameState.board, gameState.players._1)

        println(player1.piecePosition(0))
        println(boardTrait.cell(9).p.get)
        println(player1)
        boardTrait.cell(9).checkIfPlayer(player1) should be(true)
        gameState.players._2 should be(1)
        gameState.players._1(0).piece(0).pos should be(9)
        gameState.players._1(0).piecePosition(0) should be(9)
        gameState.actualPlayer.piecePosition(0) should be(0)
        players.head.piecePosition(0) should be(9)
        boardTrait.cell(9).isFilled should be(true)
        boardTrait.cell(1).isFilled should be(false)
      }
      "swap two players" in {
        val cardList1: List[CardTrait] = Card("swap", "swap", "red") :: Card("", "", "") :: Nil
        val cardList2: List[CardTrait] = Card("1 11 play", "move move play", "red") :: Nil
        val player1: Player = new PlayerBuilder().Builder().withName(("Charlie", 0)).withPieces(Map(2 -> Piece(2)), 0).withCards(cardList1).build()
        val player2: Player = new PlayerBuilder().Builder().withName(("Anne", 1)).withPieces(Map(3 -> Piece(10)), 0).withCards(cardList2).build()
        val board: BoardTrait = new Board(20).fill(Cell(Some(player1)), 2).fill(Cell(Some(player2)), 10)

        controller.gameStateMaster.UpdateGame().withPlayers(Vector(player1, player2)).withActualPlayer(0).withBoard(board).buildGame
        controller.updateGame()

        //set cards
        controller.givePlayerCards(playerNum = 0, cardList1).cardList should be(cardList1)
        controller.gameState.actualPlayer.cardList.size should be(2)
        controller.selectedField(2)
        controller.selectedField(10)

        //init input
        val inputCard = InputCardMaster.UpdateCardInput()

          .withPieceNum(List(2, 3))
          .withCardNum((0, 0))
          .withSelectedCard(controller.gameState.actualPlayer.cardList.head)
          .buildCardInput()
        println(controller.manageRound(inputCard))
        println(controller.gameState.players._1(0).cardList)
        controller.gameState.players._1(0).cardList.size should be(1) //chain checks only before player1 has to play but after player2 played
        println(controller.toStringBoard)

        //use CardLogic

        controller.gameState.players._1(0).piece(2).pos should be(10)
        controller.gameState.players._1(1).piece(3).pos should be(2)
      }
      "swap two players when no player is on the field" in {
        controller.createNewBoard(28)
        controller.createPlayers(List("Player1", "Player2", "Player3", "Player4"), 4, 6)
        val cardListP1: List[CardTrait] = Card("swap", "swap", "red") :: Nil

        //set cards
        controller.givePlayerCards(playerNum = 1, cardListP1).cardList should be(cardListP1)
        val inputCard1 = InputCardMaster.UpdateCardInput()
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
      "Joker pressed and chosen card" in {
        val cardList1: List[CardTrait] = Card("questionmark", "joker", "red") :: Nil
        val player1: Player = new PlayerBuilder().Builder().withName(("Charlie", 0)).withPieces(Map(2 -> Piece(2)), 0).withCards(cardList1).build()
        val board: BoardTrait = new Board(30).fill(Cell(Some(player1)), 2)

        controller.gameStateMaster.UpdateGame().withBoard(board).withActualPlayer(0).withPlayers(Vector(player1)).buildGame
        controller.updateGame()
        println(controller.toStringBoard)

        val handCards: List[CardTrait] = controller.gameState.actualPlayer.cardList
        handCards should not be empty
        handCards.size should be(1)
        board.cell(2).isFilled should be(true)

        //------------------------------------------------------------------------- Joker unpacked

        SelectedState.reset
        controller.selectedField(2)

        val inputCard1 = InputCardMaster.UpdateCardInput()
          .withPieceNum(List(2))
          .withCardNum((0, 0))
          .withSelectedCard(controller.gameState.players._1(0).getCard(0))
          .buildCardInput()

        controller.manageRound(inputCard1)
        controller.gameStateMaster.UpdateGame().buildGame
        controller.updateGame()

        controller.gameState.players._1(0).piece(2).pos should be(2)
        controller.gameState.players._1(0).cardList.size should be(13)
        println(controller.gameState.actualPlayer.cardList)

        //------------------------------------------------------------------------- Joker packed

        val inputCard2@InputCard(otherPlayer, selPieceList, cardIdxAndOption, selectedCard, moveBy) = InputCardMaster.UpdateCardInput()
          .withPieceNum(List(2))
          .withCardNum((6, 0))
          .withSelectedCard(controller.gameState.actualPlayer.getCard(6))
          .buildCardInput()

        otherPlayer should be(-1)
        selPieceList should be(List(2))
        cardIdxAndOption should be((6, 0))
        selectedCard should be(controller.gameState.players._1(0).getCard(6))
        print(controller.toStringBoard)

        println(controller.manageRound(inputCard2))
        print(controller.toStringBoard)

        controller.gameState.board.cell(2).isFilled should be(false)
        controller.gameState.board.cell(7).isFilled should be(true)
        controller.gameState.board.cell(7).checkIfPlayer(player1) should be(true)
        controller.gameStateMaster.UpdateGame().buildGame
        controller.updateGame()

        println(controller.board.cell(7).getPlayerIdx)
        println(controller.gameState.actualPlayer.piecePosition(2))
        //-------------------------------------------------------------------------

        //trying other card -4 move with joker


        controller.givePlayerCards(playerNum = 0, List(Card("questionmark", "joker", "red")))
        controller.gameState.players._1(0).cardList should not be empty
        controller.gameState.players._1(0).cardList.size should be(1)

        val inputCard3 = InputCardMaster.UpdateCardInput()
          .withPieceNum(List(2))
          .withCardNum((0, 0))
          .withSelectedCard(controller.gameState.players._1(0).getCard(0))
          .buildCardInput()

        println(controller.manageRound(inputCard3))

        controller.selectedField(7)

        val inputCard4 = InputCardMaster.UpdateCardInput()
          .withPieceNum(List(2))
          .withCardNum((1, 0))
          .withSelectedCard(controller.gameState.players._1(0).getCard(1))
          .buildCardInput()

        //test CardLogic then use it

        println(controller.manageRound(inputCard4))


        controller.gameState.players._1(0).piece(2).pos should be(3)
      }
      "print the player hand cards" in {
        controller.createNewBoard(20)
        controller.gameStateMaster.UpdateGame().resetGame
        controller.updateGame()
        controller.toStringActivePlayerHand should be(s"${Console.YELLOW}Player 1${Console.RESET}'s hand cards: " + controller.gameState.actualPlayer.cardList + "\n")
      }
      "play a Card" in {
        controller.createNewBoard(28)
        controller.createPlayers(List("Player1", "Player2", "Player3", "Player4"), 4, 6)
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
      //      "draw Card from Deck" in {
      //        controller.drawCardFromDeck.isInstanceOf[CardTrait] should be(true)
      //      }
    }
  }
}
