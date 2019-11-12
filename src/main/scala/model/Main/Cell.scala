package model.Main

case class Cell(absPos: Int, xy: Array[Int], filled: Boolean = false) {

  def isFilled: Boolean = filled

  def fill(fill: Boolean): Boolean = {
    copy(filled = fill)
    true
  }

}
