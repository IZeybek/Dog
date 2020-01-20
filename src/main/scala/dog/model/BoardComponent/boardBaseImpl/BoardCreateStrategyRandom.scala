package dog.model.BoardComponent.boardBaseImpl

import dog.model.BoardComponent.{BoardTrait, CellTrait}
import dog.model.{Player, PlayerBuilder}

import scala.util.Random

class BoardCreateStrategyRandom extends BoardCreateStrategyTemplate {

  var player: Vector[Player] = (0 until 4).indices.map(_ => new PlayerBuilder().Builder().build()).toVector

  override def fill(board: BoardTrait): BoardTrait = {
    board.fill((0 until board.size).map(i => (i, update(player(Random.nextInt(player.size - 1))))).toMap)
  }

  def setPlayer(setPlayer: Vector[Player]): Unit = {
    player = setPlayer
  }

  def update(player: Player): CellTrait = {
    Random.nextInt(1) match {
      case 0 =>
        if (player.inHouse.nonEmpty)
          Cell(Some(player))
        else
          Cell(None)
      case 1 =>
        Cell(None)
    }
  }
}
