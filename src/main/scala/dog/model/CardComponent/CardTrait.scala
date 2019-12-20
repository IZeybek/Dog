package dog.model.CardComponent

import dog.model.CardComponent.cardBaseImpl.Card

trait CardTrait {
  this: Card =>

  def getTask: String

  def toString: String

  def getSymbol: String

  def getColor: String
}
