package model.playerComponent

import model.Player
import model.playerComponent.card.{CardTrait, ChangeCard, JokerCard, SevenCard}
import org.scalatest.{Matchers, WordSpec}

class PlayerSpec extends WordSpec with Matchers {
  "A player" when {
    "created" should {
      val cards: Array[CardTrait] = Array(SevenCard(), JokerCard(), ChangeCard())
      val player = Player("PlayerSpec", cards, null)
      "have a name" in {
        player.name should not be empty
      }
      "be printed" in {
        player.toString should be(player.name)
      }
      "have cards" in {
        player.getCards should be(player.cards)
      }
    }
  }
}
