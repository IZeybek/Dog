package model.CardComponent

import model.{Board, CardTrait, Player}

import scala.collection.mutable.ListBuffer


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


  val move: (Vector[Player], Board, List[Int], List[Int], Int) => (Board, Vector[Player], Int) = (player: Vector[Player], board: Board, selectedPlayerIndices: List[Int], pieceNum: List[Int], moveBy: Int) => {

    var isValid: Int = 0
    //move piece of specific player by returning a copy of the piece to the copy constructor player and returning a copy of the player
    val players: Vector[Player] = player
    var finalPlayer: Vector[Player] = Vector.empty[Player]
    val p: Player = player(selectedPlayerIndices.head)
    val newPos: Int = Math.floorMod(moveBy + p.getPosition(pieceNum.head), board.boardMap.size)

    //overriding player
    if (board.checkOverrideOtherPlayer(p, pieceNum.head, newPos)) {

      //get indexes and pieces
      val oPlayerIdx: Int = players.indexWhere(x => x.color == board.boardMap(newPos).getColor)
      val oPlayerPieceNum: Int = players(oPlayerIdx).getPieceNum(newPos) //get piece of other Player

      //check wether move valid or not
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


  val swap: (Vector[Player], Board, List[Int], List[Int], Int) => (Board, Vector[Player], Int) = (player: Vector[Player], board: Board, selectedPlayerIndices: List[Int], pieceNums: List[Int], moveBy: Int) => {

    var isValid = 0
    //swap a piece of the player that uses the card with the furthest piece of another player
    val p: Player = player(selectedPlayerIndices.head)
    val swapPlayer: Player = player(selectedPlayerIndices(1))
    val swapPos: (Int, Int) = (p.getPosition(pieceNums.head), swapPlayer.getPosition(pieceNums(1)))

    if (swapPos._2 == 0) isValid = -1 //Second Player is not on the field

    val playerOneSwapped: Vector[Player] = player.updated(selectedPlayerIndices.head, p.swapPiece(pieceNums.head, swapPos._2)) //swap with second player
    val playerTwoSwapped: Vector[Player] = playerOneSwapped.updated(selectedPlayerIndices(1), swapPlayer.swapPiece(pieceNums(1), swapPos._1)) //swap with first player

    val nboard = board.updateSwapPlayers(playerTwoSwapped, selectedPlayerIndices, pieceNums)

    (nboard, playerTwoSwapped, isValid)
  }


  def setStrategy(callback: (Vector[Player], Board, List[Int], List[Int], Int) => (Board, Vector[Player], Int), player: Vector[Player], board: Board, playerNum: List[Int], pieceNums: List[Int], moveBy: Int): (Board, Vector[Player], Int) = {
    callback(player, board, playerNum, pieceNums, moveBy)
  }

  def getLogic(mode: String): (Vector[Player], Board, List[Int], List[Int], Int) => (Board, Vector[Player], Int) = {
    mode match {
      case "move" => move
      case "swap" => swap
      //      case "start" => Nil
      //      case "forwardBackward" => Nil
      case _ => move
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
  def apply(amounts: List[Int]): List[Card] = {
    val list = new ListBuffer[Card]()
    amounts.indices.foreach(i => for (elem <- (0 until amounts(i))) {
      i match {
        case 0 => list.++=(GenCardDeck.apply("special").getCardDeck)
        case 1 => list.++=(GenCardDeck.apply("normal").getCardDeck)
      }
    })
    list.toList
    //    GenCardDeck.apply("special").getCardDeck ++
    //      GenCardDeck.apply("normal").getCardDeck ++
    //      GenCardDeck.apply("special").getCardDeck ++
    //      GenCardDeck.apply("normal").getCardDeck ++
    //      GenCardDeck.apply("special").getCardDeck ++
    //      GenCardDeck.apply("normal").getCardDeck ++
    //      GenCardDeck.apply("special").getCardDeck ++
    //      GenCardDeck.apply("normal").getCardDeck
  }
}


case class SpecialCardsDeck() extends CardTrait {

  val specialCards: List[Card] = generateDeck

  override def generateDeck: List[Card] = {
    List(Card("1 11 play", "move move play", "red"),
      Card("4", "backward forward", "red"),
      Card("7", "burn", "red"),
      Card("swapCard", "swap", "red"),
      Card("questionmark", "joker", "red"),
      Card("13 play", "move play", "red"))
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

