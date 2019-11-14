package model.boardComponent

case class Cell(filled: Boolean ) {

  def isFilled: Boolean = filled

  def fill(fill: Boolean): Boolean = {
    copy(filled = fill)
    true
  }

  //  def canEqual(a: Any): Boolean = a.isInstanceOf[Cell]
  //
  //  override def equals(that: Any): Boolean = {
  //    that match {
  //      case that: Cell => {
  //        that.canEqual(this) &&
  //          this.row == that.row && this.col  == that.col
  //      }
  //      case _ => false
  //    }
  //  }

  override def toString: String = "[" + (if (true) "g1"  else  "  ") + "]"

}
