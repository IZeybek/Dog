package model

import model.CardComponent.Card


case class Player(name: String, color: String, piece: Map[Int, Piece], inHouse: Int, cardList: List[Card]) {

  def this(name: String, color: String, pieceNumber: Int) = {
    this(name, color = color, (0 to pieceNumber).map(i => (i, Piece(0))).toMap, inHouse = 4, null)
  }

  def getPosition(pieceNum: Int): Int = piece(pieceNum).position

  /**
   * @return the furthest position of player
   *         1. Int: Position
   *         2. Int: pieceNum
   */
  def getFurthestPosition: (Int, Int) = {
    var max: (Int, Int) = (0, 0)
    val updateMax = (pos: Int, pieceNum: Int) => if (pos > max._1) max = (pos, pieceNum)
    piece.foreach(x => updateMax(x._2.position, x._1))
    print(s"max position of $color is $max\n")
    max
  }

  def getPieceNum(position: Int): Int = {
    piece.foreach(x => if (x._2.position == position) {
      return x._1
    })
    -1
  }

  def overridePlayer(pieceNum: Int): Player = {
    copy(piece = piece.updated(pieceNum, piece(pieceNum).setPosition(0)), inHouse = inHouse + 1)
  }

  def movePlayer(pieceNum: Int, moveBy: Int): Player = {
    val oldPos = getPosition(pieceNum)
    copy(piece = piece.updated(pieceNum, piece(pieceNum).movePiece(moveBy)), inHouse = {
      if (oldPos == 0 && moveBy > 0) inHouse - 1
      else inHouse
    })
  }

  def swapPiece(pieceNum: Int, newPos: Int): Player = {
    copy(piece = piece.updated(pieceNum, piece(pieceNum).copy(position = newPos)), inHouse = {
      if (newPos == 0) inHouse + 1
      else if (getPosition(pieceNum) == 0 && getPosition(pieceNum) < newPos) inHouse - 1
      else inHouse
    })
  }


  def removeCard(card: Card): List[Card] = {
    if (cardList.nonEmpty)
      cardList diff List(card)
    else
      Nil
  }

  def setHandCards(myCards: List[Card]): Player = copy(cardList = myCards)

  def getCard(cardNum: Int): Card = {
    if (cardList.nonEmpty)
      cardList(cardNum)
    else
      null
  }


  override def toString: String = name
}


case class Piece(var position: Int) {
  def setPosition(newPosition: Int): Piece = {
    copy(position = newPosition)
  }

  def movePiece(moveBy: Int): Piece = {
    copy(position = position + moveBy)
  }

}

//case class PlayerBuilder() {
//  var pieceNumber: Int = 4
//  var color: String = "blau"
//  var name: String = "Bob"
//
//  def withPieceNumber(pieceNum: Int): Unit = pieceNumber = pieceNum
//
//  def withColor(c: String): Unit = color = c
//
//  def withName(n: String): Unit = name = n
//
//  def build(): Player = {
//    new Player(name, color, pieceNumber)
//  }
//}
