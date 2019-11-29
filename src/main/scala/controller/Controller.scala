package controller

import controller.GameState._
import model.CardComponent.{Card, CardDeck, CardLogic}
import model._
import util.Observable

import scala.util.Random

class Controller() extends Observable {

  val colors: Map[String, Integer] = Map("gelb" -> 0, "blau" -> 1, "grün" -> 2, "rot" -> 3)
  var gameState: GameState = IDLE
  var player: Array[Player] = createSetPlayer(List("p1", "p2", "p3", "p4"))
  var cardDeck: Array[Card] = createCardDeck

  var cardIndex: Integer = 0
  var board: Board = setNewBoard(16)

  //Board

  def setNewBoard(size: Int): Board = {
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

  def createSetPlayer(playerNames: List[String]): Array[Player] = {
    val colors = Array("gelb", "blau", "grün", "rot")
    player = (0 until playerNames.size).map(i => new Player(playerNames(i), colors(i), 4)).toArray
    gameState = CREATEPLAYER
    notifyObservers
    player
  }

  def parseCard(playerNum: Integer, pieceNum: Integer, cardNum: Integer): Unit = {
    val selectedCard: Card = playCard(playerNum, cardNum)
    selectedCard.color match {
      case "blue" => selectedCard.task match {
        case "move" => movePlayer(playerNum, pieceNum, selectedCard.symbol.toString.toInt)
        case _ =>
      }
      case "red" =>
      case _ =>
    }
  }

  def movePlayer(playerNum: Integer, pieceNum: Integer, moveBy: Integer): Player = {
    val taskMode = CardLogic.getLogic("move")
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

  def playCard(playerNum: Int, cardNum: Integer): Card = {
    val oldCard = player(playerNum).getCard(cardNum)
    player(playerNum) = player(playerNum).copy(cardList = player(playerNum).removeCard(player(playerNum).getCard(cardNum)))
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
