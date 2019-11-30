package model

import scala.util.Random

class BoardCreateStrategyRandom extends BoardCreateStrategyTemplate {
  override def fill(board: Board, player: Array[Player]): Board = {
    board.copy(boardMap = (0 until board.boardMap.size).map(i => (i, update(i, player(Random.nextInt(player.size - 1))))).toMap)
  }

  def update(idx: Int, player: Player): Cell = {
    Random.nextInt(1) match {
      case 0 =>
        if (player.inHouse > 0)
          Cell(idx, true, player)
        else
          Cell(idx, false, null)
      case 1 =>
        Cell(idx, false, null)
    }
  }
}
