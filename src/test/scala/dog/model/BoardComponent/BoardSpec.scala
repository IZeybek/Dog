package dog.model.BoardComponent

import dog.model.BoardComponent.boardBaseImpl.{Board, Cell}
import dog.model.{Piece, Player}
import org.scalatest.{Matchers, WordSpec}

class BoardSpec extends WordSpec with Matchers {
  "A Board" when {
    "created" should {
      var board: BoardTrait = new Board(20)
      "have a Map" when {
        "created" in {
          (0 until board.size).foreach(i => board.cell(i) should be(i))
        }
      }
      "return map " in {
        (0 until board.size).foreach(i => board.cell(i) should not be null)
      }
      "check if player has to be overridden" in {
        board = board.fill(Map(6 -> Cell(Some(Player("P1", "grün", Map(1 -> Piece(6)), 3, 0, Nil)))))
        board.checkOverrideOtherPlayer(Player("P1", "grün", Map(0 -> Piece(0), 1 -> Piece(6)), 3, 0, Nil), 0, 6) should be(true)
        board.checkOverrideOtherPlayer(Player("P1", "grün", Map(0 -> Piece(0), 1 -> Piece(6)), 3, 0, Nil), 0, 3) should be(false)
      }
    }
  }
}
