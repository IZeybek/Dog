package controller

import model.CardComponent.{Card, CardDeck, CardLogic}
import model._
import util.{Observable, SolveCommand, UndoManager}

import scala.util.Random

class Controller() extends Observable {


  private val undoManager = new UndoManager

  def doStep(): Unit = {
    undoManager.doStep(new SolveCommand(this))
  }

  def undoCommand(): Unit = {
    undoManager.undoStep
    notifyObservers
  }

  def redoCommand(): Unit = {
    undoManager.redoStep
    notifyObservers
  }

  def replaceController(mementoBoard: Board, mementoPlayer: Vector[Player], mementoCardDeck: (Vector[Card], Int)): Boolean = {
    true
  }

  var gameStateMaster = new GameStateMaster
  var gameState: GameState = gameStateMaster.UpdateGame().buildGame
  initAndDistributeCardsToPlayer(6)

  //Board
  def createNewBoard(size: Int): Board = {
    val board = new Board(size)
    gameState = gameStateMaster.UpdateGame().withBoard(board).buildGame
    notifyObservers
    board
  }

  def createRandomBoard(size: Int): Board = {
    val board = new BoardCreateStrategyRandom().createNewBoard(size)
    notifyObservers
    gameState = gameStateMaster.UpdateGame().withBoard(board).buildGame
    board
  }

  /**
   * prints the board and houses
   *
   * @return the board and houses in a String
   */
  def toStringBoard: String = toStringHouse + gameState.board.toString()

  /**
   * prints the houses of each player
   *
   * @return the houses in a String
   */
  def toStringHouse: String = {
    val players: Vector[Player] = gameState.players._1
    val title: String = s"${Console.UNDERLINED}Houses${Console.RESET}"
    val up: String = "‾" * players.length * 3
    val down: String = "_" * players.length * 3
    var house: String = ""
    players.indices.foreach(i => house = house + s" ${players(i).color}${players(i).inHouse}${Console.RESET} ")
    "\n" + down + "\n" + house + "\t" + title + "\n" + up + "\n"
  }

  def getBoard: Board = gameState.board

  //Player
  //@TODO: extend method to dynamic playerADD with color algorithm, later... bitches
  def createPlayers(playerNames: List[String]): GameState = {
    val colors = gameStateMaster.colors
    val players: Vector[Player] = playerNames.indices.map(i => Player.PlayerBuilder().withColor(colors(i)).withName(playerNames(i)).build()).toVector
    gameState = gameStateMaster.UpdateGame().withPlayers(players).buildGame
    gameState
  }

  /**
   * Manages the round
   *
   * @param otherPlayer is not -1 when e.g. swapping -> user has to know when to insert more or less commands
   * @param pieceNum    is a List of indexes for the pieces of each player for e.g. swapping, only first is used when its about a move
   * @param cardNum     is the index of the card in a CardList of the player that is played
   * @return a String that is returned to the TUI for more information
   */
  def manageRound(otherPlayer: Int, pieceNum: List[Int], cardNum: Int): String = {
    val selectedPlayerList: List[Int] = gameState.players._2 :: otherPlayer :: Nil
    var returnString: String = ""
    if (useCardLogic(selectedPlayerList, pieceNum, cardNum) == 0) {
      gameState = gameStateMaster.UpdateGame().withNextPlayer().buildGame
      returnString = s"Player ${gameState.players._1(gameState.players._2).color}${gameState.players._1(gameState.players._2).name}${Console.RESET}'s turn\n"
    } else {
      undoCommand()
      returnString = s"Move was not possible! Please retry player ${gameState.players._1(gameState.players._2).color}${gameState.players._2}${Console.RESET} ;)\n"
    }
    returnString
  }

