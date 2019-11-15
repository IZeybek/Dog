package model


class Player(name: String, piece: Map[Integer, Piece]) {

  //  var piecesOnBoard: Int = 0
  var piecesOnStandby: Int = 4

  override def toString: String = name

  def getPiece: Map[Integer, Piece] = piece //indexing & mapping pieces
}

case class Piece(index: Integer, color: String) {
  def getIndex: Integer = index
  def getColor: String = color
}

