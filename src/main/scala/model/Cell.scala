package model

case class Cell(idx: Int, p: Option[Player]) {

  def removePlayer: Cell = {
    this.p match {
      case Some(_) => copy(p = None())
      case None() => this
    }
  }

  def addPlayer(p: Player): Cell = {
    this.p match {
      case Some(_) => this
      case None() => copy(p = Some(p))
    }
  }

  def getColor: String = {
    this.p match {
      case Some(p) => p.color
      case None() => " "
    }
  }

  override def toString: String = {
    var player: String = ""
    p match {
      case Some(p) => player = p.color + "x" + Console.RESET
      case None() => player = " "
    }
    "[" + player + "]"
  }
}
