package controller.controllerComponent

import controller.ControllerTrait
import model.boardComponent.{Board, Cell}
import model.playerComponent.card.CardTrait
import model.{Piece, Player}

import scala.collection.mutable

class Controller {

  val board: Board = createBoard(4)
  var player: Array[Player] = createPlayer(Array("Jo", "isy", "me", "Tead"))

  def createBoard(size: Int): Board = Board(size)


  def generateRandomCards(): Array[CardTrait] = {
    val array = mutable.ArrayBuffer.empty[CardTrait]
    for (_ <- 0 until 8) {
      array += board.createRandomCard()
    }
    array.toArray
  }

  def createPlayer(name: Array[String]): Array[Player] = {
    val player = new Array[Player](4)
    player(0) = new Player(name(0), Piece(Array(1, 2, 3, 4), "gelb"))
    player(1) = new Player(name(1), Piece(Array(1, 2, 3, 4), "blau"))
    player(2) = new Player(name(2), Piece(Array(1, 2, 3, 4), "black"))
    player(3) = new Player(name(3), Piece(Array(1, 2, 3, 4), "rot"))

    player
  }

  //   def getCards: Array[CardTrait] = player.cards


  //
  //   def dragCard: CardTrait = {
  //    board.createRandomCard()
  //  }


  def printBoard(): Unit = {
    print(board.toString)
  }


  //   def printCard(): Unit = {
  //    printCard
  //  }
  def move(player: Player, moveIndex: Int): Boolean = {
    true
  }
}
