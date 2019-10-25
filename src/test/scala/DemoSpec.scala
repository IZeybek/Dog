
import org.scalatest._

class DemoSpec extends WordSpec with Matchers {

  "A Person" when {
    "has" should {
      val text = "been"
      "gone" in {
        text should be("been")
      }
    }
  }


}
