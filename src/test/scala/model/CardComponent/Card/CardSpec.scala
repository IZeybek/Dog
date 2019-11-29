package model

import controller.Controller
import model.CardComponent.{Card, CardDeck, CardLogic, GenCardDeck, NormalCardsDeck, SpecialCardsDeck}
import org.scalatest.{Matchers, WordSpec}

class CardSpec extends WordSpec with Matchers {
  "A Card" when {
    "created" should {
      val card = Card("2", "move", "grün")
      "have a CaseClass" in {
        card should not be null
      }
      "have a Symbol" in {
        card.getSymbol should be("2")
      }
      "have a Task" in {
        card.getTask should be("move")
      }
      "have a Color" in {
        card.getColor should be("grün")
      }
      "have a Color" in {
        card.toString should be("Card(2)")
      }
    }
  }

}

class CardLogicSpec extends WordSpec with Matchers {
  "A CardLogic" when {
    "created" should {
      val controller = new Controller
      val cardLogic = CardLogic
      val player = controller.createSetPlayer(List("p1", "p2", "p3", "p4"))
      val board = controller.setNewBoard(20)
      val mode = cardLogic.getLogic("move")
      "have a mode" in {
        mode should be(cardLogic.move)
      }
      "have a getLogic" in {
        cardLogic.getLogic("move") should be(cardLogic.move)
      }
      "have a Strategy" in {
        cardLogic.setStrategy(mode, player, board, 2, 2, 2) should not be null
      }
      "have a move" in {
        cardLogic.move should not be null
      }
    }
  }
}

class GenCardDeckSpec extends WordSpec with Matchers {
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


class CardDeckSpec extends WordSpec with Matchers {
  "A CardDeck" when {
    "created" should {
      val cardDeck = CardDeck
      "have a SpecialCardsDeck as List[Card]" in {
        cardDeck.apply() shouldBe a[List[Card]]
      }
    }
  }
}


class SpecialCardsDeckSpec extends WordSpec with Matchers {
  "A CardDeck" when {
    "generated" should {
      val specialCard = SpecialCardsDeck()
      "have a SpecialCardsDeck as List[Card]" in {
        specialCard.generateDeck shouldBe a[List[Card]]
      }
      "have a SpecialCardsDeck " in {
        specialCard.getCardDeck should be(List(Card("1 11 start", "move;move;start", "red"),
          Card("4", "forwardBackward", "red"),
          Card("7", "burn", "red"),
          Card("swap", "swap", "red"),
          Card("?", "joker", "red"),
          Card("13 play", "move;start", "red")))
      }
    }
  }
}

class NormalCardsDeckSpec extends WordSpec with Matchers {
  "A NormalCardsDeck" when {
    "generated" should {
      val normalCard = NormalCardsDeck()
      "have a NormalCardsDeck as List[Card]" in {
        normalCard.generateDeck shouldBe a[List[Card]]
      }
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

