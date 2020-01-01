package dog.model.BoardComponent

import dog.controller.InputCard
import dog.model.Player

trait BoardTrait {

  def createNewBoard: BoardTrait

  def toString: String

  def checkOverrideOtherPlayer(player: Player, newPos: Integer): Boolean

  def cell(idx: Int): CellTrait

  def size: Int

  def fill(cell: CellTrait, pos: Int): BoardTrait

  def fill(boardMap: Map[Int, CellTrait]): BoardTrait

  def updateSwapPlayers(player: Vector[Player], inputCard: InputCard): BoardTrait

  def getPieceIndex(idx: Int): Int

  def updateMovePlayer(player: Player, oldPos: Int, newPos: Int): BoardTrait
}

trait CellTrait {

  def p: Option[Player]

  def removePlayerFromCell(): CellTrait

  def addPlayerToCell(p: Player): CellTrait

  def getColor: String

  def toString: String

  def isFilled: Boolean
}
