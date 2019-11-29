package controller

import java.nio.file.AtomicMoveNotSupportedException

import model.CardComponent.{Card, CardDeck, CardLogic, MoveLogic}
import model._
import util.Observable

import scala.util.Random

class Controller() extends Observable {

  var player: Array[Player] = createPlayer(List("p1", "p2", "p3", "p4"))
  val colors: Map[String, Integer] = Map("gelb" -> 0, "blau" -> 1, "grün" -> 2, "rot" -> 3)
  var cardDeck: Array[Card] = createCardDeck
  var board: Board = createBoard(20)
  var cardIndex: Integer = 0

  //Board

  def createBoard(size: Integer): Board = {
    board = new Board(size)
    notifyObservers
    board
  }

  def createRandomBoard: Board = {
    board = new BoardCreateStrategyRandom().createNewBoard(10)
    board
  }

  def toStringBoard: String = toStringHouse + board.toString()

  def toStringHouse: String = {
    val title = s"${Console.UNDERLINED}Houses${Console.RESET}"
    val up = "‾" * player.length * 3
    val down = "_" * player.length * 3
    var house = ""
    player.indices.foreach(i => house = house + s" ${
      player(i).color match {
        case "gelb" => Console.YELLOW;
        case "blau" => Console.BLUE;
        case "grün" => Console.GREEN;
        case "rot" => Console.RED
      }
    }${player(i).inHouse}${Console.RESET} ")
    "\n" + down + "\n" + house + "\t" + title + "\n" + up + "\n"
  }

  def getBoard: Board = board

  //Player

  def setPlayer(name: List[String]): Array[Player] = {
    player = createPlayer(name)
    player
  }

  def createPlayer(playerNames: List[String]): Array[Player] = {
    val player: Array[Player] = new Array[Player](playerNames.size)
    val colors = Array("gelb", "blau", "grün", "rot")
    playerNames.indices.foreach(i => player(i) = Player(playerNames(i), colors(i), Map(0 -> Piece(0), 1 -> Piece(0), 2 -> Piece(0), 3 -> Piece(0), 4 -> Piece(0)), 4, null))
    player
  }


  def movePlayer(playerNum: Integer, pieceNum: Integer): Player = {
    val logicMode: String = "move";
    val taskMode = CardLogic.getLogic(logicMode)
    val selectedCard = playCard(playerNum)

    val moveBy = if (selectedCard.getTask == "move") selectedCard.getSymbol.toString.toInt else 2
    val move: (Board, Array[Player]) = CardLogic.setStrategy(taskMode, player, board, playerNum, pieceNum, moveBy)

    board = move._1
    player = move._2

    notifyObservers
    player(playerNum)
  }

  //Cards

  def createCardDeck: Array[Card] = {

    val array = Random.shuffle(CardDeck.apply()).toArray
    cardIndex = array.length
    array
  }

  def toStringCardDeck: String = {
    var cardString: String = "________DECK________\n"
    cardDeck.indices.foreach(i => cardString += s"$i: ${cardDeck(i)}\n") + "\n"
  }

  def drawFewCards(amount: Int): List[Card] = {
    var hand: List[Card] = List(drawCardFromDeck)
    for (i <- 0 until amount - 1) {
      hand = drawCardFromDeck :: hand
    }
    hand
  }

  def drawCardFromDeck: Card = {
    if (cardIndex != 0) cardIndex = cardIndex - 1
    cardDeck(cardIndex)
  }

  def playCard(playerNum: Int): Card = {
    val oldCard = player(playerNum).getCard(0)
    player(playerNum) = player(playerNum).copy(cardList = player(playerNum).removeCard(player(playerNum).getCard(0)))
    println(oldCard.getTask)

    println(oldCard)
    oldCard
  }

  def toStringPlayerHands(): Unit = {
    for (i <- player.indices) {
      println("player: " + i + " --> myHand: " + player(i).cardList)
    }
  }

  def initPlayerHandCards(amount: Int): Unit = for (pNr <- player.indices) player(pNr) = player(pNr).setHandCards(drawFewCards(amount))


}