  /**
   * uses the card and extracts its logic
   *
   * @param selectedPlayerList is the list of Players -> first one is the actual player =>
   *                           managed by manageRound but can also be set manually for testing purposes
   * @param pieceNum           is a List of indexes for the pieces of each player for e.g. swapping, only first is used when its about a move
   * @param cardNum            is the index of the card in a CardList of the player that is played
   * @return
   */
  def useCardLogic(selectedPlayerList: List[Int], pieceNum: List[Int], cardNum: Int): Int = {
    val board: Board = gameState.board
    if (selectedPlayerList != Nil && gameState.players._1(selectedPlayerList.head).cardList.nonEmpty) {

      doStep()
      val selectedCard: Card = getSelectedCard(selectedPlayerList.head, cardNum)

      // will be changed later as well since other logic's aren't implemented yet
      val taskMode = CardLogic.getLogic(selectedCard.getTask)
      val moveInInt = if (selectedCard.getTask == "move") selectedCard.getSymbol.toInt else 0
      val updateGame: (Board, Vector[Player], Int) = CardLogic.setStrategy(taskMode, gameState.players._1, board, selectedPlayerList, pieceNum, moveInInt)
      if (updateGame._3 == 0) {
        gameState = gameStateMaster.UpdateGame().withPlayers(updateGame._2).withBoard(updateGame._1).buildGame
        notifyObservers
      }
      return updateGame._3
    }
    -1
  }

  //Cards

  def createCardDeck(amounts: List[Int]): (Vector[Card], Int) = {
    val array = Random.shuffle(CardDeck.apply(amounts)).toVector
    (array, array.length)
  }

  def toStringCardDeck: String = {
    var cardString: String = "________DECK________\n"
    val cardDeck: (Vector[Card], Int) = gameState.cardDeck
    cardDeck._1.indices.foreach(i => if (i < cardDeck._2) cardString += s"$i: ${cardDeck._1(i)}\n") + "\n"
    cardString
  }


  def getSelectedCard(playerNum: Int, cardNum: Integer): Card = {
    val player: Vector[Player] = gameState.players._1
    val oldCard = player(playerNum).getCard(cardNum)
    val newPlayer: Vector[Player] = player.updated(playerNum, player(playerNum).copy(cardList = player(playerNum).removeCard(player(playerNum).getCard(cardNum))))
    gameState = gameStateMaster.UpdateGame().withPlayers(newPlayer).buildGame
    println(s"$oldCard with ${oldCard.getTask}")
    oldCard
  }

  def drawFewCards(amount: Int): List[Card] = {
    var hand: List[Card] = Nil
    (0 until amount).foreach(x => hand = drawCardFromDeck :: hand)
    hand
  }

  def drawCardFromDeck: Card = {
    var cardDeck: (Vector[Card], Int) = gameState.cardDeck
    if (cardDeck._2 != 0)
      cardDeck = cardDeck.copy(cardDeck._1, cardDeck._2 - 1)
    gameState = gameStateMaster.UpdateGame().withCardDeck(cardDeck._1).withCardPointer(cardDeck._2).buildGame
    cardDeck._1(cardDeck._2)
  }

  def initAndDistributeCardsToPlayer(amount: Int): Unit = {
    val player: Vector[Player] = gameState.players._1
    var newPlayer: Vector[Player] = Vector.empty[Player]
    player.foreach(x => newPlayer = newPlayer.:+(x.copy(cardList = drawFewCards(amount))))
    gameState = gameStateMaster.UpdateGame().withPlayers(newPlayer).buildGame
  }

  def toStringPlayerHands: String = {
    val player: Vector[Player] = gameState.players._1
    var playerHands: String = ""
    player.foreach(x => playerHands = playerHands + s"${x.color}${x.name} ${Console.RESET} --> myHand: " + x.cardList + "\n")
    playerHands
  }


  //AREA 51-------------------------------------------------------------------------------------------------------

  def testDistributeCardsToPlayer(playerNum: Int, cards: List[Card]): Player = {
    val player: Vector[Player] = gameState.players._1
    val newPlayer: Vector[Player] = player.updated(playerNum, player(playerNum).setHandCards(cards))
    gameState = gameStateMaster.UpdateGame().withPlayers(newPlayer).buildGame
    newPlayer(playerNum)
  }
}
