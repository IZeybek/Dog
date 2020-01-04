package dog.controller

import dog.model.CardComponent.CardTrait


case class InputCard(actualPlayer: Int, otherPlayer: Int, selPieceList: List[Int], cardNum: (Int, Int), selectedCard: CardTrait, moveBy: Int)

//--------------------------------------------------------------------------------------

object InputCardMaster {

  var otherPlayer: Int = -1
  var actualPlayer: Int = 0
  var selPieceList: List[Int] = List(0)

  var selCard: CardTrait = _
  var cardNum: (Int, Int) = (0, 0)

  var moveBy: Int = 0

  case class UpdateCardInput() {

    def withOtherPlayer(otherP: Int): UpdateCardInput = {
      otherPlayer = otherP
      this
    }

    def withActualPlayer(actPlayer: Int): UpdateCardInput = {
      actualPlayer = actPlayer
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

    def withSelectedCard(selectedC: CardTrait): UpdateCardInput = {
      selCard = selectedC
      moveBy = if (selCard.task.equals("move")) selCard.symbol.toInt else 0
      this
    }

    def withMoveBy(move: Int): UpdateCardInput = {
      moveBy = move
      this
    }

    def buildCardInput(): InputCard = {
      InputCard(actualPlayer, otherPlayer, selPieceList, cardNum, selCard, moveBy)
    }
  }
}
