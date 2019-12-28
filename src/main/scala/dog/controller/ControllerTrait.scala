package dog.controller

import dog.model.BoardComponent.BoardTrait
import dog.model.CardComponent.CardTrait
import dog.model.Player
import dog.util.UndoManager

import scala.swing.Publisher

trait ControllerTrait extends Publisher {

  val undoManager: UndoManager
  var gameState: GameState
  var gameStateMaster: GameStateMasterTrait

  def doStep(): Unit

  def undoCommand(): Unit

  def redoCommand(): Unit

  /**
   * prints the board and houses
   *
   * @return the board and houses in a String
   */
  def toStringBoard: String

  /**
   * prints the houses of each player
   *
   * @return the houses in a String
   */
  def toStringHouse: String

  def getBoard: BoardTrait

  //Player
  //@TODO: extend method to dynamic playerADD with color algorithm, later... bitches
  def createPlayers(playerNames: List[String]): GameState

  def createCardDeck(amounts: List[Int]): (Vector[CardTrait], Int)

  def toStringCardDeck: String

  def drawCardFromDeck: CardTrait

  def drawFewCards(amount: Int): List[CardTrait]

  def toStringPlayerHands: String

  def testDistributeCardsToPlayer(playerNum: Int, cards: List[CardTrait]): Player

  //Board
  def createNewBoard(size: Int): BoardTrait

  def createNewBoard: BoardTrait

  def toStringActivePlayerHand: String

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
  def useCardLogic(inputCard: InputCard): Int

  def getSelectedCard(playerNum: Int, cardNum: (Int, Int)): CardTrait

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
  def manageRound(inputCard: InputCard): String


}
