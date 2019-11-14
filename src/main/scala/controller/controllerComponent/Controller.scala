package controller.controllerComponent

import controller.ControllerTrait
import model.boardComponent.{Board, Cell}
import model.playerComponent.card.CardTrait
import model.{Piece, Player}

import scala.collection.mutable

class Controller extends ControllerTrait {

  override val board: Board = createBoard(4)
  override var player: Array[Player] = createPlayer(Array("Jo", "isy", "me", "Tead"))

  override def createBoard(size: Int): Board = Board(size)


  override def generateRandomCards(): Array[CardTrait] = {
    val array = mutable.ArrayBuffer.empty[CardTrait]
    for (_ <- 0 until 8) {
      array += board.createRandomCard()
    }
    array.toArray
  }

  override def createPlayer(name: Array[String]): Array[Player] = {
    println(name(0))
    player
  }

  //  override def getCards: Array[CardTrait] = player.cards


  //
  //  override def dragCard: CardTrait = {
  //    board.createRandomCard()
  //  }


  override def printBoard(): Unit = {
    print(board.toString)
  }


  //  override def printCard(): Unit = {
  //    printCard
  //  }
  override def move(player: Player, moveIndex: Int): Boolean = {
    true
  }
}
