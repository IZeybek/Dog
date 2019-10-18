class Player(name: String, handCards: Array[String]) {
  private var position = 0
  def printName: Unit = {
    print(name)
  }

  def printPosition: Unit = {
    print(position)
  }

  def getPosition: Integer = {
    position
  }

  def movePlayer(moveby: Int): Unit = {
    position = position + moveby
  }
}

