package dog.model.CardComponent.cardBaseImpl

import dog.controller.{GameState, InputCard, InputCardMaster, JokerState}
import dog.model.BoardComponent.BoardTrait
import dog.model.CardComponent.{CardDeckTrait, CardTrait}
import dog.model.Player

import scala.collection.mutable.ListBuffer
import scala.util.{Random, Success, Try}


case class Card(symbol: String, task: String, color: String) extends CardTrait {

  override def toString: String = {
    "Card(" + s"${
      color match {
        case "blue" => Console.BLUE;
        case "red" => Console.RED
        case _ => ""
      }
    }$symbol${Console.RESET})"
  }

  override def parseToList: List[String] = symbol :: task :: color :: Nil

  override def parse(select: Int): Card = {
    val newSymbol: Try[String] = Try(symbol.split("\\s+")(select))
    val newTask: Try[String] = Try(task.split("\\s")(select))

    copy(symbol = newSymbol match {
      case Success(symbol) => symbol
      case _ => symbol.split("\\s+")(0)
    }, task = newTask match {
      case Success(task) => task
      case _ => task.split("\\s+")(0)
    })
  }
}


object CardLogic {

  val move: (GameState, InputCard) => (BoardTrait, Vector[Player], Int) = (gameState: GameState, inputC: InputCard) => {

    var isValid: Int = 0
    //move piece of specific player by returning a copy of the piece to the copy constructor player and returning a copy of the player
    var players: Vector[Player] = gameState.players._1
    val actPlayerIdx: Int = inputC.actualPlayerIdx
    val actPlayer: Player = players(actPlayerIdx)
    val board: BoardTrait = gameState.board
    val selPiece = inputC.selPieceList.head

    val oldPos: Int = actPlayer.piecePosition(selPiece)
    val newPos: Int = Math.floorMod(inputC.moveBy + oldPos, board.size)


    //overriding player
    if (board.checkOverrideOtherPlayer(actPlayer, newPos)) {
      //get indexes and pieces
      val oPlayerIdx: Int = board.cell(newPos).p.get.nameAndIdx._2

      val oPlayerPieceNum: Int = players(oPlayerIdx).getPieceNum(newPos) //get piece of other Player

      //check whether move valid or not
      if (oPlayerPieceNum == -1) isValid = -1

      //update Vector when overridden
      players = players.updated(oPlayerIdx, players(oPlayerIdx).overridePlayer(oPlayerPieceNum))
      players = players.updated(actPlayerIdx, actPlayer.setPosition(selPiece, newPos))

    } else {
      //update Vector when not overridden
      players = players.updated(actPlayerIdx, actPlayer.setPosition(selPiece, newPos))
    }

    (board.updateMovePlayer(players(actPlayerIdx), oldPos, newPos), players, isValid)
  }

  val swap: (GameState, InputCard) => (BoardTrait, Vector[Player], Int) = (gameState: GameState, inputCard: InputCard) => {

    var isValid = 0
    //swap a piece of the player that uses the card with the furthest piece of another player
    val actPlayer: Player = gameState.actualPlayer
    val swapPlayer: Player = gameState.players._1(inputCard.otherPlayer)
    val selPiece = inputCard.selPieceList.head
    val swapPos: (Int, Int) = (actPlayer.piece(selPiece).pos, swapPlayer.piece(inputCard.selPieceList(1)).pos)

    if (swapPos._2 == swapPlayer.homePosition) isValid = -1 //Second Player is not on the field

    var players: Vector[Player] = gameState.players._1.updated(inputCard.actualPlayerIdx, actPlayer.swapPiece(selPiece, swapPos._2)) //swap with second player

    players = players.updated(inputCard.otherPlayer, swapPlayer.swapPiece(inputCard.selPieceList(1), swapPos._1)) //swap with first player

    val nBoard: BoardTrait = gameState.board.updateSwapPlayers(players(actPlayer.nameAndIdx._2), players(swapPlayer.nameAndIdx._2), inputCard.selPieceList)

    (nBoard, players, isValid)
  }

  val four: (GameState, InputCard) => (BoardTrait, Vector[Player], Int) = (gameState: GameState, inputCard: InputCard) => {

    val cardOption = inputCard.selectedCard.task.split("\\s+")

    cardOption(inputCard.cardIdxAndOption._2) match {
      case "forward" => move(gameState, InputCardMaster.UpdateCardInput().withMoveBy(4).buildCardInput())
      case "backward" => move(gameState, InputCardMaster.UpdateCardInput().withMoveBy(-4).buildCardInput())
      case _ => (gameState.board, gameState.players._1, -1)
    }
  }

