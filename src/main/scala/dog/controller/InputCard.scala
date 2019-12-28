package dog.controller

import dog.model.CardComponent.CardTrait


case class InputCard(otherPlayer: Int, pieceNum: List[Int], cardNum: (Int, Int), selPlayerList: List[Int], selectedCard: CardTrait)

//--------------------------------------------------------------------------------------

object InputCardObject {

  var otherPlayer: Int = -1
  var pieceNum: List[Int] = List(0)
  var cardNum: (Int, Int) = (0, 0)
  var selectedPlayerList: List[Int] = List(0)
  var selectedCard: CardTrait = _
  var cardAttributes: List[String] = List("")

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

    def withSelectedPlayerList(selectedPList: List[Int]): UpdateCardInput = {
      selectedPlayerList = selectedPList
      this
    }

    def withSelectedCard(selectedC: CardTrait): UpdateCardInput = {
      selectedCard = selectedC
      this
    }

    def withCardAttributes(cAttributes: List[String]): UpdateCardInput = {
      cardAttributes = cAttributes
      this
    }

    def buildCardInput(): InputCard = {
      InputCard(otherPlayer, pieceNum, cardNum, selectedPlayerList, selectedCard)
    }
  }

}


