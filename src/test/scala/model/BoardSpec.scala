package model

import controller.Controller
import org.scalatest.{Matchers, WordSpec}

class BoardSpec extends WordSpec with Matchers {
  "A Board" when {
    "created" should {
      val controller = new Controller
      val board = controller.setNewBoard(16)
      "have a Map" when {
        "created" in {
          for (i <- 0 until board.getBoardMap.size) {
            board.getBoardMap(i).getPos should be(i)
          }
        }
        "return map " in {
          for (i <- 0 until board.getBoardMap.size) {
            board.getBoardMap(i).getPos should be(i)
          }
        }
      }
    }
  }
}
