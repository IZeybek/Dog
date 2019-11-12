
import scala.io.Source

case class Cell(absPos: Int, xy: Array[Int], filled: Boolean) {
  def isFilled: Boolean = filled
  def fill(filled: Boolean): Boolean = filled
}


//read 2D Board in 1D array
val cells = scala.collection.mutable.ArrayBuffer.empty[Cell]
val file = Source.fromFile("C:\\Users\\Josef\\Documents\\Dog\\src\\feld.txt")
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
val c = cells.toArray


/** prints 2D Board from 1D array
 *
 * @param c   is a 1D Array that consists of Cells
 * @param dim is the dimension of the Field
 */
def prettyPrint(c: Map[Int, Cell], dim: Array[Int]): String = {

  val x = dim(0)
  val y = dim(1)

  var h = 0
  val checkPos = (i: Int, j: Int, c: Array[Cell]) => i == c(h).xy(1) && j == c(h).xy(0)
  val str = ""

  val check: String = (cell: Cell) => {
    if (lastPos + 1 != cellPos && checkPos) {
      "i"
    } else {
      " "
    }
  }

  for (i <- 0 until y;
       j <- 0 until x) {
    if (c.last.absPos + 1 != h && checkPos(i, j, c)) {
      StringC(str, "i")
      h += 1
    } else {
      StringC(str, " ")
    }
    StringC(str, "\n")
  }
  StringC(str, "").s
}

case class StringC(oldString: String, newString: String) {
  val s = copy(oldString, newString)

  def copy(oldString: String, newString: String): String = new String(oldString.concat(newString))
}

prettyPrint(c, Array(25, 25))

