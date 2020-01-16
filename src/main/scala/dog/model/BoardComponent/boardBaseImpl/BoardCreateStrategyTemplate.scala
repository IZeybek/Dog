package dog.model.BoardComponent.boardBaseImpl

import dog.model.BoardComponent.BoardTrait

trait BoardCreateStrategyTemplate {
  def createNewBoard(size: Int): BoardTrait = {
    var board: BoardTrait = new Board(size)
    board = prepare(board)
    board = fill(board)
    board
  }

  def prepare(board: BoardTrait): BoardTrait = {
    //do nothing on default
    board
  }

  def fill(board: BoardTrait): BoardTrait

}
