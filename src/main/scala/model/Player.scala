package model


case class Player(name: String, color: String, piece: Map[Int, Piece], inHouse: Int) {

  def getColor: String = color

  def getPiece: Map[Int, Piece] = piece //indexing & mapping pieces

  def movePlayer(pieceNum: Integer, moveBy: Integer): Player = {
    val oldPos = piece(pieceNum).position
    copy(piece = piece.updated(pieceNum, piece(pieceNum).copy(piece(pieceNum).position + moveBy)), inHouse = if (oldPos == 0) inHouse - 1 else inHouse)
  }

  def overridePlayer(pieceNum: Integer): Player = {
    copy(piece = piece.updated(pieceNum, piece(pieceNum).copy(0)), inHouse = inHouse + 1)
  }

  override def toString: String = name
}

case class Piece(var position: Int) {
  def getPosition: Int = position
}

