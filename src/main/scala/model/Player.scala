package model

import model.CardComponent.Card


case class Player(name: String, color: String, piece: Map[Int, Piece], inHouse: Int, cardList: List[Card]) {

  def this(name: String, color: String) = {
    this(name, color = color, (0 until 5).map(i => (i, Piece(0))).toMap, inHouse = 4, null)
  }

  def getColor: String = color

  def getPiece: Map[Int, Piece] = piece //indexing & mapping pieces

  def getPosition(pieceNum: Integer): Integer = piece(pieceNum).getPosition

  def overridePlayer(pieceNum: Integer): Player = copy(piece = piece.updated(pieceNum, piece(pieceNum).setPosition(0)), inHouse = inHouse + 1)


  def removeCard(card: Card): List[Card] = cardList diff List(card)

  def setHandCards(myCards: List[Card]): Player = copy(cardList = myCards)


  def getCard(cardNum: Integer): Card = cardList(cardNum)

  def movePlayer(pieceNum: Integer, moveBy: Integer): Player = {

    val oldPos = piece(pieceNum).getPosition
    copy(piece = piece.updated(pieceNum, piece(pieceNum).movePiece(moveBy)), inHouse = if (oldPos == 0) inHouse - 1 else inHouse)
  }


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

