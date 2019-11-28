package model.CardComponent

import model.{CardTrait, Piece, Player}

import scala.util.Random


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

case class MoveLogic() {

  def myLogic(player : List[Player], task: String): Unit = {
    val s = CardLogic.getLogic("move")
    val logicMode: String = "move";

    val deviationModeFunction = CardLogic.getLogic(logicMode)
    val move1 = CardLogic.setStrategy(deviationModeFunction, player,2,3,3)
    val move2 = CardLogic.setStrategy(deviationModeFunction,  player,2,3,3)

//    val mean = DeviationMode.mean(deviationModeFunction, 2, 25)
//    val breach = DeviationMode.compare(DeviationMode.compareMeanDeviation, mean, 88)

  }

}

object CardLogic {


  val move = (player:List[Player], playerNum: Int, pieceNum: Int, moveBy: Int) => {
    player(playerNum).getPiece(pieceNum).movePiece(moveBy)
  }
  val swap = (player:List[Player],playerNum: Int, pieceNum: Int, moveBy: Int) => {
    player(playerNum).getPiece(2).movePiece(moveBy)
  }

  def setStrategy(callback: (List[Player],Int, Int, Int) => Piece, player: List[Player],playerNum: Int, pieceNum: Int, moveBy: Int) = {
    callback(pieceNum, pieceNum, moveBy);
  }

  def getLogic(mode: String) = {
    mode match {
      case "move" => move
      case "swap" => swap
//      case "start" => Nil
//      case "forwardBackward" => Nil
      case _ => throw new IllegalArgumentException("Supported type are LOWER and HIGHER.")
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

