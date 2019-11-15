package controller.controllerComponent

import model.boardComponent.Board
import model.playerComponent.card.CardTrait
import model.{Piece, Player}
import util.Observable

import scala.collection.mutable

class Controller extends Observable {

  val board: Board = createBoard(4)
  var player: Array[Player] = createPlayer(Array("Jo", "Isy", "Mario", "Luigi"))

  def createBoard(size: Int): Board = Board(size)

  def generateRandomCards(): Array[CardTrait] = {
    val array = mutable.ArrayBuffer.empty[CardTrait]
    for (_ <- 0 until 8) {
      array += board.createRandomCard()
    }
    notifyObservers
    array.toArray
  }

  def createPlayer(name: Array[String]): Array[Player] = {
    val player = new Array[Player](4)
    player(0) = new Player(name(0), Array(Piece(1, "gelb"), Piece(2, "gelb"), Piece(3, "gelb"), Piece(4, "gelb")))
    player(1) = new Player(name(1), Array(Piece(1, "blau"), Piece(2, "blau"), Piece(3, "blau"), Piece(4, "blau")))
    player(2) = new Player(name(2), Array(Piece(1, "grün"), Piece(2, "grün"), Piece(3, "grün"), Piece(4, "grün")))
    player(3) = new Player(name(3), Array(Piece(1, "rot"), Piece(2, "rot"), Piece(3, "rot"), Piece(4, "rot")))
    notifyObservers
    player
  }

  def printBoard(): Unit = print(board.toString)

  def move(player: Player, moveIndex: Int, piece: Int): Boolean = {
    player.getPiece(piece)
    true
  }
}
