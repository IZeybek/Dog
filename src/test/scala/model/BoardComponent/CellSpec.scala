package model.BoardComponent

import model.BoardComponent.boardBaseImpl.Cell
import model.Player
import org.scalatest.{Matchers, WordSpec}

class CellSpec extends WordSpec with Matchers {
  "A Cell" when {
    "created" should {
      val player: Player = Player.PlayerBuilder().build()
      val cellSpecTrue: Cell = Cell(42, Some(player))
      val cellSpecFalse: Cell = Cell(24, None)
      "be filled" in {
        cellSpecTrue.p should be(Some(player))
      }
      "not be filled" in {
        cellSpecFalse.p should be(None)
      }
      "have idx" in {
        cellSpecTrue.idx should be(42)
      }
      "add Player to Cell" in {
        cellSpecFalse.addPlayerToCell(player).p should be(Some(player))
        cellSpecFalse.removePlayerFromCell()
      }
      "remove Player from Cell" in {
        cellSpecTrue.removePlayerFromCell().p should be(None)
        cellSpecFalse.addPlayerToCell(player).p should be(Some(player))
      }
      "be printed out" in {
        cellSpecTrue.toString should be("[" + player.consoleColor + "x" + Console.RESET + "]")
        cellSpecFalse.toString should be("[" + " " + "]")
      }
    }
  }
}
