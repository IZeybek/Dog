package model

import model.boardComponent.{Board, Read}

case class Model() extends ModelTrait {

  override def createBoard: Unit = {
    if (board == null)
      board = Read.readIn("src/feld.txt")
  }

  override def getBoard: Board = board

  override var board: Board = _
}
