package model.Main

import model.ModelTrait

case class Model() extends ModelTrait {

  val board = Read.readIn("src/feld.txt")

  override def createBoard: Boolean = {
    if (board != null) true
    else false
  }

  override def getBoard: Board = board
}
