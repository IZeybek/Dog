package model.playerComponent

import model.playerComponent.card.{ChangeCard, JokerCard, SevenCard}
import model.{Piece, Player}
import org.scalatest.{Matchers, WordSpec}

class CardSpec extends WordSpec with Matchers {
  "A Card" when {
    val playerTest = Player("CardSpec", Map(0 -> Piece(0, "gelb"), 1 -> Piece(0, "gelb"), 2 -> Piece(0, "gelb"), 3 -> Piece(0, "gelb")))
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
