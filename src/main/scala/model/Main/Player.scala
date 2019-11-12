package model.Main

case class Player(name: String, cards: Array[Card]) {
  override def toString: String = name

  def getCards: Array[Card] = cards
}
