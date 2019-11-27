package model
import model.CardComponent.Card

trait CardTrait {

  def generateDeck : List[Card]

  def getCardDeck: List[Card]

}