  val nothing: (GameState, InputCard) => (BoardTrait, Vector[Player], Int) = (gameState: GameState, inputCard: InputCard) => {

    move(gameState, InputCardMaster.UpdateCardInput().buildCardInput())
  }

  val threePlay: (GameState, InputCard) => (BoardTrait, Vector[Player], Int) = (gameState: GameState, inputCard: InputCard) => {

    val cardOption = inputCard.selectedCard.symbol.split("\\s+")
    val nextPiecePlay = gameState.players._1(inputCard.actualPlayerIdx).nextPiece()
    cardOption(inputCard.cardIdxAndOption._2) match {
      case "1" => move(gameState, InputCardMaster.UpdateCardInput().withMoveBy(1).buildCardInput())
      case "11" => move(gameState, InputCardMaster.UpdateCardInput().withMoveBy(11).buildCardInput())
      case "play" => move(gameState, InputCardMaster.UpdateCardInput().withPieceNum(List(nextPiecePlay)).withMoveBy(0).buildCardInput())
      case _ => (gameState.board, gameState.players._1, -1)
    }
  }

  val twoPlay: (GameState, InputCard) => (BoardTrait, Vector[Player], Int) = (gameState: GameState, inputCard: InputCard) => {

    val cardOption = inputCard.selectedCard.symbol.split("\\s+")
    val nextPiecePlay = gameState.players._1(inputCard.actualPlayerIdx).nextPiece()
    cardOption(inputCard.cardIdxAndOption._2) match {
      case "13" => move(gameState, InputCardMaster.UpdateCardInput().withMoveBy(13).buildCardInput())
      case "play" => move(gameState, InputCardMaster.UpdateCardInput().withPieceNum(List(nextPiecePlay)).withMoveBy(0).buildCardInput())
      case _ => (gameState.board, gameState.players._1, -1)
    }
  }

  val joker: (GameState, InputCard) => (BoardTrait, Vector[Player], Int) = (gameState: GameState, inputCard: InputCard) => {
    println("joker")

    if (JokerState.state.equals(JokerState.unpacked)) {
      println("unpacked -> ")
      val updatedGameState = setStrategy(getLogic(inputCard.selectedCard.task), gameState, inputCard)
      JokerState.handle
      (updatedGameState._1, updatedGameState._2, 1)
    } else {
      JokerState.handle
      (gameState.board, gameState.players._1, 1)
    }


  }

  def setStrategy(callback: (GameState, InputCard) => (BoardTrait, Vector[Player], Int), gameState: GameState, inputCard: InputCard): (BoardTrait, Vector[Player], Int) = {
    callback(gameState, inputCard)
  }

  def getLogic(mode: String): (GameState, InputCard) => (BoardTrait, Vector[Player], Int) = {
    mode match {
      case "move" => move
      case "swap" => swap
      case "backward forward" => four
      case "move move play" => threePlay
      case "move play" => twoPlay
      case "joker" => joker
      case _ => nothing
    }
  }
}

object Card {

  var amount = 6
  var shuffledCards: List[CardTrait] = Random.shuffle(CardDeck.CardDeckBuilder().withAmount(List(amount * 2, amount * 2)).buildCardList)

  case class RandomCardsBuilder() {

    def withAmount(setAmount: Int): RandomCardsBuilder = {
      amount = setAmount
      this
    }

    def buildRandomCardList: List[CardTrait] = {
      shuffledCards = Random.shuffle(CardDeck.CardDeckBuilder().withAmount(List(amount * 2, amount * 2)).buildCardList)
      shuffledCards.take(amount)
    }

    def buildRandomCardVector: Vector[CardTrait] = {
      shuffledCards.toVector.take(amount)
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
  var cardDeck: Vector[CardTrait] = GenCardDeck.apply("special").cardDeck.toVector
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
          case 0 => list.++=(GenCardDeck.apply("special").cardDeck)
          case 1 => list.++=(GenCardDeck.apply("normal").cardDeck)
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

  override val cardDeck: List[CardTrait] = generateDeck

  override def generateDeck: List[CardTrait] = {
    List(Card("1 11 play", "move move play", "red"),
      Card("4", "backward forward", "red"),
      Card("7", "burn", "red"),
      Card("swapCard", "swap", "red"),
      Card("questionmark", "joker", "red"),
      Card("13 play", "move play", "red"))
  }
}

case class NormalCardsDeck() extends CardDeckTrait {

  override val cardDeck: List[CardTrait] = generateDeck

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
}
