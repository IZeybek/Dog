package dog.model.BoardComponent.boardAdvancedImpl

import com.google.inject.Inject
import com.google.inject.name.Named
import dog.model.BoardComponent.BoardTrait
import dog.model.BoardComponent.boardBaseImpl.{BoardCreateStrategyTemplate, Cell}
import dog.model.BoardComponent.boardMockImpl.{Board => BaseBoard}
import dog.model.Player

class Board @Inject()(@Named("DefaultSize") size: Int) extends BaseBoard(size) {
  override def createNewBoard: BoardTrait = (new BoardCreateStrategyNormal).createNewBoard(size)
}

class BoardCreateStrategyNormal extends BoardCreateStrategyTemplate {
  override def createNewBoard(size: Int): BoardTrait = {
    val board: BoardTrait = new Board(size)
    board.copy((0 until size).map(i => (i, Cell(i, None))).toMap)
  }

  override def fill(board: BoardTrait, player: Vector[Player]): BoardTrait = board
}
