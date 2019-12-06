package controller

import controller.GameState._
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

  def replaceController(mementoBoard: Board, mementoPlayer: Array[Player], mementoCardDeck: (Array[Card], Int)): Boolean = {
    board = board.updateBoard(mementoBoard)

    player.indices.foreach(i => player(i) = player(i).update(mementoPlayer(i)))

    cardDeck = mementoCardDeck
    notifyObservers
    true
  }

  var gameState: GameState = IDLE
  var player: Array[Player] = createPlayers(List("p1", "p2", "p3", "p4"))
  var cardDeck: (Array[Card], Int) = createCardDeck //card deck and int pointer
  var gameStateMaster = new GameStateMaster
  var gameState: GameState = gameStateMaster.UpdateGame().buildGame
  initAndDistributeCardsToPlayer(6)

  //Board
  def createNewBoard(size: Int): Board = {
    val board = new Board(size)
    notifyObservers
    gameState = gameStateMaster.UpdateGame().withBoard(board).buildGame
    board
  }

  def createRandomBoard(size: Int): Board = {
    val board = new BoardCreateStrategyRandom().createNewBoard(size)
    notifyObservers
    gameState = gameStateMaster.UpdateGame().withBoard(board).buildGame
    board
  }

  def toStringBoard: String = toStringHouse + gameState.board.toString()

  def toStringHouse: String = {
    val players: Array[Player] = gameState.players._1
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
    val colors = Array("gelb", "blau", "grün", "rot")
    val players: Array[Player] = playerNames.indices.map(i => Player.PlayerBuilder().withColor(colors(i)).withName(playerNames(i)).build()).toArray
    gameState = gameStateMaster.UpdateGame().withPlayers(players).buildGame
    gameState
  }

  def useCardLogic(selectedPlayerList: List[Int], pieceNum: List[Int], cardNum: Int): Int = {
    val players: Array[Player] = gameState.players._1
    val board: Board = gameState.board
    if (selectedPlayerList != Nil && players(selectedPlayerList.head).cardList.nonEmpty) {

      doStep()
      val selectedCard: Card = getSelectedCard(selectedPlayerList.head, cardNum)
      val task = selectedCard.getTask

      if (task == "swap" || task == "move") { // will be changed later as well since other logic's aren't implemented yet
        val taskMode = CardLogic.getLogic(task)
        val moveInInt = if (selectedCard.getTask == "move") selectedCard.getSymbol.toInt else 0
        val updateGame: (Board, Array[Player], Int) = CardLogic.setStrategy(taskMode, players, board, selectedPlayerIndices, pieceNum, moveInInt)

        gameState = gameStateMaster.UpdateGame().withPlayers(updateGame._2).withBoard(updateGame._1).buildGame
        notifyObservers
        return updateGame._3
      }
    }
    -1
  }

  //Cards

  def createCardDeck: (Array[Card], Int) = {
    val array = Random.shuffle(CardDeck.apply()).toArray
    (array, array.length)
  }

  def toStringCardDeck: String = {
    var cardString: String = "________DECK________\n"
    val cardDeck: (Array[Card], Int) = gameState.cardDeck
    cardDeck._1.indices.foreach(i => if (i < cardDeck._2) cardString += s"$i: ${cardDeck._1(i)}\n") + "\n"
    cardString
  }

  def drawFewCards(amount: Int): List[Card] = {
    var hand: List[Card] = Nil
    for (i <- 0 until amount) {
      hand = drawCardFromDeck :: hand
    }
    hand
  }

  def getSelectedCard(playerNum: Int, cardNum: Integer): Card = {
    val player: Array[Player] = gameState.players._1
    val oldCard = player(playerNum).getCard(cardNum)
    player(playerNum) = player(playerNum).copy(cardList = player(playerNum).removeCard(player(playerNum).getCard(cardNum)))
    gameState = gameStateMaster.UpdateGame().withPlayers(player).buildGame
    println(s"$oldCard with ${oldCard.getTask}")
    oldCard
  }

  def drawCardFromDeck: Card = {
    var cardDeck: (Array[Card], Int) = gameState.cardDeck
    if (cardDeck._2 != 0) cardDeck = (cardDeck._1, cardDeck._2 - 1)
    gameState = gameStateMaster.UpdateGame().withCardDeck(cardDeck._1).withCardPointer(cardDeck._2).buildGame
    cardDeck._1(cardDeck._2)
  }

  def toStringPlayerHands: Unit = {
    val player: Array[Player] = gameState.players._1
    player.indices.foreach(i => println(s"${player(i).color}player: " + i + s"${Console.RESET} --> myHand: " + player(i).cardList))
  }

  def distributeCardsToPlayer(playerNum: Int, cards: List[Card]): Player = {
    val player: Array[Player] = gameState.players._1
    player(playerNum) = player(playerNum).setHandCards(cards)
    gameState = gameStateMaster.UpdateGame().withPlayers(player).buildGame
    player(playerNum)
  }

  def initAndDistributeCardsToPlayer(amount: Int): Unit = {
    val player: Array[Player] = gameState.players._1
    player.indices.foreach(pNr => player(pNr) = player(pNr).updateHandCards(drawFewCards(amount)))
    gameState = gameStateMaster.UpdateGame().withPlayers(player).buildGame
  }


}
