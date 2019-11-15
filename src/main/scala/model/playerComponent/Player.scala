package model


case class Player(name: String, piece: Map[Int, Piece]) {

  //  var piecesOnBoard: Int = 0
  var piecesOnStandby: Int = 4

  override def toString: String = name

  def getPiece: Map[Int, Piece] = piece //indexing & mapping pieces
}

case class Piece(position: Int, color: String) {
  def getPosition: Int = position
  def getColor: String = color
}

