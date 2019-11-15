package model


case class Player(name: String, color: String, piece: Map[Int, Piece]) {

  //  var piecesOnBoard: Int = 0
  var piecesOnStandby: Int = 4

  def getColor: String = color

  override def toString: String = name

  def getPiece: Map[Int, Piece] = piece //indexing & mapping pieces
}

case class Piece(position: Int) {
  def getPosition: Int = position
}

