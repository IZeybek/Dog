package model

import util.Observable


case class Board(boardMap: Map[Int, Cell]) extends Observable {

  //can create a Board with a given size
  def this(size: Int) = {
    this((0 until size).map(i => (i, Cell(i, false, null))).toMap)
  }

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
    print(s"piece $pieceNum is moved")
    val oldPos: Integer = player.getPiece(pieceNum).getPosition
    //set old Cell unoccupied
    var nBoard: Map[Int, Cell] = boardMap.updated(oldPos, boardMap(oldPos).copy(filled = false, player = null))
    //set new Pos as occupied
    nBoard = nBoard.updated(oldPos + moveBy, boardMap(oldPos + moveBy).copy(filled = true, player = player))
    copy(nBoard)
  }

  def checkOverrideOtherPlayer(player: Player, pieceNum: Integer, moveBy: Integer): Boolean = {
    boardMap(moveBy + player.piece(pieceNum).getPosition).isFilled
  }

  def getPlayerColor(pos: Integer): String = {
    boardMap(pos).player.color
  }
}

