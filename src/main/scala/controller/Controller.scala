package controller

import model.CardComponent.{Card, CardDeck}
import model._
import util.Observable

import scala.util.Random

class Controller() extends Observable {

  var player: Array[Player] = createPlayer(List("p1", "p2", "p3", "p4"))
  var board: Board = createRandomBoard
  val colors: Map[String, Integer] = Map("gelb" -> 0, "blau" -> 1, "grün" -> 2, "rot" -> 3)
  var cardDeck: Array[Card] = createCardDeck
  var cardIndex: Integer = 0

  //Board

  def setNewBoard(size: Integer): Board = {
    board = createBoard(size)
    notifyObservers
    board
  }

  def createBoard(size: Integer): Board = {
    board = new Board(size)
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
    notifyObservers
    player
  }

  def createPlayer(playerNames: List[String]): Array[Player] = {
    val player: Array[Player] = new Array[Player](playerNames.size)
    val colors = Array("gelb", "blau", "grün", "rot")
    playerNames.indices.foreach(i => player(i) = Player(playerNames(i), colors(i), Map(0 -> Piece(0), 1 -> Piece(0), 2 -> Piece(0), 3 -> Piece(0), 4 -> Piece(0)), 4, null))
    notifyObservers
    player
  }

  def movePlayer(playerNum: Integer, pieceNum: Integer, moveBy: Integer): Player = {
    //move piece of specific player by returning a copy of the piece to the copy constructor player and returning a copy of the player
    val p: Player = player(playerNum)
    if (board.overridePlayer(p, pieceNum, moveBy)) {
      val playerIndex = colors(board.getColor(moveBy + p.getPosition(pieceNum)))
      player(playerIndex) = player(playerIndex).overridePlayer(pieceNum)
    }
    board = board.movePlayer(p, pieceNum, moveBy)
    player(playerNum) = p.movePlayer(pieceNum, moveBy)
    notifyObservers
    player(playerNum)
  }


  //Cards

  def createCardDeck: Array[Card] = {
    notifyObservers
    Random.shuffle(CardDeck.apply()).toArray
  }

  def toStringCardDeck: String = {
    var cardString: String = "________DECK________\n"
    cardDeck.indices.foreach(i => cardString += s"$i: ${cardDeck(i)}\n") + "\n"
  }

  def drawCard: Card = {
    val card: Card = cardDeck(cardIndex)
    cardIndex = cardIndex + 1
    card
  }

  def drawPlayerCard(playerNum: Integer, cardNum: Integer): Card = {
    player(playerNum) = player(playerNum).copy(cards = player(playerNum).removeCard(player(playerNum).drawCard(cardNum)))
    player(playerNum).drawCard(cardNum)
  }

}
