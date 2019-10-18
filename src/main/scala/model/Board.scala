package model

case class Board(field: Array[Array[Cell]]) {
  val fill = (f: String, p: Int) => {
    if (f contains 0) {
      field()
    }

  }

}
