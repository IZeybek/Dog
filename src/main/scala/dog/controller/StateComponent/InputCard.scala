package dog.controller.StateComponent

import dog.model.CardComponent.CardTrait
import dog.model.CardComponent.cardBaseImpl.Card


case class InputCard(otherPlayer: Int, selPieceList: List[Int], cardIdxAndOption: (Int, Int), selectedCard: CardTrait, moveBy: Int)

//--------------------------------------------------------------------------------------

object InputCardMaster {

  var otherPlayer: Int = -1
  var selPieceList: List[Int] = List(-1)
  var cardNum: (Int, Int) = (-1,-1)
  var moveBy: Int = 0
  var selCard: CardTrait = Card("pseudo", "pseudo", "pseudo")

  case class UpdateCardInput() {

    def reset(): Unit = {

      otherPlayer = -1
      selPieceList = List(-1)
      cardNum = (-1, -1)
      moveBy = 0
      selCard = Card("pseudo", "pseudo", "pseudo")
    }

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
      InputCard(otherPlayer, selPieceList, cardNum, selCard, moveBy)
    }
  }

}

