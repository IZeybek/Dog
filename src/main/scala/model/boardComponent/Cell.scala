package model.boardComponent

class Cell(idx: Int) {

  var filled: Boolean = false

  def getPos: Int = idx

  def isFilled: Boolean = filled

  def fill(fill: Boolean): Boolean = {
    filled = fill
    filled
  }

  override def toString: String = "[" + (if (filled) "x" else "  ") + "]"

}
