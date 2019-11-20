package model

import util.Observable

import scala.io.Source

case class Board(boardMap: Map[Int, Cell]) extends Observable {

  val file = Source.fromFile("src/feld.txt")
  val arrayOutput = initArrayOutput

  def getArrayOutput: Array[Array[Cell]] = arrayOutput

  def initArrayOutput: Array[Array[Cell]] = {
    val array = Array.ofDim[Cell](21, 21)
    var x = 0
    var y = 0

    for (line <- file.getLines) {

      for (myVlaue <- line.split("\\s+")) {
        if (myVlaue.size > 2) {
          x += myVlaue.size
        } else {
          array(y)(x) = getBoardMap(myVlaue.toInt)
          x += 1;
        }
      }
      y += 1
      x = 0;
    }
    array
  }

  def getBoardMap: Map[Int, Cell] = boardMap


  override def toString(): String = {
    var box = ""
    val line_down = "_" * getBoardMap.size * 3 + "\n"
    var line_up = "\n" + "‾" * getBoardMap.size * 3
    box = box + line_down
    for (i <- 0 until getBoardMap.size) {
      box += getBoardMap(i).toString
    }
    box += line_up + "\n"
    box
  }

  def makeString(): String = {
    var box = ""
    //output as an array!
    for (i <- 0 to 20) {
      for (j <- 0 to 20) {
        if (arrayOutput(i)(j) != null) box += arrayOutput(i)(j).toString
        else box += " - "
      }
      box += "\n"
    }
    box
  }
}
