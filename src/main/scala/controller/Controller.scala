package controller

import model.{Board, Piece, Player}
import util.Observable

class Controller() extends Observable {

  var board: Board = createBoard
  var player: Array[Player] = createPlayer(Array("p1", "p2", "p3", "p4"))

  def setNewBoard: Board = {
    board = Board()
    println("notifying observers in setNewBoard")
    notifyObservers
    board
  }

  def createBoard: Board = {
    notifyObservers
    println("notifying observers in createBoard")
    Board()
  }

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

  def move(playerNum: Integer, pieceNum: Integer, moveBy: Integer): Integer = {
    val newPos = player(playerNum).piece(pieceNum).setPosition(moveBy)
    player(playerNum) = player(playerNum).copy(piece = player(playerNum).piece.updated(pieceNum, player(playerNum).piece(pieceNum).setPosition(moveBy)))
    notifyObservers
    newPos
  }

}
