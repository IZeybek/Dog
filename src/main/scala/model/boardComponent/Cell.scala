package model.boardComponent

case class Cell(idx: Int, filled: Boolean) {

  def getPos: Int = idx

  def isFilled: Boolean = filled

  def fill(fill: Boolean): Boolean = {
    copy(filled = fill)
    true
  }

  override def toString: String = "[" + (if (filled) "x" else "  ") + "]"

}
