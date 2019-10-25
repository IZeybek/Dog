package aview


import org.scalatest._

class TuiSpec extends WordSpec with Matchers {

  "Tui" when {
    "executed" should {
      val tui = Tui()
      "created" in {
        tui.input("create") should be("empty board created")
      }
      "set" in {
        tui.input("set") should be("Player set")
      }
      "printed" in {
        tui.input("p") should be("board printed")
      }
      "searched" in {
        tui.input("s") should be("player cell can not be found since its not created yet")
      }
    }
  }


}
