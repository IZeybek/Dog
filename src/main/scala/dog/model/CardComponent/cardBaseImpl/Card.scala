package dog.model.CardComponent.cardBaseImpl

import dog.model.BoardComponent.BoardTrait
import dog.model.CardComponent.{CardDeckTrait, CardTrait}
import dog.model.Player

import scala.collection.mutable.ListBuffer
import scala.util.Random


case class Card(symbol: String, task: String, color: String) extends CardTrait {

  override def toString: String = {
    "Card(" + s"${
      getColor match {
        case "blue" => Console.BLUE;
        case "red" => Console.RED
      }
    }$getSymbol${Console.RESET})"
  }

  override def getSymbol: String = symbol

  override def getColor: String = color

  override def getTask: String = task
}


object CardLogic {

  val move: (Vector[Player], BoardTrait, List[Int], List[Int], Int) => (BoardTrait, Vector[Player], Int) = (player: Vector[Player], board: BoardTrait, selectedPlayerIndices: List[Int], pieceNum: List[Int], moveBy: Int) => {

    var isValid: Int = 0
    //move piece of specific player by returning a copy of the piece to the copy constructor player and returning a copy of the player
    val players: Vector[Player] = player
    var finalPlayer: Vector[Player] = Vector.empty[Player]
    val p: Player = player(selectedPlayerIndices.head)
    val newPos: Int = Math.floorMod(moveBy + p.getPosition(pieceNum.head), board.getBoardMap.size)

    //overriding player
    if (board.checkOverrideOtherPlayer(p, pieceNum.head, newPos)) {

      //get indexes and pieces
      val oPlayerIdx: Int = players.indexWhere(x => x.color == board.getBoardMap(newPos).getColor)
      val oPlayerPieceNum: Int = players(oPlayerIdx).getPieceNum(newPos) //get piece of other Player

      //check whether move valid or not
      if (oPlayerPieceNum == -1) isValid = -1

      //update Vector when overridden
      val overriddenPlayers: Vector[Player] = players.updated(oPlayerIdx, players(oPlayerIdx).overridePlayer(oPlayerPieceNum))
      finalPlayer = overriddenPlayers.updated(selectedPlayerIndices.head, players(selectedPlayerIndices.head).setPosition(pieceNum.head, newPos))
    } else {

      //update Vector when not overridden
      finalPlayer = players.updated(selectedPlayerIndices.head, players(selectedPlayerIndices.head).setPosition(pieceNum.head, newPos))
    }

    (board.updateMovePlayer(p, pieceNum.head, newPos), finalPlayer, 0)
  }


  val swap: (Vector[Player], BoardTrait, List[Int], List[Int], Int) => (BoardTrait, Vector[Player], Int) = (player: Vector[Player], board: BoardTrait, selectedPlayerIndices: List[Int], pieceNums: List[Int], moveBy: Int) => {

    var isValid = 0
    //swap a piece of the player that uses the card with the furthest piece of another player
    val p: Player = player(selectedPlayerIndices.head)
    val swapPlayer: Player = player(selectedPlayerIndices(1))
    val swapPos: (Int, Int) = (p.getPosition(pieceNums.head), swapPlayer.getPosition(pieceNums(1)))

    if (swapPos._2 == 0) isValid = -1 //Second Player is not on the field

    val playerOneSwapped: Vector[Player] = player.updated(selectedPlayerIndices.head, p.swapPiece(pieceNums.head, swapPos._2)) //swap with second player
    val playerTwoSwapped: Vector[Player] = playerOneSwapped.updated(selectedPlayerIndices(1), swapPlayer.swapPiece(pieceNums(1), swapPos._1)) //swap with first player

    val nBoard: BoardTrait = board.updateSwapPlayers(playerTwoSwapped, selectedPlayerIndices, pieceNums)

    (nBoard, playerTwoSwapped, isValid)
  }


