package model.boardComponent

import model.Board
import org.scalatest.{Matchers, WordSpec}

class BoardSpec extends WordSpec with Matchers {
  "A Board" when {
    "created" should {
      val board = Board()
      "have a Map" when {
        "created" in {
          board.getBoardMap.size should be (64)
          for(i <- 0 until  board.getBoardMap.size){
            board.getBoardMap(i).getPos should be (i)
          }
        }
        "output created" in {
          var sum = 0;

          for(i <- 0 until 21; j <- 0 until(21)){
            sum += {if(board.getArrayOutput(i)(j) != null) board.getArrayOutput(i)(j).getPos else 0}
          }
          sum should be (2017)
        }
        "toString executed" in {
          var box = ""
          //output as an array!
          for (i <- 0 to 20) {
            for (j <- 0 to 20) {
              if (board.getArrayOutput(i)(j) != null) box += board.getArrayOutput(i)(j).toString
              else box += " -- "
            }
            box += "\n"
          }
          board.toString should be (box)
        }
      }

    }
  }
}
