package model.BoardComponent.boardBaseImpl

import model.Player

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
