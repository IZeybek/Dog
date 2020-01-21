package dog.model.FileIOComponent.fileIOXmlImpl

import dog.controller.ControllerComponent.ControllerTrait
import dog.controller.ControllerComponent.controllerBaseImpl.Controller
import dog.controller.StateComponent.GameState
import dog.model.BoardComponent.boardBaseImpl.Board
import dog.model.CardComponent.cardBaseImpl.Card
import org.scalatest.{Matchers, WordSpec}

class FileIOSpec extends WordSpec with Matchers {
  val fileIO: FileIO = new FileIO
  val controller: ControllerTrait = new Controller(new Board(20))
  val gameState: GameState = controller.gameState
  "a FileIO after save and load" should {
    "restore the old status" in {
      fileIO.save(gameState)
      fileIO.load.lastPlayedCard should be(Card("", "", ""))
    }
  }
}
