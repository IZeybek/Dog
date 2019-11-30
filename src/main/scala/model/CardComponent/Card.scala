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

//case class MoveLogic() {
//
//  def myLogic(player: Array[Player], task: String): Unit = {
//    //    val s = CardLogic.getLogic("move")
//    //    val logicMode: String = "move";
//    //
//    //    val taskMode = CardLogic.getLogic(logicMode)
//    //    val move1 = CardLogic.setStrategy(deviationModeFunction, player, 2, 3, 3)
//    //    val move2 = CardLogic.setStrategy(deviationModeFunction, player, 2, 3, 3)
//    //
//    //    //    val mean = DeviationMode.mean(deviationModeFunction, 2, 25)
//    //    //    val breach = DeviationMode.compare(DeviationMode.compareMeanDeviation, mean, 88)
//
//  }
//
//}

object CardLogic {


  val move = (player: Array[Player], board: Board, playerNums: List[Int], pieceNum: Int, moveBy: Int) => {

    //move piece of specific player by returning a copy of the piece to the copy constructor player and returning a copy of the player
    val p: Player = player(playerNums(0))
    val players: Array[Player] = player

    if (board.checkOverrideOtherPlayer(p, pieceNum, moveBy)) {
      val nextCellPos = moveBy + p.getPosition(pieceNum)
      val otherPlayerIndex: Int = players.indexWhere(x => x.color == board.getBoardMap(nextCellPos).player.color)
      players(otherPlayerIndex) = board.getBoardMap(nextCellPos).player.overridePlayer(pieceNum)
    }
    players(playerNums(0)) = p.movePlayer(pieceNum, moveBy)

    (board.movePlayer(p, pieceNum, moveBy), players)
  }

  val swap = (player: Array[Player], board: Board, playerNums: List[Int], pieceNum: Int, moveBy: Int) => {
    val p: Player = player(playerNums(0))
    val swapPlayer: Player = player(playerNums(1))
    val swapPos: (Int, Int) = (p.getPosition(pieceNum), swapPlayer.getFurthestPosition()._1)
    val players: Array[Player] = player

    players(playerNums(0)) = p.swapPiece(pieceNum, swapPos._2)
    players(playerNums(1)) = swapPlayer.swapPiece(swapPlayer.getFurthestPosition()._2, swapPos._1)
    (board, players)
  }


  def setStrategy(callback: (Array[Player], Board, List[Int], Int, Int) => (Board, Array[Player]), player: Array[Player], board: Board, playerNum: List[Int], pieceNum: Int, moveBy: Int) = {
    callback(player, board, playerNum, pieceNum, moveBy)
  }

  def getLogic(mode: String) = {
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

