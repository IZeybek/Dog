package aview



import controller.Controller
import org.scalatest._

class TuiSpec extends WordSpec with Matchers {

  "Tui" when {
    "executed" should {
      val controller = new Controller()
      val tui = new Tui(controller)

      "new board" in {
        tui.input("n board") should be("created a new board")
      }
      "new player p1 p2 p3 p4" in {
        tui.input("n player p1 p2 p3 p4") should be("created new players")
      }
      "new player" in {
        tui.input("n player a") should be("creation failed!")
      }
      "new ..." in {
        tui.input("n test") should be("creation failed!")
      }
      "move" in {
        tui.input("m") should be("moved a player")
      }
      "print" in {
        tui.input("p") should be("printed board")
      }
    }
  }
}


