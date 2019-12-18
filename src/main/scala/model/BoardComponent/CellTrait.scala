package model.BoardComponent

import model.Player

trait BoardTrait {
  def toString: String

  def updateMovePlayer(player: Player, pieceNum: Integer, setPos: Integer): BoardTrait

  def updateSwapPlayers(player: Vector[Player], playerNums: List[Int], pieceNums: List[Int]): BoardTrait

  def checkOverrideOtherPlayer(player: Player, pieceNum: Integer, newPos: Integer): Boolean
}

trait CellTrait {
  this: CellTrait =>

  def removePlayerFromCell(): CellTrait

  def addPlayerToCell(p: Player): CellTrait

  def getColor: String

  def toString: String
}
