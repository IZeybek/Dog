package model

case class Cell(idx: Int, p: Option[Player]) {
  def getPos: Int = idx

  override def toString: String = {
    var player: String = _
    p match {
      case Some(b) => player = b.color + "x" + Console.RESET
      case None => player = " "
    }
    "[" + player + "]"
  }
}
