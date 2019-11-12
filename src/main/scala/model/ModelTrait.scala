package model

import model.boardComponent.Board

trait ModelTrait {

  var board: Board

  def createBoard: Unit

  def getBoard: Board
}
