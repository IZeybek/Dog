package model

import model.boardComponent.Board
import model.playerComponent.card.CardTrait

trait ModelTrait {
  var player: Player
  var cards: Array[CardTrait]
  var board: Board

  def createBoard: Board
  def getBoard: Board

  def getCards: Array[CardTrait]

  def dragCard: CardTrait
}
