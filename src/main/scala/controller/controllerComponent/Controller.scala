package controller.controllerComponent

import model.boardComponent.Board
import model.playerComponent.card.CardTrait
import model.{Piece, Player}
import util.Observable

import scala.collection.mutable

class Controller(playerNames : Array[String]) extends Observable {

  val board: Board = createBoard
  val player: Array[Player] = createPlayer(playerNames)

  def createBoard: Board = Board()
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
    player(0) =  Player(name(0), "gelb", Map(0 -> Piece(0), 1 -> Piece(0), 2 -> Piece(0), 3 -> Piece(0)))
    player(1) =  Player(name(1), "blau", Map(0 -> Piece(0), 1 -> Piece(0), 2 -> Piece(0), 3 -> Piece(0)))
    player(2) =  Player(name(2), "grün", Map(0 -> Piece(0), 1 -> Piece(0), 2 -> Piece(0), 3 -> Piece(0)))
    player(3) =  Player(name(3), "rot", Map(0 -> Piece(0), 1 -> Piece(0), 2 -> Piece(0), 3 -> Piece(0)))
    notifyObservers
    player
  }

  def printBoard(): Unit = print(board.toString)

//  def move(player: Player, moveIndex: Int, piece: Int): Player = {
//    val old = player.getPiece(piece).position
//
//    //update Piece in Player
//    val p: Player = player.copy(piece = player.piece.updated(piece, player.getPiece(piece).copy(position = old + moveIndex)))
//    notifyObservers
//    p
//  }
}
