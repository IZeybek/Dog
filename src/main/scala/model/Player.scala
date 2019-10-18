package model

case class Player(name: String, handCards: Array[String]) {
  override def toString(): String = name //returns playername
}

