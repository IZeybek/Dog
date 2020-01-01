package dog.model.BoardComponent

import dog.controller.InputCard
import dog.model.Player

trait BoardTrait {

  def createNewBoard: BoardTrait

  def toString: String

  def updateMovePlayer(player: Player, pieceNum: Integer, setPos: Integer): BoardTrait

  def checkOverrideOtherPlayer(player: Player, pieceNum: Integer, newPos: Integer): Boolean

  def cell(idx: Int): CellTrait

  def size: Int

  def fill(cell: CellTrait, pos: Int): BoardTrait

  def fill(boardMap: Map[Int, CellTrait]): BoardTrait

  def updateSwapPlayers(player: Vector[Player], inputCard: InputCard): BoardTrait
}

trait CellTrait {

  def p: Option[Player]

  def getPieceIdx: Int

  def removePlayerFromCell(): CellTrait

  def addPlayerToCell(p: Player, newPieceIdx: Int): CellTrait

  def getColor: String

  def toString: String

  def isFilled: Boolean
}
