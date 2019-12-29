package dog.model.BoardComponent

import dog.controller.InputCardMaster
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
      "create a board" in {
        board.createNewBoard.size should be(20)
      }
      "return map " in {
        (0 until board.size).foreach(i => board.cell(i) should not be null)
      }
      "check if player has to be overridden" in {
        val player = Player(("P1", 0), "grün", Map(0 -> Piece(6)), 3, 0, Nil)
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
      "swap two player" in {
        val player1: Player = Player.PlayerBuilder().withPieces(Map(0 -> Piece(0))).withColor("lila").build()
        player1.getPosition(0) should be(0)
        val player2: Player = Player.PlayerBuilder().withPieces(Map(0 -> Piece(10))).withColor("grau").build()
        player2.getPosition(0) should be(10)
        board = board.fill(Map(0 -> Cell(Some(player1)), 10 -> Cell(Some(player2))))
        val inputCard = InputCardMaster.UpdateCardInput()
          .withActualPlayer(0)
          .withOtherPlayer(1)
          .withPieceNum(List(0, 0))
          .withCardNum((0, 0))
          .withMoveBy(0)
          .buildCardInput()
        board = board.updateSwapPlayers(Vector(player1, player2), inputCard)
        board.cell(10).getColor should be("grau")
        board.cell(0).getColor should be("lila")
      }
    }
  }
}
