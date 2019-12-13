package model

import model.CardComponent.CardTrait
import model.CardComponent.cardBaseImpl.CardDeck

import scala.util.{Failure, Success, Try}


case class Player(name: String, c: String, piece: Map[Int, Piece], inHouse: Int, start: Int, cardList: List[CardTrait]) {

  val color: String = {
    c match {
      case "grün" => Console.GREEN
      case "blau" => Console.BLUE
      case "rot" => Console.RED
      case "gelb" => Console.YELLOW
      case _ => ""
    }
  }

  def update(mementoPlayer: Player): Player = copy(inHouse = mementoPlayer.inHouse,
    start = mementoPlayer.start,
    cardList = mementoPlayer.cardList)

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

  def getPosition(pieceNum: Int): Int = piece(pieceNum).position

  def setHandCards(myCards: List[CardTrait]): Player = {
    copy(cardList = myCards)
  }

  def this(name: String, c: String, pieceQuantity: Int, cards: List[CardTrait]) = {
    this(name, c = c, (0 to pieceQuantity).map(i => (i, Piece(0))).toMap, inHouse = 4, 0, cards)
  }

  def removeCard(card: CardTrait): Player = {
    tryRemoveCard(card) match {
      case Some(list) => copy(cardList = list)
      case None => this
    }
  }

  def tryRemoveCard(card: CardTrait): Option[List[CardTrait]] = {
    Try(cardList diff List(card)) match {
      case Success(list) => Some(list)
      case Failure(_) =>
        println("Es konnte keine Karte entfernt werden!\n")
        None
    }
  }

  def getCard(cardNum: Int): CardTrait = {
    tryGetCard(cardNum) match {
      case Some(value) => value
      case None => throw new Exception("Es konnte keine Karte ausgewählt!\n")
    }
  }

  def tryGetCard(cardNum: Int): Option[CardTrait] = {
    Try(cardList(cardNum)) match {
      case Success(value) => Some(value)
      case Failure(_) => None
    }
  }

  override def toString: String = name
}

/*
trait Option[Player] {
  def map(f: Player => Player): Option[Player]
}

case class Some[Player](p: Player) extends Option[Player] {
  override def map(f: Player => Player): Some[Player] = Some(f(p))
}

case class None[Player]() extends Option[Player] {
  override def map(f: Player => Player) = new None
} */


case class Piece(var position: Int) {
  def setPosition(newPosition: Int): Piece = copy(position = newPosition)

  def movePiece(moveBy: Int): Piece = copy(position = position + moveBy)
}

object Player {

  case class PlayerBuilder() {
    var pieceNumber: Int = 4
    var color: String = "blau"
    var name: String = "Bob"
    var amount: Int = 6
    var cardsDeck: List[CardTrait] = CardDeck.apply(List(1, 1))

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

    def withCards(cards: List[CardTrait], amount: Int): PlayerBuilder = {
      cardsDeck = cards
      this
    }

    def build(): Player = {
      new Player(name, color, pieceNumber, cardsDeck)
    }
  }

}
