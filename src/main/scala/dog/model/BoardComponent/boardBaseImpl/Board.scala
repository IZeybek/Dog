package dog.model.BoardComponent.boardBaseImpl

import dog.model.BoardComponent.boardAdvancedImpl.BoardCreateStrategyNormal
import dog.model.BoardComponent.{BoardTrait, CellTrait}
import dog.model.Player

case class Board(boardMap: Map[Int, CellTrait]) extends BoardTrait {

  //can create a Board with a given size
  def this(size: Int) = this((0 until size).map(i => (i, Cell(None))).toMap)

  override def size: Int = boardMap.size

  override def cell(idx: Int): CellTrait = boardMap(idx)

  override def toString: String = {
    var box = ""
    val line_down = "_" * boardMap.size * 3 + "\n"
    val line_up = "\n" + "‾" * boardMap.size * 3
    box = box + line_down
    for (i <- 0 until boardMap.size) {
      box += boardMap(i).toString
    }
    box += line_up + "\n"
    box
  }

  override def updateMovePlayer(player: Player, pieceNum: Integer, setPos: Integer): Board = {
    val oldPos: Integer = player.getPosition(pieceNum)

    //set old Cell unoccupied
    var nBoard: Map[Int, CellTrait] = boardMap.updated(oldPos, boardMap(oldPos).removePlayerFromCell())
    //set new Pos as occupied
    nBoard = nBoard.updated(setPos, boardMap(setPos).addPlayerToCell(p = player))
    copy(boardMap = nBoard)
  }

  override def updateSwapPlayers(player: Vector[Player], playerNums: List[Int], pieceNums: List[Int]): Board = {

    val p: Player = player(playerNums.head)
    val swapPlayer: Player = player(playerNums(1))

    //set cell to swapPlayer
    var nBoard: Map[Int, CellTrait] = boardMap.updated(p.getPosition(pieceNums.head), boardMap(p.getPosition(pieceNums.head)).addPlayerToCell(swapPlayer))

    //set cell to player
    nBoard = nBoard.updated(swapPlayer.getPosition(pieceNums(1)), boardMap(swapPlayer.getPosition(pieceNums(1))).addPlayerToCell(p))
    copy(boardMap = nBoard)
  }

  override def checkOverrideOtherPlayer(player: Player, pieceNum: Integer, newPos: Integer): Boolean = {
    boardMap(newPos).isFilled
  }

  override def createNewBoard: BoardTrait = (new BoardCreateStrategyNormal).createNewBoard(boardMap.size)

  override def fill(cell: CellTrait, pos: Int): BoardTrait = {
    copy(boardMap = boardMap.updated(pos, cell))
  }

  override def fill(boardMap: Map[Int, CellTrait]): BoardTrait = {
    copy(boardMap)
  }
}

