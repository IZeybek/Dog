package controller

import controller.GameState._
import model.CardComponent.{Card, CardDeck}
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

  def createRandomBoard: Board = {
    board = new BoardCreateStrategyRandom().createNewBoard(10)
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


  def movePlayer(playerNum: Integer, pieceNum: Integer, moveBy: Integer): Player = {
    //move piece of specific player by returning a copy of the piece to the copy constructor player and returning a copy of the player
    val p: Player = player(playerNum)
    if (board.overridePlayer(p, pieceNum, moveBy)) {
      val playerIndex = colors(board.getPlayerColor(moveBy + p.getPosition(pieceNum)))
      player(playerIndex) = player(playerIndex).overridePlayer(pieceNum)
    }
    board = board.movePlayer(p, pieceNum, moveBy)
    player(playerNum) = p.movePlayer(pieceNum, moveBy)
    gameState = MOVE
    notifyObservers
    player(playerNum)
  }

  def playCard(playerNum : Int): Card ={
    player(playerNum).getCard(0)
  }

  def drawCards(amount: Int): List[Card] = {
    var hand : List[Card] = List(drawCard)
    for(i <- 0 until amount-1){
      hand = drawCard ::  hand
    }
    gameState = DRAWCARD
    hand
  }

  def toStringPlayerHands(): Unit = {
    for (i <- player.indices) {
      println("player: " + i + " --> myHand: " + player(i).cardList)
    }
  }

  def initPlayerHandCards(amount: Int): Unit = for (pNr <- player.indices) player(pNr) = player(pNr).setHandCards(drawCards(amount))


  //Cards

  def createCardDeck: Array[Card] = {
    notifyObservers
    val array = Random.shuffle(CardDeck.apply(2, 2)).toArray
    cardIndex = array.length
    array
  }

  def toStringCardDeck: String = {
    var cardString: String = "________DECK________\n"
    cardDeck.indices.foreach(i => cardString += s"$i: ${cardDeck(i)}\n") + "\n"
  }


  def drawCard: Card = {
    if (cardIndex != 0) cardIndex = cardIndex - 1
    cardDeck(cardIndex)
  }

  def drawPlayerCard(playerNum: Integer, cardNum: Integer): Card = {
    player(playerNum) = player(playerNum).copy(cardList = player(playerNum).removeCard(player(playerNum).getCard(cardNum)))
    player(playerNum).getCard(cardNum)
  }

}


