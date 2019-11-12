package model

import model.Main.Player
import org.scalatest.{Matchers, WordSpec}

class PlayerSpec extends WordSpec with Matchers {
  "A player" when {
    "created" should {
      val player = Player("PlayerSpec", null)
      val isNotEmpty = (s: String) => s != null
      "have a name" in {
        player.name should not be empty
      }
      "toString" in {
        player.toString should be(player.name)
      }
    }
  }
}
