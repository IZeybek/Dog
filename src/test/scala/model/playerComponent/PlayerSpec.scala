package model.playerComponent

import model.playerComponent.card.{CardTrait, ChangeCard, JokerCard, SevenCard}
import model.{Piece, Player}
import org.scalatest.{Matchers, WordSpec}

class PlayerSpec extends WordSpec with Matchers {
  "A player" when {
    "created" should {
      val cards: Array[CardTrait] = Array(SevenCard(), JokerCard(), ChangeCard())
      val player = Player("PlayerSpec", Map(0 -> Piece(0, "gelb"), 1 -> Piece(0, "gelb"), 2 -> Piece(0, "gelb"), 3 -> Piece(0, "gelb")))
      "have a name" in {
        player.name should not be empty
      }
      "be printed" in {
        player.toString should be(player.name)
      }
      "have cards" in {
      }
    }
  }
}
