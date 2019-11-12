package model

import model.boardComponent.Board
import model.playerComponent.card.CardTrait

trait ModelTrait {

  var board: Board
  var player: Player

  def createBoard: Unit
  def getBoard: Board

  var getCards: Array[CardTrait]
}
