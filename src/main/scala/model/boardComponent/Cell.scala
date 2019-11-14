package model.boardComponent

case class Cell(filled: Boolean) {

  val filling: String = ???

  def isFilled: Boolean = filled

  def fill(fill: Boolean): Boolean = {
    copy(filled = fill)
    true
  }

  override def toString: String = "[" + (if (filled) "" else "  ") + "]"

}
