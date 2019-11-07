package aview

import org.scalatest.{Matchers, WordSpec}

class BoardSpec extends WordSpec with Matchers{


  "A Base/Main" when{
    "started" should{
      val s = new Array[String](1)
      s(0) = "hallo"
      "run" in {
        Base.main(s) should
      }
    }

  }

}
