package model


case class Player(name: String, color: String, piece: Map[Int, Piece], inHouse: Int) {

  def getColor: String = color

  override def toString: String = name

  def getPiece: Map[Int, Piece] = piece //indexing & mapping pieces
}

case class Piece(var position: Int) {
  def getPosition: Int = position
}

