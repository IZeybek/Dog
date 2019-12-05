package model.CardComponent

import model.{Board, CardTrait, Player}


case class Card(symbol: String, task: String, color: String) {

  def getSymbol: String = symbol

  def getTask: String = task

  def getColor: String = color


  override def toString: String = {
    "Card(" + s"${
      getColor match {
        case "blue" => Console.BLUE;
        case "red" => Console.RED
      }
    }$getSymbol${Console.RESET})"
  }
}


object CardLogic {


  val move: (Array[Player], Board, List[Int], List[Int], Int) => (Board, Array[Player], Int) = (player: Array[Player], board: Board, selectedPlayerIndices: List[Int], pieceNum: List[Int], moveBy: Int) => {

    //move piece of specific player by returning a copy of the piece to the copy constructor player and returning a copy of the player
    var players: Array[Player] = player
    val p: Player = player(selectedPlayerIndices.head)
    val newPos: Int = Math.floorMod(moveBy + p.getPosition(pieceNum.head), board.boardMap.size)
    var isValid: Int = 0

    //overriding player
    if (board.checkOverrideOtherPlayer(p, pieceNum.head, newPos)) {
      val otherPlayerIndex: Int = players.indexWhere(x => x.color == board.boardMap(newPos).getColor)
      val otherPlayerPieceNum: Int = players(otherPlayerIndex).getPieceNum(newPos) //get piece of other Player
      println(s"$otherPlayerIndex has $otherPlayerPieceNum on $newPos")
      if (otherPlayerPieceNum == -1) isValid = -1
      players = players.updated(otherPlayerIndex, players(otherPlayerIndex).overridePlayer(otherPlayerPieceNum))
    }

    players = players.updated(selectedPlayerIndices.head, players(selectedPlayerIndices.head).setPosition(pieceNum.head, newPos))
    (board.updateMovePlayer(p, pieceNum.head, newPos), players, 0)
  }


  val swap: (Array[Player], Board, List[Int], List[Int], Int) => (Board, Array[Player], Int) = (player: Array[Player], board: Board, selectedPlayerIndices: List[Int], pieceNums: List[Int], moveBy: Int) => {

    //swap a piece of the player that uses the card with the furthest piece of another player
    val p: Player = player(selectedPlayerIndices.head)
    val swapPlayer: Player = player(selectedPlayerIndices(1))
    val swapPos: (Int, Int) = (p.getPosition(pieceNums.head), swapPlayer.getPosition(pieceNums(1)))
    val players: Array[Player] = player
    var isValid = 0

    players(selectedPlayerIndices.head) = p.swapPiece(pieceNums.head, swapPos._2)
    players(selectedPlayerIndices(1)) = swapPlayer.swapPiece(pieceNums(1), swapPos._1)

    val nboard = board.updateSwapPlayers(players, selectedPlayerIndices, pieceNums)

    (nboard, players, isValid)
  }


  def setStrategy(callback: (Array[Player], Board, List[Int], List[Int], Int) => (Board, Array[Player], Int), player: Array[Player], board: Board, playerNum: List[Int], pieceNums: List[Int], moveBy: Int): (Board, Array[Player], Int) = {
    callback(player, board, playerNum, pieceNums, moveBy)
  }

  def getLogic(mode: String): (Array[Player], Board, List[Int], List[Int], Int) => (Board, Array[Player], Int) = {
    mode match {
      case "move" => move
      case "swap" => swap
      //      case "start" => Nil
      //      case "forwardBackward" => Nil
      case _ => throw new IllegalArgumentException("Supported type are move")
    }
  }
}

object GenCardDeck {

  def apply(typ: String): CardTrait = typ match {
    case "special" =>
      SpecialCardsDeck()
    case "normal" =>
      NormalCardsDeck()
  }
}

object CardDeck {
  def apply(): List[Card] = {

    GenCardDeck.apply("special").getCardDeck ++
      GenCardDeck.apply("normal").getCardDeck ++
      GenCardDeck.apply("special").getCardDeck ++
      GenCardDeck.apply("normal").getCardDeck ++
      GenCardDeck.apply("special").getCardDeck ++
      GenCardDeck.apply("normal").getCardDeck ++
      GenCardDeck.apply("special").getCardDeck ++
      GenCardDeck.apply("normal").getCardDeck
  }
}


case class SpecialCardsDeck() extends CardTrait {

  val specialCards: List[Card] = generateDeck

  override def generateDeck: List[Card] = {
    List(Card("1 11 start", "move;move;start", "red"),
      Card("4", "forwardBackward", "red"),
      Card("7", "burn", "red"),
      Card("swap", "swap", "red"),
      Card("?", "joker", "red"),
      Card("13 play", "move;start", "red"))
  }

  override def getCardDeck: List[Card] = specialCards
}


case class NormalCardsDeck() extends CardTrait {

  val normalCards: List[Card] = generateDeck

  override def generateDeck: List[Card] = {
    List(Card("2", "move", "blue"),
      Card("3", "move", "blue"),
      Card("5", "move", "blue"),
      Card("6", "move", "blue"),
      Card("8", "move", "blue"),
      Card("9", "move", "blue"),
      Card("10", "move", "blue"),
      Card("12", "move", "blue"))
  }

  override def getCardDeck: List[Card] = normalCards
}

