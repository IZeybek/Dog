package dog.model.BoardComponent

import dog.model.Player

trait BoardTrait {

  def createNewBoard: BoardTrait

  def toString: String

  def updateMovePlayer(player: Player, pieceNum: Integer, setPos: Integer): BoardTrait

  def updateSwapPlayers(player: Vector[Player], playerNums: List[Int], pieceNums: List[Int]): BoardTrait

  def checkOverrideOtherPlayer(player: Player, pieceNum: Integer, newPos: Integer): Boolean

  def cell(idx: Int): CellTrait

  def size: Int

  def fill(cell: CellTrait, pos: Int): BoardTrait

  def fill(boardMap: Map[Int, CellTrait]): BoardTrait
}

trait CellTrait {

  def p: Option[Player]

  def removePlayerFromCell(): CellTrait

  def addPlayerToCell(p: Player): CellTrait

  def getColor: String

  def toString: String

  def isFilled: Boolean
}
