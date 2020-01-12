package dog.controller

import dog.model.CardComponent.CardTrait
import dog.model.CardComponent.cardBaseImpl.Card
import dog.model.Player


case class InputCard(actualPlayerIdx: Int, otherPlayer: Int, selPieceList: List[Int], cardIdxAndOption: (Int, Int), selectedCard: CardTrait, moveBy: Int)

//--------------------------------------------------------------------------------------

object InputCardMaster {

  var otherPlayer: Int = -1
  var selPieceList: List[Int] = List(0)
  var cardNum: (Int, Int) = (0, 0)
  var actualPlayerIdx = 0
  var actualPlayer: Player = _
  var moveBy: Int = 0
  var selCard: CardTrait = _

  case class UpdateCardInput() {

    def reset(): Unit = {

      otherPlayer = -1
      selPieceList = List(0)
      cardNum = (-1, -1)
      moveBy = 0
      selCard = Card("pseudo", "pseudo", "pseudo")
    }

    def withOtherPlayer(otherP: Int): UpdateCardInput = {
      otherPlayer = otherP
      this
    }

    def withActualPlayer(actPlayer: Int): UpdateCardInput = {
      actualPlayerIdx = actPlayer
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
      InputCard(actualPlayerIdx, otherPlayer, selPieceList, cardNum, selCard, moveBy)
    }
  }

}

