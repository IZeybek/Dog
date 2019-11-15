package model.playerComponent

import model.playerComponent.card.{ChangeCard, JokerCard, SevenCard}
import org.scalatest.{Matchers, WordSpec}

class CardSpec extends WordSpec with Matchers {
  "A Card" when {
    //    val cardSpecPlayer = Player("CardSpec", "gelb", Map(0 -> new Piece(0), 1 -> new Piece(0), 2 -> new Piece(0), 3 -> new Piece(0)))
    "as ChangeCard" should {
      val changeCard = ChangeCard()
      "have a function" in {
        print("the player can switch his Piece with one of another player")
      }
      "be printed" in {
        print(changeCard)
      }
    }
    "as SevenCard" should {
      val sevenCard = SevenCard()
      "have a function" in {
        print("the player can share its points between his Pieces")
      }
    }
    "as JokerCard" should {
      val jokerCard = JokerCard()
      "have a function" in {
        print("the player can transform this card in any card he wishes")
      }
    }
  }
}
