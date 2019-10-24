package model

case class Cell(absPos: Int, xy: Array[Int], filled: Boolean) {
  def isFilled: Boolean = filled
  def fill(filled: Boolean): Boolean = filled
}
