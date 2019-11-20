package model

case class Cell(idx: Int, filled: Boolean, player: Int) {

  def getPos: Int = idx

  def isFilled: Boolean = filled

  override def toString: String = {

    "[" + (if (filled) s"${if (player == 0) Console.RED; else if (player == 1) Console.BLUE; else if (player == 2) Console.GREEN; else Console.YELLOW;}x" + s"${Console.RESET}" else " ") + "]"

  }
}
