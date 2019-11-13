package model.boardComponent

import model.playerComponent.card._

import scala.io.Source
import scala.util.Random

case class Board(xy: Array[Int], cells: Array[Cell]) {

  def replaceCell(pos: Int, cell: Cell): Board = {
    copy(xy, cells.updated(pos, cell))
  }

  def createRandomCard(): CardTrait = {
    Random.nextInt(2) match {
      case 0 => ChangeCard()
      case 1 => JokerCard()
      case 2 => SevenCard()
      case _ => null
    }
  }

  def prettyPrint(): Unit = {
    val x = xy(0)
    val y = xy(1)
    var j = 0
    var cellPos = 0

    val checkPos = (i: Int, j: Int, c: Array[Cell]) => i == c(cellPos).xy(1) && j == c(cellPos).xy(0)

    for (i <- 0 until y) {
      for (j <- 0 until x) {
        if (cells.last.absPos + 1 != cellPos && checkPos(i, j, cells)) {
          print(Console.BLUE + "i" + Console.WHITE)
          cellPos += 1
        } else
          print(" ")
      }
      println(" ")
      j = 0
    }
  }

}

object Read {

  def readIn(path: String): Board = {
    val cells = scala.collection.mutable.ArrayBuffer.empty[Cell]
    val file = Source.fromFile(path)
    var z = 0
    var x = 0
    var y = 0

    val buildArray = (c: Char) => {
      if (c == '1') {
        cells += Cell(z, Array(x, y), true)
        z += 1
      }
      x += 1
    }

    val forEachString = (s: String) => {
      x = 0
      s.foreach(buildArray)
      y += 1
    }

    file.getLines().foreach(forEachString)
    Board(Array(x, y), cells.toArray)
  }
}
