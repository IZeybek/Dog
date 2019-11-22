package model.CardComponent

import model.Cards

case class Card(symbol: String, task: String, color: String) {

  def getSymbol: String = symbol

  def getTask: String = task

  def getColor: String = color
}

case class SpecialCards() extends Cards {

  val specialCards: List[Card] = generateDeck

  override def generateDeck: List[Card] = {
    List(Card("1 11 start", "move;move;start", "red"),
      Card("4","forwards;backwards","red" ),
      Card("7","" ,"red"),
      Card("swap","swap", "red"),
      Card("?", "joker", "red"),
      Card("13 play", "move;start", "red"))
  }

  override def getCardDeck(): List[Card] = specialCards
}


case class NormalCards() extends Cards {


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

