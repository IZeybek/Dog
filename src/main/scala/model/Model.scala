package model

import model.boardComponent.{Board, Read}
import model.playerComponent.card.CardTrait

case class Model() extends ModelTrait {

  override var board: Board = _
  override var player: Player = _

  override def createBoard: Unit = {
    if (board == null)
      board = Read.readIn("src/feld.txt")
  }

  override def getBoard: Board = board

  override var getCards: Array[CardTrait] = _
}
