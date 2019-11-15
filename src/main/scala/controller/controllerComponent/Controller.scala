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
    player(0) = Player(name(0), Map(0 -> Piece(0, "gelb"), 1 -> Piece(0, "gelb"), 2 -> Piece(0, "gelb"), 3 -> Piece(0, "gelb")))
    player(1) = Player(name(1), Map(0 -> Piece(0, "blau"), 1 -> Piece(0, "blau"), 2 -> Piece(0, "blau"), 3 -> Piece(0, "blau")))
    player(2) = Player(name(2), Map(0 -> Piece(0, "grün"), 1 -> Piece(0, "grün"), 2 -> Piece(0, "grün"), 3 -> Piece(0, "grün")))
    player(3) = Player(name(3), Map(0 -> Piece(0, "rot"), 1 -> Piece(0, "rot"), 2 -> Piece(0, "rot"), 3 -> Piece(0, "rot")))
    notifyObservers
    player
  }

  def printBoard(): Unit = print(board.toString)

  def move(player: Player, moveIndex: Int, piece: Int): Player = {
    val old = player.getPiece(piece).position

    //update Piece in Player
    val p: Player = player.copy(piece = player.piece.updated(piece, player.getPiece(piece).copy(position = old + moveIndex)))
    notifyObservers
    p
  }
}
