package model

case class Cell(idx: Int, filled: Boolean) {

  def getPos: Int = idx

  def isFilled: Boolean = filled

  override def toString: String = "[" + (if (filled) "x" else "  ") + "]"

}
