package dog.controller.controllerBaseImpl.controllerStubImpl

import dog.controller.ControllerComponent.ControllerTrait
import dog.controller.ControllerComponent.controllerStubImpl.Controller
import dog.model.BoardComponent.boardBaseImpl.Board
import dog.model.CardComponent.cardBaseImpl.Card
import dog.model.Player
import org.scalatest.{Matchers, WordSpec}

class ControllerStubSpec extends WordSpec with Matchers {
  "A ControllerStub" when {
    "initialized" should {
      val controller: ControllerTrait = new Controller()
      "have an UndoManager" in {
        controller.undoManager should not be null
      }
      "have a GameStateMaster" in {
        controller.gameStateMaster should not be null
      }
      "have a GameState" in {
        controller.gameState should not be null
      }
      "print a Board" in {
        controller.toStringBoard should be("Board")
      }
      "print Houses" in {
        controller.toStringHouse should be("House")
      }
      "print Garage" in {
        controller.toStringGarage should be("Garage")
      }
      "print CardDeck" in {
        controller.toStringCardDeck should be("CardDeck")
      }
      "print Player hand Cards" in {
        controller.toStringPlayerHands should be("PlayerHand")
      }
      "give Player Cards" in {
        controller.givePlayerCards(0, Nil) should be(Player(("Charlie", 50), "black", Map.empty, Nil, new Board(20), Nil, 20))
      }
      "draw Cards" in {
        controller.drawCards(0) should be(Card("5", "move", "blau") :: Nil)
      }
      "create Players" in {
        controller.createPlayers(List.empty, 0, 6).isEmpty should be(true)
      }
      "create a new Board of size 10" in {
        controller.createNewBoard(10) should be(new Board(10))
      }
      "create a new Board of size 20" in {
        controller.createNewBoard should be(new Board(20))
      }
      "create a CardDeck" in {
        val (cards, pointer) = controller.createCardDeck(List.empty)
        cards.isEmpty should be(true)
        pointer should be(0)
      }
    }
  }
}
