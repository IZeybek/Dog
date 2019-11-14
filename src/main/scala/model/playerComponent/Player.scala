package model


case class Player(name: String, piece: Piece) {

  var piecesOnBoard: Int = 0
  var piecesOnStandby: Int = 4

  override def toString: String = name
  def getPiece : Piece = piece
}

case class Piece(piece: Array[Int], color: String) {
  def getPieces: Array[Int] = piece



  def getColor: String = color
}

