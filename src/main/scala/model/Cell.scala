package model

case class Cell(idx: Int, filled: Boolean, player: Player) {

  def getPos: Int = idx

  def isFilled: Boolean = filled

  override def toString: String = {
    "[" + (if (filled) s"${if (player.color == "gelb") Console.YELLOW; else if (player.color == "blau") Console.BLUE; else if (player.color == "green") Console.GREEN; else if (player.color == "rot") Console.RED; else Console.RESET}x" + s"${Console.RESET}" else " ") + "]"
  }
}
