package model

import model.playerComponent.card.CardTrait

case class Player(name: String, cards: Array[CardTrait], piece: Array[Piece]) {

  var piecesOnBoard: Int = _
  var piecesOnStandby: Int = _

  override def toString: String = name

  def getCards: Array[CardTrait] = cards

}

case class Piece(xy: Array[Int])
