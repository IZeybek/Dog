package dog.model.BoardComponent.boardBaseImpl

import dog.model.BoardComponent.CellTrait
import dog.model.Player

case class Cell(p: Option[Player]) extends CellTrait {

  override def isFilled: Boolean = {
    p match {
      case Some(_) => true
      case None => false
    }
  }

  override def removePlayerFromCell(): Cell = {
    this.p match {
      case Some(_) => copy(p = None)
      case None => copy(p = None)
    }
  }

  override def addPlayerToCell(newPlayer: Player): Cell = {
    this.p match {
      case Some(_) => copy(p = Some(newPlayer))
      case None => copy(p = Some(newPlayer))
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
