package model

import org.scalatest.{Matchers, WordSpec}

class PlayerBuilderSpec extends WordSpec with Matchers {
  "A PlayerBuilder" when {
    "created" should {
      val playerBuilder = Player.PlayerBuilder()
      "have a CaseClass" in {
        playerBuilder.build() should not be null
      }
    }
  }
}
