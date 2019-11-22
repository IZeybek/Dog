package model

import controller.Controller
import org.scalatest.{Matchers, WordSpec}

class CardSpec extends WordSpec with Matchers {
  "A Card" when {
    val player = new Controller().setPlayer(Array("P1", "P2", "P3", "P4"))
    "a ChangeCard" should {
      val changeCard = ChangeCard()
      "have a function" in {
        changeCard.cardFunc(0, player) should be(player)
      }
      "be printed" in {
        print(changeCard)
      }
    }
    "a SevenCard" should {
      val sevenCard = SevenCard()
      "have a function" in {
        sevenCard.cardFunc(0, player) should be(player)
      }
      "be printed" in {
        print(sevenCard)
      }
    }
    "a JokerCard" should {
      val jokerCard = JokerCard()
      "have a function" in {
        jokerCard.cardFunc(0, player) should be(player)
      }
      "be printed" in {
        println("should be printed then")
      }
    }
  }
}
