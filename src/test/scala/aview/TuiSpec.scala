package aview


import org.scalatest._

class TuiSpec extends WordSpec with Matchers {

  "Tui" when {
    "executed" should {
      val tui = Tui()
      "have a sentence" in {
        tui.input("create") should be ("empty board created")
      }
    }
  }


}
