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

  override def removePlayerFromCell(): Cell = copy(None)

  override def addPlayerToCell(newPlayer: Player): Cell = copy(p = Some(newPlayer))

  override def checkIfPlayer(player: Player): Boolean = {
    p match {
      case Some(playerOnCell) => playerOnCell.nameAndIdx._1 == player.nameAndIdx._1 && playerOnCell.nameAndIdx._2 == player.nameAndIdx._2
      case None => false
    }
  }

  override def getPlayerIdx: Int = {
    this.p match {
      case Some(p) => p.nameAndIdx._2
      case None => -1
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
