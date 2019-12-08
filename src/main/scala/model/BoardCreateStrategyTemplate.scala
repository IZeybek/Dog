package model

trait BoardCreateStrategyTemplate {
  def createNewBoard(size: Int): Board = {
    val board: Board = new Board(size)
    board
  }

  def prepare(board: Board): Board = {
    //do nothing
    board
  }

  def fill(board: Board, player: Vector[Player]): Board

}
