package dog.model.BoardComponent

import dog.controller.StateComponent.InputCardMaster
import dog.model.BoardComponent.boardBaseImpl.{Board, Cell}
import dog.model.{Piece, Player}
import org.scalatest.{Matchers, WordSpec}

class BoardSpec extends WordSpec with Matchers {
  "A Board" when {
    "created" should {
      var board: BoardTrait = new Board(28)
      "have a Map" when {
        "created" in {
          (0 until board.size).foreach(i => board.cell(i) should not be null)
        }
      }
      "create a board" in {
        board.createNewBoard.size should be(28)
      }
      "return map " in {
        (0 until board.size).foreach(i => board.cell(i) should not be null)
      }
      "check if player has to be overridden" in {
        val player = Player(("P1", 0), "green", Map(0 -> Piece(6)), List(0, 1, 2), Nil, 0)
        board = board.fill(Cell(Some(player)), 6)
        board.checkOverrideOtherPlayer(player, 6) should be(true)
        board.checkOverrideOtherPlayer(player, 3) should be(false)
      }
      "fill a board with a Map" in {
        val player = Player.PlayerBuilder().build()
        board = board.fill(Map(8 -> Cell(Some(player)), 3 -> Cell(None)))
        board.cell(8).isFilled should be(true)
        board.cell(3).isFilled should be(false)
      }
      "swap two player" in {
        val player1: Player = Player.PlayerBuilder().withPiece(1, 0).withColor("violet").build()
        player1.piece(0).pos should be(0)

        val player2: Player = Player.PlayerBuilder().withPiece(1, 10).withColor("gray").build()
        player2.piece(0).pos should be(10)

        board = board.fill(Map(0 -> Cell(Some(player1)), 10 -> Cell(Some(player2))))
        val inputCard = InputCardMaster.UpdateCardInput()
          .withActualPlayer(0)
          .withOtherPlayer(1)
          .withPieceNum(List(0, 0))
          .withCardNum((0, 0))
          .buildCardInput()
        board = board.updateSwapPlayers(player1, player2, inputCard.selPieceList)
        board.cell(10).getColor should be("gray")
        board.cell(0).getColor should be("violet")
      }
    }
  }
}
