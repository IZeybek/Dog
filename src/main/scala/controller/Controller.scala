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
    (0 until 10).foreach(i => boardMap += (i -> Cell(i, filled = false, null)))
    notifyObservers
    Board(boardMap)
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

  def createDeck: Array[Card] = {
    notifyObservers
    Random.shuffle(CardDeck().getDeck).toArray
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
    player(playerNum).drawCard(cardNum)
  }

}
