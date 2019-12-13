package model.CardComponent

import model.CardComponent.cardBaseImpl.Card

trait CardDeckTrait {

  def generateDeck: List[Card]

  def getCardDeck: List[Card]

}
