package dog.model.BoardComponent

import dog.model.Player

trait BoardTrait {

  def createNewBoard: BoardTrait

  def toString: String

  def cell(idx: Int): CellTrait

  def size: Int

  def fill(cell: CellTrait, pos: Int): BoardTrait

  def fill(boardMap: Map[Int, CellTrait]): BoardTrait

  def getPieceIndex(idx: Int): Int

  def updateMovePlayer(player: Player, oldPos: Int, newPos: Int): BoardTrait

  def updateSwapPlayers(actPlayer: Player, swapPlayer: Player, selPieceList: List[Int]): BoardTrait

  def checkOverrideOtherPlayer(newPos: Integer): Boolean
}

trait CellTrait {

  def p: Option[Player]

  def removePlayerFromCell(): CellTrait

  def addPlayerToCell(p: Player): CellTrait

  def getColor: String

  def toString: String

  def isFilled: Boolean

  def checkIfPlayer(player: Player): Boolean

  def getPlayerIdx: Int
}
