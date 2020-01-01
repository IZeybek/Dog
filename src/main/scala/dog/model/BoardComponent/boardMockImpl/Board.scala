package dog.model.BoardComponent.boardMockImpl

import dog.controller.InputCard
import dog.model.BoardComponent.boardBaseImpl.Cell
import dog.model.BoardComponent.{BoardTrait, CellTrait}
import dog.model.Player

class Board(var size: Int) extends BoardTrait {

  override def updateSwapPlayers(player: Vector[Player], inputCard: InputCard): BoardTrait = this

  override def createNewBoard: BoardTrait = this

  override def cell(idx: Int): CellTrait = Cell(None)

  override def fill(cell: CellTrait, pos: Int): BoardTrait = this

  override def fill(boardMap: Map[Int, CellTrait]): BoardTrait = this

  override def getPieceIndex(idx: Int): Int = 0

  override def checkOverrideOtherPlayer(player: Player, newPos: Integer): Boolean = true

  override def updateMovePlayer(player: Player, oldPos: Int, newPos: Int): BoardTrait = this
}
