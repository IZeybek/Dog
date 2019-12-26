package dog.controller.Component.controllerMockImpl

import dog.controller.{ControllerTrait, GameState, GameStateMaster, GameStateMasterTrait}
import dog.model.BoardComponent.BoardTrait
import dog.model.BoardComponent.boardBaseImpl.Board
import dog.model.CardComponent.CardTrait
import dog.model.CardComponent.cardBaseImpl.Card
import dog.model.Player
import dog.util.{SolveCommand, UndoManager}

class Controller extends ControllerTrait {
  override val undoManager: UndoManager = new UndoManager
  override var gameStateMaster: GameStateMasterTrait = new GameStateMaster()
  override var gameState: GameState = gameStateMaster.UpdateGame().buildGame

  override def doStep(): Unit = undoManager.doStep(new SolveCommand(this))

  override def undoCommand(): Unit = undoManager.undoStep()

  override def redoCommand(): Unit = undoManager.redoStep()

  /**
   * prints the board and houses
   *
   * @return the board and houses in a String
   */
  override def toStringBoard: String = ""

  /**
   * prints the houses of each player
   *
   * @return the houses in a String
   */
  override def toStringHouse: String = ""

  override def getBoard: BoardTrait = new Board(20)

  override def createPlayers(playerNames: List[String]): GameState = gameState

  /**
   * Manages the round
   *
   * @param otherPlayer is not -1 when e.g. swapping -> user has to know when to insert more or less commands
   * @param pieceNum    is a List of indexes for the pieces of each player for e.g. swapping, only first is used when its about a move
   * @param cardNum     is the index of the card in a CardList of the player that is played
   * @return a String that is returned to the TUI for more information
   */
  override def manageRound(otherPlayer: Int, pieceNum: List[Int], cardNum: Int): String = ""

  /**
   * uses the card and extracts its logic
   *
   * @param selectedPlayerList is the list of Players -> first one is the actual player =>
   *                           managed by manageRound but can also be set manually for testing purposes
   * @param pieceNum           is a List of indexes for the pieces of each player for e.g. swapping, only first is used when its about a move
   * @param cardNum            is the index of the card in a CardList of the player that is played
   * @return
   */
  override def useCardLogic(selectedPlayerList: List[Int], pieceNum: List[Int], cardNum: Int): Int = 0

  override def createCardDeck(amounts: List[Int]): (Vector[CardTrait], Int) = (Vector(Card("5", "move", "blau")), 0)

  override def toStringCardDeck: String = ""

  override def getSelectedCard(playerNum: Int, cardNum: Integer): CardTrait = Card("5", "move", "blau")

  override def drawCardFromDeck: CardTrait = Card("5", "move", "blau")

  override def drawFewCards(amount: Int): List[CardTrait] = Card("5", "move", "blau") :: Nil

  override def toStringPlayerHands: String = ""

  override def testDistributeCardsToPlayer(playerNum: Int, cards: List[CardTrait]): Player = gameState.players._1(0)

  override def createNewBoard(size: Int): BoardTrait = new Board(size)

  override def createNewBoard: BoardTrait = new Board(20)

  override def toStringActivePlayerHand: String = ""
}
