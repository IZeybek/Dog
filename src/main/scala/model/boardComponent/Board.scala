package model.boardComponent

import model.playerComponent.card._

import scala.io.Source
import scala.util.Random

case class Board(size: Int) {


  val boardMap: Map[Int, Cell] = createBoard(size)
  val file = Source.fromFile("src/feld.txt")
  val arrayOutput = initArrayOutput

  def createBoard(size: Int): Map[Int, Cell] = {

    var boardMap = Map(0 -> Cell(false))

    for {
      i <- 0 until (size * 16)
    } boardMap += (i -> Cell(false))

    boardMap
  }

  def initArrayOutput: Array[Array[Cell]] = {
    val array = Array.ofDim[Cell](21, 21)
    var x = 0
    var y = 0

    for (line <- file.getLines) {

      for (myVlaue <- line.split("\\s+")) {
        if (myVlaue.size > 2) {
          x += myVlaue.size
        } else {
          array(y)(x) = boardMap(myVlaue.toInt)
          x += 1;
        }
      }
      y += 1
      x = 0;
    }
    array
  }

  //  def replaceCell(pos: Int, cell: Cell): Board = {
  //    copy(xy, cells.updated(pos, cell))
  //  }

  def createRandomCard(): CardTrait = {
    Random.nextInt(2) match {
      case 0 => ChangeCard()
      case 1 => JokerCard()
      case 2 => SevenCard()
      case _ => null
    }
  }

  def getBoardMap: Map[Int, Cell] = boardMap


  override def toString(): String = {

    //output as an array!
    for (i <- 0 to 20) {
      for (j <- 0 to 20) {
        if (arrayOutput(i)(j) != null) print(arrayOutput(i)(j).toString)
        else print(" -- ")
      }
      println("")
    }
    ""
  }
}
