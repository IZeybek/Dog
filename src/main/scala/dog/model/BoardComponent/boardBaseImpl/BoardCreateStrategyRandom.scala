package dog.model.BoardComponent.boardBaseImpl

import dog.model.BoardComponent.BoardTrait
import dog.model.Player

import scala.util.Random

class BoardCreateStrategyRandom extends BoardCreateStrategyTemplate {

  var player: Vector[Player] = player.indices.map(_ => Player.PlayerBuilder().build()).toVector

  override def fill(board: BoardTrait): BoardTrait = {
    board.copy(boardMap = (0 until board.getBoardMap.size).map(i => (i, update(i, player(Random.nextInt(player.size - 1))))).toMap)
  }

  def setPlayer(setPlayer: Vector[Player]): Unit = {
    player = setPlayer
  }

  def update(idx: Int, player: Player): Cell = {
    Random.nextInt(1) match {
      case 0 =>
        if (player.inHouse > 0)
          Cell(idx, Some(player))
        else
          Cell(idx, None)
      case 1 =>
        Cell(idx, None)
    }
  }
}