  def setStrategy(callback: (Vector[Player], BoardTrait, List[Int], List[Int], Int) => (BoardTrait, Vector[Player], Int), player: Vector[Player], board: BoardTrait, playerNum: List[Int], pieceNums: List[Int], moveBy: Int): (BoardTrait, Vector[Player], Int) = {
    callback(player, board, playerNum, pieceNums, moveBy)
  }

  def getLogic(mode: String): (Vector[Player], BoardTrait, List[Int], List[Int], Int) => (BoardTrait, Vector[Player], Int) = {
    mode match {
      case "move" => move
      case "swap" => swap
      //      case "start" => Nil
      //      case "forwardBackward" => Nil
      case _ => move
    }
  }
}


object Card {
  var amount = 6

  case class RandomCardsBuilder() {

    def withAmount(setAmount: Int): RandomCardsBuilder = {
      amount = setAmount
      this
    }

    def buildRandomCardList: List[CardTrait] = {
      val shuffledCardList: List[CardTrait] = Random.shuffle(CardDeck.CardDeckBuilder().withAmount(List(amount * 2, amount * 2)).buildCardList)
      shuffledCardList.take(amount)
    }
  }

}

object GenCardDeck {
  def apply(typ: String): CardDeckTrait = typ match {
    case "special" => SpecialCardsDeck()
    case "normal" => NormalCardsDeck()
  }
}

object CardDeck {
  var cardDeck: Vector[CardTrait] = GenCardDeck.apply("special").getCardDeck.toVector
  var lengthOfCardDeck: Int = cardDeck.length
  var amount: List[Int] = List(2, 2)

  def toStringCardDeck(cardDeck: (Vector[CardTrait], Int)): String = {
    var cardString: String = "________DECK________\n"
    cardDeck._1.indices.foreach(i => if (i < cardDeck._2) cardString += s"$i: ${cardDeck._1(i)}\n") + "\n"
    cardString
  }

  case class CardDeckBuilder() {

    def withAmount(setAmount: List[Int]): CardDeckBuilder = {
      amount = setAmount
      withCardDeck
      this
    }

    def withCardDeck: CardDeckBuilder = {
      val list = new ListBuffer[CardTrait]()
      amount.indices.foreach(f = i => for (_ <- 0 until amount(i)) {
        i match {
          case 0 => list.++=(GenCardDeck.apply("special").getCardDeck)
          case 1 => list.++=(GenCardDeck.apply("normal").getCardDeck)
        }
      })
      cardDeck = list.toVector
      lengthOfCardDeck = cardDeck.length
      this
    }

    def withShuffle: CardDeckBuilder = {
      val shuffledCardDeck: Vector[CardTrait] = Random.shuffle(cardDeck)
      cardDeck = shuffledCardDeck
      this
    }

    def buildCardVectorWithLength: (Vector[CardTrait], Int) = (cardDeck, lengthOfCardDeck)

    def buildCardVector: Vector[CardTrait] = cardDeck

    def buildCardListWithLength: (List[CardTrait], Int) = (cardDeck.toList, lengthOfCardDeck)

    def buildCardList: List[CardTrait] = cardDeck.toList

  }

}


case class SpecialCardsDeck() extends CardDeckTrait {

  val specialCards: List[CardTrait] = generateDeck

  override def generateDeck: List[CardTrait] = {
    List(Card("1 11 play", "move move play", "red"),
      Card("4", "backward forward", "red"),
      Card("7", "burn", "red"),
      Card("swapCard", "swap", "red"),
      Card("questionmark", "joker", "red"),
      Card("13 play", "move play", "red"))
  }

  override def getCardDeck: List[CardTrait] = specialCards
}


case class NormalCardsDeck() extends CardDeckTrait {

  val normalCards: List[CardTrait] = generateDeck

  override def generateDeck: List[CardTrait] = {
    List(Card("2", "move", "blue"),
      Card("3", "move", "blue"),
      Card("5", "move", "blue"),
      Card("6", "move", "blue"),
      Card("8", "move", "blue"),
      Card("9", "move", "blue"),
      Card("10", "move", "blue"),
      Card("12", "move", "blue"))
  }

  override def getCardDeck: List[CardTrait] = normalCards
}
