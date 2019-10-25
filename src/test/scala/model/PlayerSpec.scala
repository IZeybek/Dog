package model

import org.scalatest.{Matchers, WordSpec}

class PlayerSpec extends WordSpec with Matchers {
  "A player" when {
    "created" should {
      val player = Player("PlayerSpec")
      "have a name" in {
        player.name in be(String)
      }
    }
  }
}
