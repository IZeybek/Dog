package model

import model.boardComponent.{Board, Read}
import model.playerComponent.card.CardTrait

case class Model() extends ModelTrait {

  override var board: Board = _
  override var player: Player = _

  override var cards: Array[CardTrait] = _

  override def createBoard: Board = {
    if (board == null)
      board = Read.readIn("src/feld.txt")
    board
  }

  override def getCards: Array[CardTrait] = cards

  override def getBoard: Board = board

  override def dragCard: CardTrait = {

  }
}
