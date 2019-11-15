package controller

import model.{Board, CardTrait, Player}

trait ControllerTrait {

  var player: Array[Player]
  val board: Board

  def move(player : Player, moveIndex:Int) : Boolean

  def createBoard(size: Int): Board

  //  def getCards: Array[CardTrait]
  //  def dragCard: CardTrait
  def printBoard: Unit

  def createPlayer(name: Array[String]): Array[Player]

  def generateRandomCards(): Array[CardTrait]

  //  def printCard: Unit = {
  //    player.cards.foreach(x => x.toString)
  //  }

}
