package dog.model

import dog.model.CardComponent.CardTrait
import dog.model.CardComponent.cardBaseImpl.Card

import scala.util.{Failure, Success, Try}


case class Player(name: String, color: String, piece: Map[Int, Piece], inHouse: Int, start: Int, cardList: List[CardTrait]) {

  val consoleColor: String = {

    color match {
      case "grün" => Console.GREEN
      case "blau" => Console.BLUE
      case "rot" => Console.RED
      case "gelb" => Console.YELLOW
      case _ => ""
    }
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

  def setPosition(pieceNum: Int, newPos: Int): Player = {
    val oldPos = getPosition(pieceNum)
    copy(piece = piece.updated(pieceNum, piece(pieceNum).setPosition(newPos)), inHouse = {
      if (oldPos == start && newPos - oldPos > 0) inHouse - 1
      else inHouse
    })
  }

  def getPosition(pieceNum: Int): Int = piece(pieceNum).position

  def swapPiece(pieceNum: Int, newPos: Int): Player = {
    copy(piece = piece.updated(pieceNum, piece(pieceNum).copy(position = newPos)), inHouse = {
      if (newPos == 0) inHouse + 1
      else if (getPosition(pieceNum) == 0 && getPosition(pieceNum) < newPos) inHouse - 1
      else inHouse
    })
  }

  def setHandCards(myCards: List[CardTrait]): Player = {
    copy(cardList = myCards)
  }

  def this(name: String, c: String, pieceQuantity: Int, cards: List[CardTrait]) = {
    this(name, color = c, (0 to pieceQuantity).map(i => (i, Piece(0))).toMap, inHouse = 4, 0, cards)
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


case class Piece(var position: Int) {
  def setPosition(newPosition: Int): Piece = copy(position = newPosition)

  def movePiece(moveBy: Int): Piece = copy(position = position + moveBy)
}

object Player {
  var pieceNumber: Int = 4
  var color: String = "blau"
  var name: String = "Bob"
  var cardsDeck: List[CardTrait] = Card.RandomCardsBuilder().withAmount(6).buildRandomCardList

  case class PlayerBuilder() {

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

    def withCards(cards: List[CardTrait]): PlayerBuilder = {
      cardsDeck = cards
      this
    }

    def withGeneratedCards(setAmount: Int): PlayerBuilder = {
      cardsDeck = Card.RandomCardsBuilder().withAmount(setAmount).buildRandomCardList
      this
    }

    def build(): Player = {
      new Player(name, color, pieceNumber, cardsDeck)
    }
  }

}
