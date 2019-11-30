package model

import org.scalatest.{Matchers, WordSpec}

class PlayerSpec extends WordSpec with Matchers {
  "A player" when {
    "created" should {
      //      val cards: Array[CardTrait] = Array(SevenCard(), JokerCard(), ChangeCard())
      val player = Player("PlayerSpec", "gelb", Map(0 -> Piece(0), 1 -> Piece(0), 2 -> Piece(0), 3 -> Piece(0)), 4, null)
      "have a name" in {
        player.name should not be empty
      }
      "be printed" in {
        player.toString should be(player.name)
      }
      "have a color" in {
        player.color should be(player.color)
      }
      "have a map of pieces on the field" in {
        player.getPosition(0) should be(0)
      }
    }
  }
}
