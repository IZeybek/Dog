package model

import model.CardComponent.{Card, CardDeck}


case class Player(name: String, c: String, piece: Map[Int, Piece], inHouse: Int, start: Int, cardList: List[Card]) {

  def update(mementoPlayer: Player): Player = copy(inHouse = mementoPlayer.inHouse,
    start = mementoPlayer.start,
    cardList = mementoPlayer.cardList)


  val color: String = {
    c match {
      case "grün" => Console.GREEN
      case "blau" => Console.BLUE
      case "rot" => Console.RED
      case "gelb" => Console.YELLOW
      case _ => ""
    }
  }


  def getPosition(pieceNum: Int): Int = piece(pieceNum).position


  def getPieceNum(position: Int): Int = {
    piece.foreach(x => if (x._2.position == position) {
      return x._1
    })
    -1
  }

  def overridePlayer(pieceNum: Int): Player = {
    copy(piece = piece.updated(pieceNum, piece(pieceNum).setPosition(0)), inHouse = inHouse + 1)
  }

  def setPosition(pieceNum: Int, newPos: Int): Player = {
    val oldPos = getPosition(pieceNum)
    copy(piece = piece.updated(pieceNum, piece(pieceNum).setPosition(newPos)), inHouse = {
      if (oldPos == start && newPos - oldPos > 0) inHouse - 1
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


  def setHandCards(myCards: List[Card]): Player = copy(cardList = myCards)

  def this(name: String, c: String, pieceQuantity: Int, cards: List[Card]) = {
    this(name, c = c, (0 to pieceQuantity).map(i => (i, Piece(0))).toMap, inHouse = 4, 0, cards)
  }

  def removeCard(card: Card): List[Card] = {
    if (cardList.nonEmpty)
      cardList diff List(card)
    else
      Nil
  }

  def getCard(cardNum: Int): Card = {
    if (cardList.nonEmpty)
      cardList(cardNum)
    else
      null
  }

  override def toString: String = name
}


trait Option[Player] {
  def map(f: Player => Player): Option[Player]
}

case class Some[Player](p: Player) extends Option[Player] {
  override def map(f: Player => Player): Some[Player] = Some(f(p))
}

case class None[Player]() extends Option[Player] {
  override def map(f: Player => Player) = new None
}


case class Piece(var position: Int) {
  def setPosition(newPosition: Int): Piece = copy(position = newPosition)

  def movePiece(moveBy: Int): Piece = copy(position = position + moveBy)
}

object Player {

  case class PlayerBuilder() {
    var pieceNumber: Int = 4
    var color: String = "blau"
    var name: String = "Bob"
    var cardsDeck: List[Card] = CardDeck.apply()

    def withPieceNumber(pieceNum: Int): PlayerBuilder = {
      pieceNumber = pieceNum
      this
    }

    def withColor(c: String): PlayerBuilder = {
      color = c
      this
    }

    def withName(n: String): PlayerBuilder = {
      name = n
      this
    }

    def withCards(cards: List[Card]): PlayerBuilder = {
      cardsDeck = cards
      this
    }

    def build(): Player = {
      new Player(name, color, pieceNumber, cardsDeck)
    }
  }

}
