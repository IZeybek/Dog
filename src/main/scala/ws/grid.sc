import scala.collection.mutable
import scala.io.Source

case class Cell(xy: Array[Int], filled: Boolean) {
  def isFilled: Boolean = filled

  def fill(filled: Boolean): Boolean = filled
}

//read 2D Board in 1D array
val cells = mutable.SortedMap[Integer, Cell]()
val file = Source.fromFile("C:\\Users\\ismoz\\Documents\\00_GitHub\\Dog\\src\\feld.txt")
var x = 0;
var y = 0;
var z = 0

val buildMap = (c: Char) => {
  if (c == '1') {
    cells.put(z, Cell(Array(x, y), true))
    print(f"($z ${cells(z).xy(0)} ${cells(z).xy(1)})\t")
    z += 1
  }
  x += 1
}

val forEachString = (s: String) => {
  x = 0

  y += 1
}
for (line <- file.getLines) {
  var s = line.split("\\s+")
  for (myVlaue <- s) {
    if (myVlaue.size > 2 ) {
      x += myVlaue.size - 1
    }else{

    }
  }

}
val c = cells.toMap

//val m = Map(0 -> Cell(Array(0, 0), false), 1 -> Cell(Array(0, 1), false), 2 -> Cell(Array(1, 0), false), 3 -> Cell(Array(1, 1), false))


val lineseparator = "+" + ("-" * 5) * 5 + "+\n"
val line = "|" + (" " * 5) * 5 + "|\n"
var box = lineseparator + (line * 5) * 5 + lineseparator

def rowLineToIndex(row: Int, line: Int): Integer = {
  row * 28 + line
}

z = 0

def replace(row: Integer, line: Integer): String = {
  if (z < 72 && c(z).xy(0) == row && c(z).xy(1) == line) {
    val i = rowLineToIndex(row, line)
    box = box.substring(0, i) + "x" + box.substring(i + 1)
    z += 1
  }
  box
}

for {
  line <- 0 until 28
  row <- 0 until 28
} replace(row, line)

box
