package dog.controller

import dog.model.CardComponent.CardTrait


case class InputCard(otherPlayer: Int, selPieceList: List[Int], cardNum: (Int, Int), selPlayerList: List[Int], selectedCard: CardTrait, moveBy: Int)

//--------------------------------------------------------------------------------------

object InputCardObject {

  var otherPlayer: Int = -1
  var selPieceList: List[Int] = List(0)
  var cardNum: (Int, Int) = (0, 0)
  var selPlayerList: List[Int] = List(0)
  var moveBy: Int = 0
  var selCard: CardTrait = _

  case class UpdateCardInput() {

    def withOtherPlayer(otherP: Int): UpdateCardInput = {
      otherPlayer = otherP
      this
    }

    def withPieceNum(pieceN: List[Int]): UpdateCardInput = {
      selPieceList = pieceN
      this
    }

    def withCardNum(cardN: (Int, Int)): UpdateCardInput = {
      cardNum = cardN
      this
    }

    def withSelectedPlayerList(selectedPList: List[Int]): UpdateCardInput = {
      selPlayerList = selectedPList
      this
    }

    def withSelectedCard(selectedC: CardTrait): UpdateCardInput = {
      selCard = selectedC
      moveBy = if (selCard.symbol.length <= 2) selCard.symbol.toInt else 0
      this
    }

    def withMoveBy(move: Int): UpdateCardInput = {
      moveBy = move
      this
    }

    def buildCardInput(): InputCard = {
      InputCard(otherPlayer, selPieceList, cardNum, selPlayerList, selCard, moveBy)
    }
  }
}