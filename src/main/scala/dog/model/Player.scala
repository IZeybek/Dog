package dog.model

import dog.model.BoardComponent.BoardTrait
import dog.model.BoardComponent.boardAdvancedImpl.Board
import dog.model.CardComponent.CardTrait
import dog.model.CardComponent.cardBaseImpl.Card

import scala.util.{Failure, Success, Try}


case class Player(nameAndIdx: (String, Int),
                  color: String, piece: Map[Int, Piece],
                  inHouse: List[Int], garage: BoardTrait,
                  cardList: List[CardTrait],
                  homePosition: Int) {

  def nextPiece(): Int = {

    Try(inHouse.head) match {
      case Success(iH) => iH
      case _ => -1
    }
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
    piece.foreach(x => if (x._2.pos == position)
      return x._1)
    -1
  }

  def piecePosition(pieceNum: Int): Int = piece(pieceNum).pos

  def overridePlayer(pieceNum: Int): Player = {
    copy(piece = piece.updated(pieceNum, piece(pieceNum).copy(pos = homePosition)), inHouse = pieceNum :: inHouse)
  }

  def setPosition(pieceIdx: Int, newPos: Int): Player = {
    val oldPos: Int = piece(pieceIdx).pos
    copy(piece = piece.updated(pieceIdx, piece(pieceIdx).copy(pos = newPos)), inHouse = {
      if (oldPos == homePosition && newPos - oldPos >= 0)
        inHouse.filter(_ != pieceIdx)
      else
        inHouse
    })
  }

  def setNewGaragePosition(pieceIdx: Int, newPos: Int): Player = copy(piece = piece.updated(pieceIdx, piece(pieceIdx).copy(pos = newPos)), garage = garage.fill(garage.cell(newPos).addPlayerToCell(this), newPos))


  def swapPiece(pieceIdx: Int, newPos: Int): Player = {
    copy(piece = piece.updated(pieceIdx, piece(pieceIdx).copy(pos = newPos)), inHouse = {
      if (newPos == homePosition)
        pieceIdx :: inHouse
      else if (piece(pieceIdx).pos == 0 && piece(pieceIdx).pos < newPos)
        inHouse.filter(_ != pieceIdx)
      else
        inHouse
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

  def getCard(cardIdx: Int): CardTrait = {
    tryGetCard(cardIdx) match {
      case Some(value) => value
      case None => throw new Exception("Es konnte keine Karte ausgewählt werden!\n")
    }
  }

  def tryGetCard(cardIdx: Int): Option[CardTrait] = {
    Try(cardList(cardIdx)) match {
      case Success(value) => Some(value)
      case Failure(_) => None
    }
  }

  override def toString: String = nameAndIdx._1

  def toStringColor: String = s"$consoleColor$toString${Console.RESET}"
}


case class Piece(pos: Int) {

  def movePiece(moveBy: Int): Piece = copy(pos = pos + moveBy)
}

object Player {
  var pieceAmount: Int = 4
  var color: String = "blue"
  var name: (String, Int) = ("Bob", 0)
  var cardsDeck: List[CardTrait] = Card.RandomCardsBuilder().withAmount(6).buildRandomCardList
  var homePosition = 0
  var pieces: Map[Int, Piece] = (0 until pieceAmount).map(i => (i, Piece(homePosition))).toMap
  var inHouse: List[Int] = List(0, 1, 2, 3)
  var garage: BoardTrait = new Board(inHouse.size)

  def reset(): Unit = {
    pieceAmount = 4
    color = "blue"
    name = ("Bob", 0)
    cardsDeck = Card.RandomCardsBuilder().withAmount(6).buildRandomCardList
    pieces = (0 until pieceAmount).map(i => (i, Piece(homePosition))).toMap
  }

  case class PlayerBuilder() {

    def withPiece(piecesAmount: Int, homePos: Int): PlayerBuilder = {
      pieceAmount = piecesAmount
      homePosition = homePos
      pieces = (0 until piecesAmount).map(i => (i, Piece(homePos))).toMap
      inHouse = (0 until piecesAmount).toList
      garage = new Board(inHouse.size)
      this
    }

    def withPieces(setPieces: Map[Int, Piece], setHomePosition: Int): PlayerBuilder = {
      pieces = setPieces
      pieceAmount = pieces.size
      homePosition = setHomePosition
      this
    }

    def withPieces(setPieces: Map[Int, Piece]): PlayerBuilder = {
      pieces = setPieces
      pieceAmount = pieces.size
      this
    }

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

    // can be used for testing or a player with explicit garage
    def withGarage(board: BoardTrait): PlayerBuilder = {
      garage = board
      this
    }

    def build(): Player = {
      val player: Player = new Player(name, color, pieces, inHouse, garage, cardsDeck, homePosition)
      reset()
      player
    }
  }

}
