package util

import model.Cell

import scala.io.Source

case class Read() {
  def prettyPrint(c: Array[Cell], dim: Array[Int]): Unit = {
    //@TODO write Board Class with all components like the Array[Cell] but also Dimension -> possible to use only Board as parameter
    val x = dim(0);
    val y = dim(1)
    var j = 0;
    var h = 0

    val checkPos = (i: Int, j: Int, c: Array[Cell]) => i == c(h).xy(1) && j == c(h).xy(0)

    for (i <- 0 until y) {
      for (j <- 0 until x) {
        if (c.last.absPos + 1 != h && checkPos(i, j, c)) {
          print("i")
          h += 1
        } else
          print(" ")
      }
      println("")
      j = 0
    }
  }


  def readIn(path: String): Array[Cell] = {
    val cells = scala.collection.mutable.ArrayBuffer.empty[Cell]
    val file = Source.fromFile(path)
    var x = 0;
    var y = 0;
    var z = 0
    for (line <- file.getLines()) {
      x = 0
      for (e <- line) {
        if (e == '1') {
          cells += Cell(z, Array(x, y), true)
          z += 1
        }
        x += 1
      }
      y += 1
    }
    cells.toArray
  }

}
