package model.CardComponent

import model.CardTrait

case class Card(symbol: String, task: String, color: String) {

  def getSymbol: String = symbol

  def getTask: String = task

  def getColor: String = color

  override def toString: String = {
    var symbol = "Card("
    symbol = symbol + s"${
      getColor match {
        case "blue" => Console.BLUE;
        case "red" => Console.RED
      }
    }${getSymbol}${Console.RESET})"

    symbol
  }
}

case class CardDeck() {

  val deck = generateCards

  def generateCards: List[Card] = {
    val deck1 = SpecialCardsDeck()
    val deck2 = NormalCardsDeck()
    val deck3 = SpecialCardsDeck()
    val deck4 = NormalCardsDeck()
    deck1.getCardDeck() ++ deck2.getCardDeck() ++ deck3.getCardDeck() ++ deck4.getCardDeck()
  }

  def getDeck: List[Card] = deck
}

case class SpecialCardsDeck() extends CardTrait {

  val specialCards: List[Card] = generateDeck

  override def generateDeck: List[Card] = {
    List(Card("1 11 start", "move;move;start", "red"),
      Card("4", "forwards;backwards", "red"),
      Card("7", "burn", "red"),
      Card("swap", "swap", "red"),
      Card("?", "joker", "red"),
      Card("13 play", "move;start", "red"))
  }

  override def getCardDeck(): List[Card] = specialCards
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

  override def getCardDeck(): List[Card] = normalCards
}

