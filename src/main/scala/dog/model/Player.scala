package dog.model

import dog.model.CardComponent.CardTrait
import dog.model.CardComponent.cardBaseImpl.Card

import scala.util.{Failure, Success, Try}


case class Player(nameAndIdx: (String, Int),
                  color: String, piece: Map[Int, Piece],
                  inHouse: List[Int],
                  cardList: List[CardTrait],
                  homePosition: Int) {

  def nextPiece(): Int = {
    inHouse.head
  }

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
      println(x._2.pos)
      return x._1
    })
    -1
  }

  def piecePosition(pieceNum: Int): Int = piece(pieceNum).pos

  def overridePlayer(pieceNum: Int): Player = {
    copy(piece = piece.updated(pieceNum, piece(pieceNum).setPosition(homePosition)), inHouse = pieceNum :: inHouse)
  }

  def setPosition(pieceIdx: Int, newPos: Int): Player = {
    val oldPos = piece(pieceIdx).pos
    copy(piece = piece.updated(pieceIdx, piece(pieceIdx).setPosition(newPos)), inHouse = {
      if (oldPos == homePosition && newPos - oldPos >= 0) inHouse.filter(_ != pieceIdx)
      else inHouse
    })
  }


  def swapPiece(pieceIdx: Int, newPos: Int): Player = {
    copy(piece = piece.updated(pieceIdx, piece(pieceIdx).copy(pos = newPos)), inHouse = {
      if (newPos == homePosition) pieceIdx :: inHouse
      else if (piece(pieceIdx).pos == 0 && piece(pieceIdx).pos < newPos) inHouse.filter(_ != pieceIdx)
      else inHouse
    })
  }

  def setHandCards(myCards: List[CardTrait]): Player = {
    copy(cardList = myCards)
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
      case None => throw new Exception("Es konnte keine Karte ausgewählt werden!\n")
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


case class Piece(pos: Int) {

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
  var inHouse: List[Int] = List(0, 1, 2, 3)

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
      inHouse = (0 until piecesAmount).toList
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
      val player: Player = new Player(name, color, pieces, inHouse, cardsDeck, homePosition)
      reset()
      player
    }
  }

}
