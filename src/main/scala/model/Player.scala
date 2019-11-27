package model

import model.CardComponent.Card


case class Player(name: String, color: String, piece: Map[Int, Piece], inHouse: Int, card: List[Card]) {

  def getColor: String = color

  def getPiece: Map[Int, Piece] = piece //indexing & mapping pieces

  def getPosition(pieceNum: Integer): Integer = piece(pieceNum).getPosition

  def movePlayer(pieceNum: Integer, moveBy: Integer): Player = {
    val oldPos = piece(pieceNum).getPosition
    copy(piece = piece.updated(pieceNum, piece(pieceNum).movePiece(moveBy)), inHouse = if (oldPos == 0) inHouse - 1 else inHouse)
  }

  def overridePlayer(pieceNum: Integer): Player = copy(piece = piece.updated(pieceNum, piece(pieceNum).setPosition(0)), inHouse = inHouse + 1)

  def removeCard(card: Card, list: List[Card]): List[Card] = list diff List(card)

  def drawCard(cardNum: Integer): Card = card(cardNum)

  override def toString: String = name
}


case class Piece(var position: Int) {
  def getPosition: Int = position

  def setPosition(newPosition: Integer): Piece = {
    copy(position = newPosition)
  }

  def movePiece(moveBy: Integer): Piece = {
    copy(position = position + moveBy)
  }

}

