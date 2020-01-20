package dog.model

import dog.model.CardComponent.cardBaseImpl.Card
import org.scalatest.{Matchers, WordSpec}

class BuilderSpec extends WordSpec with Matchers {
  "A Builder" when {
    "created" should {
      val playerBuilder: PlayerBuilder = new PlayerBuilder()
      "have a CaseClass" in {
        playerBuilder.Builder().build() should not be null
      }
      "change piece number" in {
        playerBuilder.Builder().withPiece(3, 0)
        playerBuilder.pieceAmount should be(3)

        playerBuilder.Builder().withPiece(-1, 0)
        playerBuilder.pieceAmount should be(-1)
      }
      "change color" in {
        playerBuilder.Builder().withColor("lila")
        playerBuilder.color should be("lila")
      }
      "change nameAndIndex" in {
        playerBuilder.Builder().withName("Bob Ross", 0)
        playerBuilder.nameAndIndex._1 should be("Bob Ross")
      }
      "change cards" in {
        val cardList: List[Card] = Card("0", "0", "0") :: Nil
        playerBuilder.Builder().withCards(cardList)
        playerBuilder.cardsDeck should be(cardList)
      }
    }
  }
}
