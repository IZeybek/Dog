package controller

import model.CardComponent.{Card, CardDeck}
import model._
import util.Observable

import scala.util.Random

class Controller() extends Observable {

  var board: Board = createBoard
  var player: Array[Player] = createPlayer(Array("p1", "p2", "p3", "p4"))
  var colors: Map[String, Integer] = Map("gelb" -> 0, "blau" -> 1, "grün" -> 2, "rot" -> 3)
  var cardDeck: Array[Card] = createDeck
  var cardIndex: Integer = 0

  //Board

  def setNewBoard: Board = {
    board = createBoard
    notifyObservers
    board
  }

  def createBoard: Board = {

    var boardMap = Map(0 -> Cell(0, filled = false, null))

    for {
      i <- 0 until 10
    } boardMap += (i -> Cell(i, filled = false, null))

    notifyObservers
    Board(boardMap)
  }

  def toStringBoard: String = toStringHouse + board.toString()

  def toStringHouse: String = {
    val title = s"${Console.UNDERLINED}Houses${Console.RESET}"
    val up = "‾" * player.length * 3
    val down = "_" * player.length * 3
    var house = ""
    for (i <- player.indices) {
      house = house + s" ${
        player(i).color match {
          case "gelb" => Console.YELLOW;
          case "blau" => Console.BLUE;
          case "grün" => Console.GREEN;
          case "rot" => Console.RED
        }
      }${player(i).inHouse}${Console.RESET} "
    }
    "\n" + down + "\n" + house + "\t" + title + "\n" + up + "\n"
  }

  def getBoard: Board = board

  //Player

  def setPlayer(name: Array[String]): Array[Player] = {
    player = createPlayer(name)
    notifyObservers
    player
  }

  def createPlayer(name: Array[String]): Array[Player] = {
    val player = new Array[Player](4)
    val map1, map2, map3, map4 = Map(0 -> Piece(0), 1 -> Piece(0), 2 -> Piece(0), 3 -> Piece(0), 4 -> Piece(0))

    player(0) = Player(name(0), "gelb", map1, 4, null)
    player(1) = Player(name(1), "blau", map2, 4, null)
    player(2) = Player(name(2), "grün", map3, 4, null)
    player(3) = Player(name(3), "rot", map4, 4, null)

    notifyObservers
    player
  }

  def movePlayer(playerNum: Integer, pieceNum: Integer, moveBy: Integer): Boolean = {
    //move piece of specific player by returning a copy of the piece to the copy constructor player and returning a copy of the player
    var overRide: Boolean = false
    val p: Player = player(playerNum)
    if (board.overridePlayer(p, pieceNum, moveBy)) {
      val playerIndex = colors(board.getColor(moveBy + p.getPosition(pieceNum)))
      player(playerIndex) = player(playerIndex).overridePlayer(pieceNum)
      overRide = true
    }
    board = board.movePlayer(p, pieceNum, moveBy)
    player(playerNum) = p.movePlayer(pieceNum, moveBy)
    notifyObservers
    overRide
  }


  //Cards

  def createDeck: Array[Card] = {
    val deck = CardDeck().getDeck
    Random.shuffle(deck).toArray
  }

  def toStringCardDeck: String = {
    var cardString = "________DECK________\n"
    for (i <- cardDeck.indices) {
      cardString += s"$i: ${cardDeck(i)}\n"
    }
    cardString + "\n"
  }

  def drawCard: Card = {
    val card: Card = cardDeck(cardIndex)
    cardIndex = cardIndex + 1
    card
  }

}
