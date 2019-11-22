package model
import model.CardComponent.Card

trait Cards {

  def generateDeck : List[Card]

  def getCardDeck() : List[Card]


}
