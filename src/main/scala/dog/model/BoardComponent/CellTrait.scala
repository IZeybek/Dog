package dog.model.BoardComponent

import dog.model.BoardComponent.boardBaseImpl.Cell
import dog.model.Player

trait BoardTrait {
  def copy(boardMap: Map[Int, Cell]): BoardTrait

  def createNewBoard: BoardTrait

  def toString: String

  def updateMovePlayer(player: Player, pieceNum: Integer, setPos: Integer): BoardTrait

  def updateSwapPlayers(player: Vector[Player], playerNums: List[Int], pieceNums: List[Int]): BoardTrait

  def checkOverrideOtherPlayer(player: Player, pieceNum: Integer, newPos: Integer): Boolean

  def getBoardMap: Map[Int, Cell]
}

trait CellTrait {
  this: CellTrait =>

  def removePlayerFromCell(): CellTrait

  def addPlayerToCell(p: Player): CellTrait

  def getColor: String

  def toString: String
}
