package dog.model

import dog.model.CardComponent.CardTrait
import dog.model.CardComponent.cardBaseImpl.Card

import scala.util.{Failure, Success, Try}


case class Player(nameAndIdx: (String, Int),
                  color: String, piece: Map[Int, Piece],
                  inHouse: Int, start: Int,
                  cardList: List[CardTrait],
                  homePosition: Int) {

  val consoleColor: String = {

    color match {
      case "green" => Console.GREEN
      case "white" => Console.WHITE
      case "red" => Console.RED
      case "yellow" => Console.YELLOW
      case _ => ""
    }
  }

  def getPieceNum(position: Int): Int = {
    piece.foreach(x => if (x._2.pos == position) {
      return x._1
    })
    -1
  }

  def overridePlayer(pieceNum: Int): Player = {
    copy(piece = piece.updated(pieceNum, piece(pieceNum).setPosition(this.homePosition)), inHouse = inHouse + 1)
  }

  def setPosition(pieceNum: Int, newPos: Int): Player = {
    val oldPos = piece(pieceNum).pos
    copy(piece = piece.updated(pieceNum, piece(pieceNum).setPosition(newPos)), inHouse = {
      if (oldPos == start && newPos - oldPos > 0) inHouse - 1
      else inHouse
    })
  }

  //  def getPosition(pieceNum: Int): Int = {
  //    piece(pieceNum).position
  //  }

  def swapPiece(pieceNum: Int, newPos: Int): Player = {
    copy(piece = piece.updated(pieceNum, piece(pieceNum).copy(pos = newPos)), inHouse = {
      if (newPos == 0) inHouse + 1
      else if (piece(pieceNum).pos == 0 && piece(pieceNum).pos < newPos) inHouse - 1
      else inHouse
    })
  }

  def setHandCards(myCards: List[CardTrait]): Player = {
    copy(cardList = myCards)
  }

  def this(name: (String, Int), c: String, pieces: Map[Int, Piece], cards: List[CardTrait], homePosition: Int) = {
    this(name, color = c, piece = pieces, inHouse = pieces.size, 0, cards, homePosition)
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

  override def toString: String = nameAndIdx._1
}


case class Piece(var pos: Int) {

  def setPosition(newPosition: Int): Piece = copy(pos = newPosition)

  def movePiece(moveBy: Int): Piece = copy(pos = pos + moveBy)
}

object Player {
  var pieceAmount: Int = 4
  var color: String = "blue"
  var name: (String, Int) = ("Bob", 0)
  var cardsDeck: List[CardTrait] = Card.RandomCardsBuilder().withAmount(6).buildRandomCardList
  var pieces: Map[Int, Piece] = (0 until pieceAmount).map(i => (i, Piece(0))).toMap
  var homePosition = 0

  def reset(): Unit = {
    pieceAmount = 4
    color = "blue"
    name = ("Bob", 0)
    cardsDeck = Card.RandomCardsBuilder().withAmount(6).buildRandomCardList
    pieces = (0 until pieceAmount).map(i => (i, Piece(0))).toMap
  }

  case class PlayerBuilder() {

    def withPiece(piecesAmount: Int, homePos: Int): PlayerBuilder = {
      pieceAmount = piecesAmount
      homePosition = homePos
      pieces = (0 until piecesAmount).map(i => (i, Piece(homePos))).toMap
      this
    }

    //
    //    def withPieces(setPieces: Map[Int, Piece]): PlayerBuilder = {
    //      pieces = setPieces
    //      pieceNumber = pieces.size
    //      this
    //    }

    def withColor(c: String): PlayerBuilder = {
      color = c
      this
    }

    def withName(n: (String, Int)): PlayerBuilder = {
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
      val player: Player = new Player(name, color, pieces, cardsDeck, homePosition)
      reset()
      player
    }
  }

}
