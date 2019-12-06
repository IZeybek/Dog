package controller

import controller.GameState._
import model.CardComponent.{Card, CardDeck, CardLogic}
import model._
import util.{Observable, SolveCommand, UndoManager}

import scala.util.Random

class Controller(var board: Board) extends Observable {


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
  initAndDistributeCardsToPlayer(6)

  //Board
  def createNewBoard(size: Int): Board = {
    board = new Board(size)
    gameState = CREATEBOARD
    notifyObservers
    board
  }

  def createRandomBoard(size: Int): Board = {
    board = new BoardCreateStrategyRandom().createNewBoard(size)
    gameState = CREATEBOARD
    board
  }

  def toStringBoard: String = toStringHouse + board.toString()

  def toStringHouse: String = {
    val title = s"${Console.UNDERLINED}Houses${Console.RESET}"
    val up = "‾" * player.length * 3
    val down = "_" * player.length * 3
    var house = ""
    player.indices.foreach(i => house = house + s" ${player(i).color}${player(i).inHouse}${Console.RESET} ")
    "\n" + down + "\n" + house + "\t" + title + "\n" + up + "\n"
  }

  def getBoard: Board = board

  //Player
  //@TODO: extend method to dynamic playerADD with color algorithm, later... bitches
  def createPlayers(playerNames: List[String]): Array[Player] = {
    val colors = Array("gelb", "blau", "grün", "rot")
    player = playerNames.indices.map(i => Player.PlayerBuilder().withColor(colors(i)).withName(playerNames(i)).build()).toArray
    gameState = CREATEPLAYER
    player
  }

  def useCardLogic(selectedPlayerList: List[Int], pieceNum: List[Int], cardNum: Int): Player = {
    if (selectedPlayerList != Nil && player(selectedPlayerList.head).cardList.nonEmpty) {

      doStep()
      val selectedCard: Card = getSelectedCard(selectedPlayerList.head, cardNum)
      val task = selectedCard.getTask


      if (task == "swap" || task == "move") { // will be changed later as well since other logic's aren't implemented yet
        val taskMode = CardLogic.getLogic(task)
        val moveInInt = if (selectedCard.getTask == "move") selectedCard.getSymbol.toInt else 0
        val updateGame: (Board, Array[Player], Int) = CardLogic.setStrategy(taskMode, player, board, selectedPlayerList, pieceNum, moveInInt)


        board = updateGame._1
        player = updateGame._2


      }
      notifyObservers
      player(selectedPlayerList.head)
    } else {
      print("not enough arguments (select at least one Player) (will be changed later!!!)")
      notifyObservers
      player(0) // will be changed later, as "no player is selected" only occur in TUI
    }
  }

  //Cards

  def createCardDeck: (Array[Card], Int) = {
    val array = Random.shuffle(CardDeck.apply()).toArray
    (array, array.length)
  }

  def toStringCardDeck: String = {
    var cardString: String = "________DECK________\n"
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

  def drawCardFromDeck: Card = {
    if (cardDeck._2 != 0) cardDeck = (cardDeck._1, cardDeck._2 - 1)
    cardDeck._1(cardDeck._2)
  }

  def getSelectedCard(pIdx: Int, cardNum: Integer): Card = {
    val selectedCard = player(pIdx).getCard(cardNum)
    player(pIdx) = player(pIdx).updateCardList(player(pIdx).removeCard(selectedCard))

    println(s"you have selected ${selectedCard}")
    selectedCard
  }


  def toStringPlayerHands: Unit = {
    player.indices.foreach(i => println(s"${player(i).color}player: " + i + s"${Console.RESET} --> myHand: " + player(i).cardList))
  }

  def distributeCardsToPlayer(pIdx: Int, cards: List[Card]): Player = {
    player(pIdx) = player(pIdx).updateHandCards(cards)
    player(pIdx)
  }

  def initAndDistributeCardsToPlayer(amount: Int): Unit = {
    player.indices.foreach(pNr => player(pNr) = player(pNr).updateHandCards(drawFewCards(amount)))
  }


}
