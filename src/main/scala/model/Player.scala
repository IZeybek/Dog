package model


case class Player(name: String, color: String, piece: Map[Int, Piece]) {

  var piecesOnStandby: Int = 4

  def getColor: String = color

  override def toString: String = name

  def getPiece: Map[Int, Piece] = piece //indexing & mapping pieces
}

class Piece(var position: Int) {
  def setPosition(pos: Int): Int = {
    position = pos + position
    position
  }
  def getPosition: Int = position
}

