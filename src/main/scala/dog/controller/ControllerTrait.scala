package dog.controller

import dog.model.BoardComponent.BoardTrait
import dog.model.CardComponent.CardTrait
import dog.model.Player
import dog.util.UndoManager

import scala.swing.Publisher

trait ControllerTrait extends Publisher {
  def clickedField(clickedFieldIdx: Int): Int


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

  //Player
  //@TODO: extend method to dynamic playerADD with color algorithm, later... bitches
  def createPlayers(playerNames: List[String], pieceAmount: Int): GameState

  def createCardDeck(amounts: List[Int]): (Vector[CardTrait], Int)

  def toStringCardDeck: String

  def drawCardFromDeck: CardTrait

  def drawCards(amount: Int): List[CardTrait]

  def toStringPlayerHands: String

  def givePlayerCards(playerNum: Int, cards: List[CardTrait]): Player

  //Board
  def createNewBoard(size: Int): BoardTrait

  def createNewBoard: BoardTrait

  def toStringActivePlayerHand: String

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

  def useCardLogic(inputCard: InputCard): (BoardTrait, Vector[Player], Int) 
}
