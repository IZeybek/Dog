package controller.controllerComponent

import controller.ControllerTrait
import model.boardComponent.{Board, Read}
import model.playerComponent.card.CardTrait
import model.Player

import scala.collection.mutable

class Controller extends ControllerTrait {

  override var board: Board = _
  override var player: Player = _


  override def createBoard: Board = {
    if (board == null)
      board = Read.createBoard("src/feld.txt")
    board
  }

  override def generateRandomCards(): Array[CardTrait] = {
    val array = mutable.ArrayBuffer.empty[CardTrait]
    for (_ <- 0 until 8) {
      array += board.createRandomCard()
    }
    array.toArray
  }

  override def createPlayer(name: String): Player = {
    player = Player(name, generateRandomCards(), null)
    player
  }

  override def getCards: Array[CardTrait] = player.cards

  override def getBoard: Board = board

  override def dragCard: CardTrait = {
    board.createRandomCard()
  }


  override def printBoard(): Unit = {
    getBoard.toString
  }


  override def printCards(): Unit = {
    printCard
  }
}
