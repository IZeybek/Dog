package model

case class ChangeCard() extends CardTrait {

  def cardFunc(playerNum: Integer, p: Array[Player]): Array[Player] = {
    p
  }

  override def toString: String = ".//////////////////.\n////////////////////\n/////..//....///////\n/////.../////....///\n///...///...///...//\n//...///.....///..//\n//..........///...//\n/..........///....//\n/........///......//\n//......./........//\n//................//\n//......///.././/.//\n//////......////////\n//////////./////////\n.//////////////////."
}


case class SevenCard() extends CardTrait {

  def cardFunc(playerNum: Integer, p: Array[Player]): Array[Player] = {
    p
  }

  override def toString: String = "///////////.////////\n////////////////////\n/.//////////////////\n///////.........////\n/.///../..//.../////\n////////////.///////\n//////////..////////\n//////......////////\n///////...//////////\n///////..///////////\n//////...///////////\n//////..////////////\n////////////////////\n////////////////////\n////////////////////"

}


case class JokerCard() extends CardTrait {

  def cardFunc(playerNum: Integer, p: Array[Player]): Array[Player] = {
    p
  }

}

