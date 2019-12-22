package dog.model.CardComponent.Card

import dog.controller.Component.controllerBaseImpl.Controller
import dog.controller.ControllerTrait
import dog.model.BoardComponent.BoardTrait
import dog.model.BoardComponent.boardBaseImpl.Board
import dog.model.CardComponent.CardTrait
import dog.model.CardComponent.cardBaseImpl._
import dog.model.Player
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
      val board: BoardTrait = new Board(20)
      val controller: ControllerTrait = new Controller(board)
      val player: Vector[Player] = controller.gameState.players._1
      val cardLogic = CardLogic
      val mode = cardLogic.getLogic("move")
      "have a mode" in {
        mode should be(cardLogic.move)
      }
      "have a getLogic" in {
        cardLogic.getLogic("move") should be(cardLogic.move)
      }
      "have a Strategy" in {
        cardLogic.setStrategy(mode, player, board, List(2), List(2), 2) should not be null
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
        specialCard.getCardDeck should be(List(Card("1 11 play", "move move play", "red"),
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
        normalCard.getCardDeck should be(List(Card("2", "move", "blue"),
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

