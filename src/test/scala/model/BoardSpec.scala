package model

import controller.Controller
import org.scalatest.{Matchers, WordSpec}

class BoardSpec extends WordSpec with Matchers {
  "A Board" when {
    "created" should {
      val controller = new Controller
      val board = controller.createBoard
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
        /*
        "output created" in {
          var sum = 0;

          for (i <- 0 until 21; j <- 0 until (21)) {
            sum += {
              if (board.getArrayOutput(i)(j) != null) board.getArrayOutput(i)(j).getPos else 0
            }
          }
          sum should be(2017)
        } */
        //        "makeString executed" in {
        //          var box = ""
        //          for (i <- 0 until board.getBoardMap.size) box += board.getBoardMap(i).toString
        //          board.makeString() should be(box)
        //        }
      }
    }
  }
}
