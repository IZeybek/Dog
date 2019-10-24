import model.Cell

import scala.collection.mutable.ListBuffer
import scala.io.Source

var x = 0
var y = 0
var z = 0
var cells = new ListBuffer[Cell]()
for (line <- Source.fromFile("C:\\Users\\Josef\\Documents\\Dog\\src\\feld.txt").getLines()) {
  for (e <- line) {
    if (e == 1) cells += Cell(z, Array(x, y), false)
    x += 1
    z += 1
  }
  y += 1
}



//val a = Array.ofDim[Cell](15, 15)


