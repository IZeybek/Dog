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
          (0 until board.size).foreach(i => board.cell(i) should not be null)
        }
      }
      "return map " in {
        (0 until board.size).foreach(i => board.cell(i) should not be null)
      }
      "check if player has to be overridden" in {
        val player = Player("P1", "grün", Map(0 -> Piece(6)), 3, 0, Nil)
        board = board.fill(Cell(Some(player)), 6)
        board.checkOverrideOtherPlayer(player, 0, 6) should be(true)
        board.checkOverrideOtherPlayer(player, 0, 3) should be(false)
      }
      "fill a board with a Map" in {
        val player = Player.PlayerBuilder().build()
        board = board.fill(Map(8 -> Cell(Some(player)), 3 -> Cell(None)))
        board.cell(8).isFilled should be(true)
        board.cell(3).isFilled should be(false)
      }
    }
  }
}
