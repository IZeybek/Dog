package dog.model.BoardComponent.boardBaseImpl

import dog.model.BoardComponent.boardAdvancedImpl.BoardCreateStrategyNormal
import dog.model.BoardComponent.{BoardTrait, CellTrait}
import dog.model.Player

case class Board(boardMap: Map[Int, CellTrait]) extends BoardTrait {

  //can create a Board with a given size
  def this(size: Int) = this((0 until size).map(i => (i, Cell(None))).toMap)

  def this(id: Int, size: Int) = {
    this((0 until size).map(i => (i, Cell(None))).toMap)
  }

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

  override def getPieceIndex(idx: Int): Int = {
    boardMap(idx).p match {
      case Some(player) => player.getPieceNum(idx)
      case None => -1
    }
  }

  override def updateMovePlayer(player: Player, oldPos: Int, newPos: Int): BoardTrait = {
    //set old Cell unoccupied
    var nBoard: Map[Int, CellTrait] = boardMap.updated(oldPos, boardMap(oldPos).removePlayerFromCell())
    //set new Pos as occupied
    nBoard = nBoard.updated(newPos, boardMap(newPos).addPlayerToCell(p = player))
    copy(boardMap = nBoard)
  }

  override def updateSwapPlayers(actPlayer: Player, swapPlayer: Player, selPieceList: List[Int]): BoardTrait = {

    val selPiece = selPieceList.head
    val selOtherPiece = selPieceList(1)
    var nBoard: BoardTrait = fill(cell(actPlayer.piecePosition(selPiece)).addPlayerToCell(actPlayer), actPlayer.piecePosition(selPiece))
    nBoard = nBoard.fill(nBoard.cell(swapPlayer.piecePosition(selOtherPiece)).addPlayerToCell(swapPlayer), swapPlayer.piecePosition(selOtherPiece))
    nBoard
  }

  override def createNewBoard: BoardTrait = (new BoardCreateStrategyNormal).createNewBoard(boardMap.size)

  override def checkOverrideOtherPlayer(player: Player, newPos: Integer): Boolean = boardMap(newPos).isFilled

  override def fill(cell: CellTrait, pos: Int): BoardTrait = copy(boardMap = boardMap.updated(pos, cell))

  override def fill(boardMap: Map[Int, CellTrait]): BoardTrait = copy(boardMap)
}
