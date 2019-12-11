package model

import controller.Controller
import org.scalatest.{Matchers, WordSpec}

class BoardSpec extends WordSpec with Matchers {
  "A Board" when {
    "created" should {
      val controller = new Controller()
      var board = controller.createNewBoard(16)
      "have a Map" when {
        "created" in {
          (0 until board.boardMap.size).foreach(i => board.boardMap(i).idx should be(i))
        }
      }
      "return map " in {
        (0 until board.boardMap.size).foreach(i => board.boardMap(i).idx should be(i))
      }
      "check if player has to be overridden" in {
        board = board.copy(board.boardMap.updated(6, Cell(6, Some(Player("P1", "grün", Map(1 -> Piece(6)), 3, 0, Nil)))))
        board.checkOverrideOtherPlayer(Player("P1", "grün", Map(0 -> Piece(0), 1 -> Piece(6)), 3, 0, Nil), 0, 6) should be(true)
        board.checkOverrideOtherPlayer(Player("P1", "grün", Map(0 -> Piece(0), 1 -> Piece(6)), 3, 0, Nil), 0, 3) should be(false)
      }
    }
  }
}
