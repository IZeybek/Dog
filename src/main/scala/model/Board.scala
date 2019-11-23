package model

import util.Observable

import scala.io.Source

case class Board(boardMap: Map[Int, Cell]) extends Observable {

  val file = Source.fromFile("src/feld.txt")

  def getBoardMap: Map[Int, Cell] = boardMap


  override def toString: String = {
    var box = ""
    val line_down = "_" * getBoardMap.size * 3 + "\n"
    val line_up = "\n" + "‾" * getBoardMap.size * 3
    box = box + line_down
    for (i <- 0 until getBoardMap.size) {
      box += getBoardMap(i).toString
    }
    box += line_up + "\n"
    box
  }

  def movePlayer(player: Player, pieceNum: Integer, moveBy: Integer): Board = {
    val oldPos: Integer = player.getPiece(pieceNum).getPosition
    var nBoard: Map[Int, Cell] = boardMap.updated(oldPos, boardMap(oldPos).copy(filled = false, player = null))
    nBoard = nBoard.updated(oldPos + moveBy, boardMap(oldPos + moveBy).copy(filled = true, player = player))
    copy(nBoard)
  }

  def overridePlayer(player: Player, pieceNum: Integer, moveBy: Integer): Boolean = {
    boardMap(moveBy + player.piece(pieceNum).position).isFilled
  }

  def getColor(pos: Integer): String = {
    boardMap(pos).player.color
  }
}

