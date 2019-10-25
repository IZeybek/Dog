
import aview.Tui
import org.scalatest._

class TuiSpec extends WordSpec with Matchers {

  "A Base" when {
    "started" should {
      val tui = Tui
      val input = "create"
      "have a sentence" in {
        tui.input("create") should be ()
      }
    }
  }


}
