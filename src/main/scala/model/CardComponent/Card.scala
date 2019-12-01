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


  val move = (player: Array[Player], board: Board, playerNums: List[Int], pieceNum: Int, moveBy: Int) => {

    //move piece of specific player by returning a copy of the piece to the copy constructor player and returning a copy of the player
    var players: Array[Player] = player
    val p: Player = player(playerNums.head)

    //overriding player
    if (board.checkOverrideOtherPlayer(p, pieceNum, moveBy)) {

      val newPos: Int = moveBy + p.getPosition(pieceNum)
      val otherPlayerIndex: Int = players.indexWhere(x => x.color == board.boardMap(newPos).player.color)
      val otherPlayerPieceNum: Int = players(otherPlayerIndex).getPieceNum(newPos)

      println(s"$otherPlayerIndex has $otherPlayerPieceNum on $newPos")
      if (otherPlayerPieceNum == -1) throw new NoSuchElementException

      players = players.updated(otherPlayerIndex, players(otherPlayerIndex).overridePlayer(otherPlayerPieceNum))
    }

    players = players.updated(playerNums.head, players(playerNums.head).movePlayer(pieceNum, moveBy))

    (board.movePlayer(p, pieceNum, moveBy), players)
  }


  val swap = (player: Array[Player], board: Board, playerNums: List[Int], pieceNum: Int, moveBy: Int) => {

    //swap a piece of the player that uses the card with the furthest piece of another player
    val p: Player = player(playerNums(0))
    val swapPlayer: Player = player(playerNums(1))
    val swapPos: (Int, Int) = (p.getPosition(pieceNum), swapPlayer.getFurthestPosition._1)
    val players: Array[Player] = player

    players(playerNums(0)) = p.swapPiece(pieceNum, swapPos._2)
    players(playerNums(1)) = swapPlayer.swapPiece(swapPlayer.getFurthestPosition._2, swapPos._1)

    val nboard = board.swapPlayers(players, playerNums, List(pieceNum, swapPlayer.getFurthestPosition._2))

    (nboard, players)
  }


  def setStrategy(callback: (Array[Player], Board, List[Int], Int, Int) => (Board, Array[Player]), player: Array[Player], board: Board, playerNum: List[Int], pieceNum: Int, moveBy: Int) = {
    callback(player, board, playerNum, pieceNum, moveBy)
  }

  def getLogic(mode: String): (Array[Player], Board, List[Int], Int, Int) => (Board, Array[Player]) = {
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

