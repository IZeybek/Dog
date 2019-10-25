
import aview.Base
import org.scalatest._

class TuiSpec extends WordSpec with Matchers {

  "A Base" when {
    "started" should {
      val base = Base
      "have a sentence" in {
        base should be (base)
      }
    }
  }


}
