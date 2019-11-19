package controller

import model._
import util.Observable

import scala.util.Random

class Controller() extends Observable {

  var board: Board = createBoard
  var player: Array[Player] = createPlayer(Array("p1", "p2", "p3", "p4"))
  var card: Array[CardTrait] = createRandomCards()

  //Board

  def setNewBoard: Board = {
    board = createBoard
    println("notifying observers in setNewBoard")
    notifyObservers
    board
  }


  def createBoard: Board = {

    var boardMap = Map(0 -> Cell(0, false))

    for {
      i <- 0 until 64
    } boardMap += (i -> Cell(i, false))

    notifyObservers
    Board(boardMap)
  }

  //Player

  def createPlayer(name: Array[String]): Array[Player] = {
    val player = new Array[Player](4)
    val map1, map2, map3, map4 = Map(0 -> new Piece(0), 1 -> new Piece(0), 2 -> new Piece(0), 3 -> new Piece(0))

    player(0) = Player(name(0), "gelb", map1)
    player(1) = Player(name(1), "blau", map2)
    player(2) = Player(name(2), "grün", map3)
    player(3) = Player(name(3), "rot", map4)
    println("notifying observers in createPlayer")
    notifyObservers
    player
  }

  def setPlayer(name: Array[String]): Array[Player] = {
    player = createPlayer(name)
    println("notifying observers in setPlayer")
    notifyObservers
    player
  }

  def getBoard: Board = board

  def toStringBoard(): String = getBoard.toString

  def movePlayer(playerNum: Integer, pieceNum: Integer, moveBy: Integer): Integer = {
    val newPos = player(playerNum).piece(pieceNum).position + moveBy
    //move piece of specific player by returning a copy of the piece to the copy constructor player and returning a copy of the player
    player(playerNum) = player(playerNum).copy(piece = player(playerNum).piece.updated(pieceNum, player(playerNum).piece(pieceNum).copy(player(playerNum).piece(pieceNum).position + moveBy)))
    board.boardMap
    notifyObservers
    newPos
  }

  //Cards

  def createRandomCards(): Array[CardTrait] = {
    val c = List.newBuilder[CardTrait]
    for (i <- 0 until 20) yield i match {
      case i if 0 until 10 contains i =>
        c += SevenCard()
      case i if 10 until 20 contains i =>
        c += ChangeCard()
    }
    val cards = c.result()
    Random.shuffle(cards)
    cards.toArray
  }

  def drawCard(): Unit = {

  }
}
