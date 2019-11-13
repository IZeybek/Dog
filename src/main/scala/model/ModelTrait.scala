package model

import model.boardComponent.Board
import model.playerComponent.card.CardTrait

trait ModelTrait {

  var player: Player
  var board: Board

  def createBoard: Board
  def getBoard: Board
  def getCards: Array[CardTrait]
  def dragCard: CardTrait

  def createPlayer(name: String): Player

  def generateRandomCards(): Array[CardTrait]

  def printCard: Unit = {
    player.cards.foreach(x => x.toString)
  }
}
