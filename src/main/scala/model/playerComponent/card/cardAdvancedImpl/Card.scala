package model.playerComponent.card.cardAdvancedImpl

import model.Player
import model.playerComponent.card.CardTrait

case class ChangeCard() extends CardTrait {

  override def cardFunc(p: Player): Unit = ???

  override def toString: String = ".//////////////////.\n////////////////////\n/////..//....///////\n/////.../////....///\n///...///...///...//\n//...///.....///..//\n//..........///...//\n/..........///....//\n/........///......//\n//......./........//\n//................//\n//......///.././/.//\n//////......////////\n//////////./////////\n.//////////////////."
}


case class SevenCard() extends CardTrait {

  override def cardFunc(p: Player): Unit = ???

}


case class JokerCard() extends CardTrait {

  override def cardFunc(p: Player): Unit = ???

}

