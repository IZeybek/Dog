package aview


import aview.gui.Gui
import controller.Controller
import org.scalatest._

class TuiSpec extends WordSpec with Matchers {

  "Tui" when {
    "executed" should {
      val controller = new Controller()
      val gui = new Gui(controller)
      val tui = new Tui(gui, controller)
      var input = ""
      "create new players" in {
        input = "n player Bobby BobRoss Ross"
        tui.processInput(input) should be("created 3 players")
      }
      "create one player" in {
        input = "n player Bob"
        tui.processInput(input) should be("created 1 player")
      }
      "create no player" in {
        input = "n player"
        tui.processInput(input) should be("no players created")
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
      "do nothing" in {
        input = ""
        tui.processInput(input) should be("")
      }
    }
  }
}


