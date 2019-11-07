package model

trait CellTrait {
  def isFilled: Boolean

  def fill(filled: Boolean): Boolean
}
