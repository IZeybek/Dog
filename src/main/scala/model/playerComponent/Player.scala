package model.playerComponent

import model.playerComponent.CardBaseImpl.Card

case class Player(name: String, cards: Array[Card]) {
  override def toString: String = name

  def getCards: Array[Card] = cards
}
