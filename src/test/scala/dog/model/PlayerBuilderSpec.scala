package dog.model

import dog.model.CardComponent.CardTrait
import dog.model.CardComponent.cardBaseImpl.Card
import org.scalatest.{Matchers, WordSpec}

class PlayerBuilderSpec extends WordSpec with Matchers {
  "A PlayerBuilder" when {
    "created" should {
      val playerBuilder = Player.PlayerBuilder()
      "have a CaseClass" in {
        playerBuilder.build() should not be null
      }
      "change piece number" in {
        playerBuilder.withPiece(3, 0)
        Player.pieceAmount should be(3)

        playerBuilder.withPiece(-1, 0)
        Player.pieceAmount should be(-1)
      }
      "change color" in {
        playerBuilder.withColor("lila")
        Player.color should be("lila")
      }
      "change name" in {
        playerBuilder.withName("Bob Ross", 0)
        Player.name should be("Bob Ross")
      }
      "change cards" in {
        val cardList: List[Card] = Card("0", "0", "0") :: Nil
        playerBuilder.withCards(cardList)
        Player.cardsDeck should be(cardList)
      }
      "set default attributes" in {
        Player.reset()
        Player.name should be("Bob")
        Player.color should be("blue")
        Player.pieceAmount should be(4)
        Player.cardsDeck.isInstanceOf[List[CardTrait]] should be(true)
      }
    }
  }
}
