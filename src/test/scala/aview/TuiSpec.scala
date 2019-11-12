package aview


import controller.controllerComponent.Controller
import org.scalatest._

class TuiSpec extends WordSpec with Matchers {

  "Tui" when {
    "executed" should {
      "have a Controller" can {
        val controller = new Controller()
        val tui = new Tui(controller)
        "create" in {
          tui.input("create") should be("create a new board")
        }
        "print" in {
          tui.input("p") should be("print your board")
        }
        "get a wrong input" in {
          tui.input("") should be("")
        }
      }
    }
  }


}
