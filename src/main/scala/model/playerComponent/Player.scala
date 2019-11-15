package model


case class Player(name: String, color: String, piece: Map[Int, Piece]) {

  //  var piecesOnBoard: Int = 0
  var piecesOnStandby: Int = 4

  def getColor: String = color

  override def toString: String = name

  def getPiece: Map[Int, Piece] = piece //indexing & mapping pieces
}

class Piece(var position: Int) {
  def setPosition(pos: Int): Unit = {
    position = pos + position
  }
  def getPosition: Int = position
}

