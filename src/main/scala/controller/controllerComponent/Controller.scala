package controller.controllerComponent

import model.{Board, Piece, Player}
import util.Observable

class Controller(playerNames: Array[String]) extends Observable {

  var board: Board = createBoard
  var player: Array[Player] = createPlayer(playerNames)

  def setNewBoard: Board = {
    board = Board()
    board
  }

  def createBoard: Board = {
    Board()
  }

  //
  //  def generateRandomCards(): Array[CardTrait] = {
  //    val array = mutable.ArrayBuffer.empty[CardTrait]
  //    for (_ <- 0 until 8) {
  //      array += board.createRandomCard()
  //    }
  //    notifyObservers
  //    array.toArray
  //  }

  def createPlayer(name: Array[String]): Array[Player] = {
    val player = new Array[Player](4)
    val map1, map2, map3, map4 = Map(0 -> new Piece(0), 1 -> new Piece(0), 2 -> new Piece(0), 3 -> new Piece(0))

    player(0) = Player(name(0), "gelb", map1)
    player(1) = Player(name(1), "blau", map2)
    player(2) = Player(name(2), "grün", map3)
    player(3) = Player(name(3), "rot", map4)
    notifyObservers
    player
  }

  def getBoard: Board = board

  def printBoard(): String = getBoard.toString

  //  def move(player: Player, moveIndex: Int, piece: Int): Boolean = {
  //      //    val old = player.getPiece(piece).position
  //
  //      //update Piece in Player
  //      //    val p: Player = player.copy(piece = player.piece.updated(piece, player.getPiece(piece).copy(position = old + moveIndex)))
  //      player.piece(piece).setPosition(moveIndex)
  //      notifyObservers
  //      true
  //    }
}
