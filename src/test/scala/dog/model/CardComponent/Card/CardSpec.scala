package dog.model.CardComponent.Card

import dog.controller.ControllerComponent.ControllerTrait
import dog.controller.ControllerComponent.controllerBaseImpl.Controller
import dog.controller.StateComponent.InputCardMaster
import dog.model.BoardComponent.boardBaseImpl.Board
import dog.model.CardComponent.CardTrait
import dog.model.CardComponent.cardBaseImpl._
import org.scalatest.{Matchers, WordSpec}

class CardSpec extends WordSpec with Matchers {
  "A Card" when {
    "created" should {
      val card: CardTrait = Card("2", "move", "blau")
      "have a CaseClass" in {
        card should not be null
      }
      "have a Symbol" in {
        card.symbol should be("2")
      }
      "have a Task" in {
        card.task should be("move")
      }
      "have a Color" in {
        card.color should be("blau")
      }
    }
  }
}

class CardLogicSpec extends WordSpec with Matchers {
  "A CardLogic" when {
    "created" should {
      val controller: ControllerTrait = new Controller(new Board(50))
      val cardLogic = CardLogic
      val mode = cardLogic.getLogic("move")
      "have a mode" in {
        mode should be(cardLogic.move)
      }
      "have a getLogic" in {
        cardLogic.getLogic("move") should be(cardLogic.move)
      }
      "have a Strategy" in {
        val inputCard = InputCardMaster.UpdateCardInput()
          .withActualPlayer(2)
          .withOtherPlayer(0)
          .withPieceNum(List(2))
          .withCardNum((0, 0))
          .withSelectedCard(Card("5", "move", "blue"))
          .buildCardInput()
        cardLogic.setStrategy(mode, controller.gameState, inputCard) should not be null
      }
      "have a move" in {
        cardLogic.move should not be null
      }
    }
  }
}

class GenCardDeckBuilderSpec extends WordSpec with Matchers {
  "A GenCardDeck" when {
    "created" should {
      val genCard = GenCardDeck
      "have a SpecialCardsDeck" in {
        genCard.apply("special") should be(SpecialCardsDeck())
      }
      "have a NormalCardsDeck" in {
        genCard.apply("normal") should be(NormalCardsDeck())
      }
    }
  }
}


class CardDeckBuilderSpec extends WordSpec with Matchers {
  "A CardDeckBuilder" when {
    "created" should {
    }
  }
}


class SpecialCardsDeckSpec extends WordSpec with Matchers {
  "A CardDeckBuilder" when {
    "generated" should {
      val specialCard = SpecialCardsDeck()
      "have a SpecialCardsDeck " in {
        specialCard.cardDeck should be(List(Card("1 11 play", "move move play", "red"),
          Card("4", "backward forward", "red"),
          Card("7", "burn", "red"),
          Card("swapCard", "swap", "red"),
          Card("questionmark", "joker", "red"),
          Card("13 play", "move play", "red")))
      }
    }
  }
}

class NormalCardsDeckSpec extends WordSpec with Matchers {
  "A NormalCardsDeck" when {
    "generated" should {
      val normalCard = NormalCardsDeck()
      "have a SpecialCardsDeck " in {
        normalCard.cardDeck should be(List(Card("2", "move", "blue"),
          Card("3", "move", "blue"),
          Card("5", "move", "blue"),
          Card("6", "move", "blue"),
          Card("8", "move", "blue"),
          Card("9", "move", "blue"),
          Card("10", "move", "blue"),
          Card("12", "move", "blue")))
      }
    }
  }
}

