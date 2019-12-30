package dog.model.BoardComponent.boardBaseImpl

import dog.model.BoardComponent.CellTrait
import dog.model.Player

case class Cell(p: Option[Player], pieceIdx: Option[Int]) extends CellTrait {

  override def isFilled: Boolean = {
    p match {
      case Some(_) => true
      case None => false
    }
  }

  override def getPieceIdx: Int = {
    this.pieceIdx match {
      case Some(pieceIdx) => pieceIdx
      case None => 0
    }
  }

  override def removePlayerFromCell(): Cell = {
    this.p match {
      case Some(_) => copy(p = None, pieceIdx = None)
      case None => copy(p = None, pieceIdx = None)
    }
  }

  override def addPlayerToCell(newPlayer: Player, newPieceIdx: Int): Cell = {
    this.p match {
      case Some(_) => copy(p = Some(newPlayer), pieceIdx = Some(newPieceIdx))
      case None => copy(p = Some(newPlayer), pieceIdx = Some(newPieceIdx))
    }
  }

  override def getColor: String = {
    this.p match {
      case Some(p) => p.color
      case None => " "
    }
  }

  override def toString: String = {
    var player: String = ""
    p match {
      case Some(p) => player = p.consoleColor + "x" + Console.RESET
      case None => player = " "
    }
    "[" + player + "]"
  }
}
