package model

case class Board(field: Array[Array[Cell]]) {
  def fill: Cell => Any = (c: Cell,) => c.xy()

}
