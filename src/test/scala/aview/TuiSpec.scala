package aview



import controller.Controller
import org.scalatest._

class TuiSpec extends WordSpec with Matchers {

  "Tui" when {
    "executed" should {
      val controller = new Controller()
      val tui = new Tui(controller)
      var input = ""
      "new players" in {
        input = "n player Bobby BobRoss Ross"
        tui.processInput(input) should be("created 3 players")
      }
      "print cards" in {
        input = "p card"
        tui.processInput(input) should be("printed cards")
      }
      "print board" in {
        input = "p board"
        tui.processInput(input) should be("printed board")
      }
      "print game" in {
        input = "p"
        tui.processInput(input) should be("printed game")
      }
    }
  }
}


