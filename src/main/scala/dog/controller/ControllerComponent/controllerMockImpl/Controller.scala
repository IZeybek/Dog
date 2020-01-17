package dog.controller.ControllerComponent.controllerMockImpl

import dog.controller.ControllerComponent.ControllerTrait
import dog.controller.StateComponent.{GameState, GameStateMaster, GameStateMasterTrait, InputCard}
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

  override def toStringGarage: String = ""
  /**
   * prints the houses of each player
   *
   * @return the houses in a String
   */
  override def toStringHouse: String = ""

  override def createPlayers(playerNames: List[String], pieceAmount: Int): Vector[Player] = Vector.empty[Player]

  override def createCardDeck(amounts: List[Int]): (Vector[CardTrait], Int) = (Vector.empty[CardTrait], 0)

  override def toStringCardDeck: String = ""

  override def drawCardFromDeck: CardTrait = Card("5", "move", "blau")

  override def drawCards(amount: Int): List[CardTrait] = Card("5", "move", "blau") :: Nil

  override def toStringPlayerHands: String = ""

  override def givePlayerCards(playerNum: Int, cards: List[CardTrait]): Player = Player.PlayerBuilder().build()

  override def createNewBoard(size: Int): BoardTrait = new Board(size)

  override def createNewBoard: BoardTrait = new Board(20)

  override def toStringActivePlayerHand: String = ""


  /**
   * uses the card and extracts its logic
   *
   * @param inputCard is a build with all the information needed for its cardLogic
   *                  @ param selectedPlayerList is the list of Players -> first one is the actual player =>
   *                  managed by manageRound but can also be set manually for testing purposes
   *                  @ param pieceNum           is a List of indexes for the pieces of each player for e.g. swapping, only first is used when its about a move
   *                  @ param selectedCard       is the index of the card in a CardList of the player that is played
   * @return
   */
  def useCardLogic(inputCard: InputCard): (BoardTrait, Vector[Player], Int) = (new Board(20), Vector.empty[Player], 0)

  override def removeSelectedCard(playerNum: Int, cardIdx: Int): CardTrait = Card.RandomCardsBuilder().buildRandomCardList.head


  /**
   * Manages the round
   *
   * @param inputCard     is a CardInput builder in order to parse information out of it
   *                      @ otherPlayer   is not -1 when e.g. swapping -> user has to know when to insert more or less commands
   *                      @ pieceNum      is a List of indexes for the pieces of each player for e.g. swapping, only first is used when its about a move
   *                      @ cardNum       is a tuple
   *                    1. is the card number
   *                    2. is which options of the card have to be selected
   *                      e.g. "4" "forward backward" => using parse
   * @return a String that is returned to the TUI for more information
   */
  override def manageRound(inputCard: InputCard): String = ""

  override def selectedField(clickedFieldIdx: Int): Int = 0

  override def save(): Unit = {}

  override def load: String = "Loading successful"

  override def check(inputCard: InputCard, typeChain: String): (Boolean, String) = (true, "")

  override def updateGame(): Unit = {}
}
