package dog.controller.controllerBaseImpl.controllerStubImpl

import dog.controller.ControllerComponent.ControllerTrait
import dog.controller.ControllerComponent.controllerStubImpl.Controller
import org.scalatest.{Matchers, WordSpec}

class ControllerSpec extends WordSpec with Matchers {
  "A Controller" when {
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
      "create Players" in {
        controller.createPlayers(List.empty, 0, 6).isEmpty should be(true)
      }
      "create a CardDeck" in {
        val (cards, pointer) = controller.createCardDeck(List.empty)
        cards.isEmpty should be(true)
        pointer should be(0)
      }
    }
  }
}
