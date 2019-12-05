package model

import util.Observable


case class Board(boardMap: Map[Int, Cell]) extends Observable {

  //can create a Board with a given size
  def this(size: Int) = {
    this((0 until size).map(i => (i, Cell(i, None()))).toMap)
  }

  override def toString: String = {
    var box = ""
    val line_down = "_" * boardMap.size * 3 + "\n"
    val line_up = "\n" + "‾" * boardMap.size * 3
    box = box + line_down
    for (i <- 0 until boardMap.size) {
      box += boardMap(i).toString
    }
    box += line_up + "\n"
    box
  }

  def movePlayer(player: Player, pieceNum: Integer, setPos: Integer): Board = {
    val oldPos: Integer = player.getPosition(pieceNum)

    //set old Cell unoccupied
    var
    nBoard: Map[Int, Cell] = boardMap.updated(oldPos, boardMap(oldPos).removePlayerFromCell)
    //set new Pos as occupied
    nBoard = nBoard.updated(setPos, boardMap(setPos).addPlayerToCell(p = player))
    copy(boardMap = nBoard)
  }

  def swapPlayers(player: Array[Player], playerNums: List[Int], pieceNums: List[Int]): Board = {

    val p: Player = player(playerNums(0))
    val swapPlayer: Player = player(playerNums(1))

    //set cell to swapPlayer
    var nBoard: Map[Int, Cell] = boardMap.updated(p.getPosition(pieceNums.head), boardMap(p.getPosition(pieceNums.head)).addPlayerToCell(swapPlayer))

    //set cell to player
    nBoard = nBoard.updated(swapPlayer.getPosition(pieceNums(1)), boardMap(swapPlayer.getPosition(pieceNums(1))).addPlayerToCell(p))
    copy(boardMap = nBoard)
  }

  def checkOverrideOtherPlayer(player: Player, pieceNum: Integer, newPos: Integer): Boolean = {
    boardMap(newPos).p match {
      case Some(_) => true
      case None() => false
    }
  }

  def getPlayerColor(pos: Integer): String = {
    boardMap(pos).p match {
      case Some(p) => p.c
      case None() => "NO COLOR"
    }
  }

}

