package controller

import model.Player
import model.boardComponent.{Board}
import model.playerComponent.card.CardTrait

trait ControllerTrait {

  var player: Player
  val board: Board

  def createBoard(size:Int):  Board

  def getCards: Array[CardTrait]
  def dragCard: CardTrait
  def printBoard : Unit
  def createPlayer(name: String): Player

  def generateRandomCards(): Array[CardTrait]

  def printCard: Unit = {
    player.cards.foreach(x => x.toString)
  }

}
