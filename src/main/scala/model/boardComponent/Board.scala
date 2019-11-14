package model.boardComponent

import model.playerComponent.card._

import scala.util.Random

case class Board(size: Int) {

  val boardMap: Map[Int, Cell] = createBoard(size)

  def createBoard(size: Int): Map[Int, Cell] = {

    var boardMap = Map(0 -> Cell(false))

    for {
      i <- 0 until (size * 16 - 1)
    } boardMap += (i -> Cell(false))

    boardMap
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

  def onePlayerBoard(offset: Int): Array[Array[Int]] = {
    val array = Array.ofDim[Int](7, 8)
    for (i <- 0 to 6; j <- 0 to 7) {
      array(i)(j) = -1
    }

    array(0)(7) = 0 + offset
    array(1)(6) = 1 + offset

    array(2)(5) = 2 + offset
    array(3)(5) = 3 + offset
    array(4)(5) = 4 + offset
    array(5)(5) = 5 + offset
    array(6)(5) = 6 + offset

    array(6)(4) = 7 + offset
    array(6)(3) = 8 + offset
    array(6)(2) = 9 + offset
    array(6)(1) = 10 + offset

    array(5)(1) = 11 + offset
    array(4)(1) = 12 + offset
    array(3)(1) = 13 + offset
    array(2)(1) = 14 + offset
    array(1)(0) = 15 + offset
    array
  }

  override def toString(): String = {

    val board1 = onePlayerBoard(0)
    val board2 = onePlayerBoard(16)
    val board3 = onePlayerBoard(32)
    val board4 = onePlayerBoard(48)

    for (y <- 6 to 0 by -1) {
      print(" . " * 6)
      for (x <- 7 to 0 by -1) {
        if (board1(y)(x) != -1) print("[" + getBoardMap.get(board1(y)(x)).toString + "]")
        else print(" . ")
      }
      if (y != 0) print(" . " * 7 + "\n")
      else {for (y <- 0 to 6) {
        if (board2(y)(7) != -1) print("[" + getBoardMap.get(board2(y)(7)).toString  + "]")
        else print(" . ")
      }
        println()
      }
    }

    for (x <- 0 to 7) {

      for (y <- 6 to 0 by -1) {
        if (board4(y)(x) != -1) print("[" +  getBoardMap.get(board4(y)(x)).toString  + "]")
        else print(" . ")
      }

      if (x != 7) {
        print(" . " * 7)
        for (y <- 0 to 6) {
          if (x < 7 && board2(y)(6 - x) != -1) print("[" + getBoardMap.get(board2(y)(6 - x)).toString  + "]")
          else print(" . ")
        }
      } else {

        for(x <- 0 to 7) {
          if (board3(0)(x) != -1) print("[" + getBoardMap.get(board3(0)(x)).toString + "]")
          else print(" . ")
        }
        println(" . " * 6)
      }
      if(x != 7)println(" ")
    }

    for (y <- 1 to 6) {
      print(" . " * 7)
      for (x <- 0 to 7) {
        if (board3(y)(x) != -1) print("[" + getBoardMap.get(board3(y)(x)).toString + "]")
        else print(" . ")
      }
      print(" . " * 6 + "\n")
    }
    ""
  }
}
