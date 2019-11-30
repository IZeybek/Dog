package model

import util.Observable


case class Board(boardMap: Map[Int, Cell]) extends Observable {

  //can create a Board with a given size
  def this(size: Int) = {
    this((0 until size).map(i => (i, Cell(i, false, null))).toMap)
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

  def movePlayer(player: Player, pieceNum: Integer, moveBy: Integer): Board = {
    val oldPos: Integer = player.getPosition(pieceNum)

    //set old Cell unoccupied
    var nBoard: Map[Int, Cell] = boardMap.updated(oldPos, boardMap(oldPos).copy(filled = false, player = null))

    //set new Pos as occupied
    nBoard = nBoard.updated(oldPos + moveBy, boardMap(oldPos + moveBy).copy(filled = true, player = player))
    copy(boardMap = nBoard)
  }

  def swapPlayers(player: Array[Player], playerNums: List[Int], pieceNums: List[Int]): Board = {

    val p: Player = player(playerNums(0))
    val swapPlayer: Player = player(playerNums(1))

    //set cell to swapPlayer
    var nBoard: Map[Int, Cell] = boardMap.updated(p.getPosition(pieceNums(0)), boardMap(p.getPosition(pieceNums(0))).copy(player = swapPlayer))

    //set cell to player
    nBoard = nBoard.updated(swapPlayer.getPosition(pieceNums(1)), boardMap(swapPlayer.getPosition(1)).copy(player = p))
    copy(boardMap = nBoard)
  }

  def checkOverrideOtherPlayer(player: Player, pieceNum: Integer, moveBy: Integer): Boolean = {
    boardMap(moveBy + player.getPosition(pieceNum)).isFilled
  }

  def getPlayerColor(pos: Integer): String = {
    boardMap(pos).player.color
  }

}

