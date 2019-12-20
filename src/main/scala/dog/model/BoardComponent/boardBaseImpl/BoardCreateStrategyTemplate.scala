package dog.model.BoardComponent.boardBaseImpl

import dog.model.BoardComponent.BoardTrait
import dog.model.Player

trait BoardCreateStrategyTemplate {
  def createNewBoard(size: Int): BoardTrait = {
    val board: Board = new Board(size)
    board
  }

  def prepare(board: Board): BoardTrait = {
    //do nothing on default
    board
  }

  def fill(board: BoardTrait, player: Vector[Player]): BoardTrait

}
