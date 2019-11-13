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


  override def toString(): String = {

    var box = "\n"
    for {
      i <- 0 until size

    } box += box.replaceFirst("x", boardMap.get(i).toString)
    box
  }
}
