package model.boardComponent

case class Cell(absPos: Int, xy: Array[Int], filled: Boolean = false) {

  def isFilled: Boolean = filled

  def fill(fill: Boolean): Boolean = {
    copy(filled = fill)
    true
  }

  def canEqual(a: Any): Boolean = a.isInstanceOf[Cell]

  override def equals(that: Any): Boolean = {
    that match {
      case that: Cell => {
        that.canEqual(this) &&
          this.xy == that.xy &&
          this.absPos == that.absPos
      }
      case _ => false
    }
  }
}
