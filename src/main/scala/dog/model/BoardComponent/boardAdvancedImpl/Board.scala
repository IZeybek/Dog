package dog.model.BoardComponent.boardAdvancedImpl

import com.google.inject.Inject
import com.google.inject.name.Named
import dog.model.BoardComponent.BoardTrait
import dog.model.BoardComponent.boardBaseImpl.{Board => BaseBoard, _}

class Board @Inject()(@Named("DefaultSize") size: Int) extends BaseBoard(size) {
  override def createNewBoard: BoardTrait = (new BoardCreateStrategyNormal).createNewBoard(size)
}

class BoardCreateStrategyNormal extends BoardCreateStrategyTemplate {
  override def fill(board: BoardTrait): BoardTrait = {
    board.fill((0 until board.size).map(i => (i, Cell(None, None))).toMap)
  }
}
