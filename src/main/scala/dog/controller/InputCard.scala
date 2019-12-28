package dog.controller


case class InputCard(otherPlayer: Int, pieceNum: List[Int], cardNum: (Int, Int))

//--------------------------------------------------------------------------------------

object InputCardObject {

  var otherPlayer: Int = -1
  var pieceNum: List[Int] = List(0)
  var cardNum: (Int, Int) = (0, 0)


  case class UpdateCardInput() {

    def withOtherPlayer(otherP: Int): UpdateCardInput = {
      otherPlayer = otherP
      this
    }

    def withPieceNum(pieceN: List[Int]): UpdateCardInput = {
      pieceNum = pieceN
      this
    }

    def withCardNum(cardN: (Int, Int)): UpdateCardInput = {
      cardNum = cardNum
      this
    }

    def buildCardInput(): InputCard = {
      InputCard(otherPlayer, pieceNum, cardNum)
    }
  }

}


