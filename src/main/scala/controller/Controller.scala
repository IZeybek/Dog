package controller

import model._
import util.Observable

import scala.util.Random

class Controller() extends Observable {

  var board: Board = createBoard
  var player: Array[Player] = createPlayer(Array("p1", "p2", "p3", "p4"))
//  var card: Array[Cards] = createRandomCards
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
    val up = "‾" * player.length * 3
    val down = "_" * player.length * 3
    var house = ""
    for (i <- player.indices) {
      house = house + s" ${
        i match {
          case 0 => Console.YELLOW;
          case 1 => Console.BLUE;
          case 2 => Console.GREEN;
          case 3 => Console.RED
        }
      }${player(i).inHouse}${Console.RESET} "
    }
    down + "\n" + house + "\n" + up + "\n"
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

    player(0) = Player(name(0), "gelb", map1, 4)
    player(1) = Player(name(1), "blau", map2, 4)
    player(2) = Player(name(2), "grün", map3, 4)
    player(3) = Player(name(3), "rot", map4, 4)

    notifyObservers
    player
  }

  def movePlayer(playerNum: Integer, pieceNum: Integer, moveBy: Integer): Integer = {
    //move piece of specific player by returning a copy of the piece to the copy constructor player and returning a copy of the player
    var playerIndex = -1
    if (board.boardMap(moveBy + player(playerNum).piece(pieceNum).position).isFilled) {
      val color = board.boardMap(moveBy + player(playerNum).piece(pieceNum).position).player.color
      for (i <- player.indices) yield {
        if (player(i).color == color)
          playerIndex = i
      }
      player(playerIndex) = player(playerIndex).overridePlayer(pieceNum)
    }
    board = board.movePlayer(player(playerNum), pieceNum, moveBy)
    player(playerNum) = player(playerNum).movePlayer(pieceNum, moveBy)
    notifyObservers
    playerIndex
  }

  //Cards

//  def createRandomCards: Array[Cards] = {
//    val c = List.newBuilder[Cards]
//    for (i <- 0 until 20) yield i match {
//      case i if 0 until 10 contains i =>
//        c += SevenCard()
//      case i if 10 until 20 contains i =>
//        c += ChangeCard()
//    }
//    val cards = c.result()
//    Random.shuffle(cards)
//    cardIndex = 0
//    cards.toArray
//  }

//  def drawCard: Cards = {
//    if (cardIndex == 20)
//      createRandomCards
//    val c = card(cardIndex)
//    cardIndex += 1
//    c
//  }
}
