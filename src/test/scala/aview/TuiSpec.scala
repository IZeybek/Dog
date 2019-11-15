package aview

import org.scalatest._

class TuiSpec extends WordSpec with Matchers {

  "Tui" when {
    "executed" should {
      val tui = new Tui
      "new board" in {
        tui.input("n board") should be("created a new board")
      }
      "new player" in {
        tui.input("n player") should be("created new players")
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
