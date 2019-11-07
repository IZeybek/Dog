package model.Main

import model.CellTrait

case class Cell(absPos: Int, xy: Array[Int], filled: Boolean) extends CellTrait {
  def isFilled: Boolean = filled

  def fill(filled: Boolean): Boolean = filled
}
