package dog.model.BoardComponent

import dog.model.BoardComponent.boardBaseImpl.Cell
import dog.model.Player
import org.scalatest.{Matchers, WordSpec}

class CellSpec extends WordSpec with Matchers {
  "A Cell" when {
    "created" should {
      val player: Player = Player.PlayerBuilder().build()
      val cellSpecTrue: Cell = Cell(Some(player), Some(0))
      val cellSpecFalse: Cell = Cell(None, None)
      "be filled" in {
        cellSpecTrue.p should be(Some(player))
      }
      "not be filled" in {
        cellSpecFalse.p should be(None)
      }
      "add Player to Cell" in {
        cellSpecFalse.addPlayerToCell(player, 0).p should be(Some(player))
        cellSpecFalse.removePlayerFromCell()
      }
      "remove Player from Cell" in {
        cellSpecTrue.removePlayerFromCell().p should be(None)
        cellSpecFalse.addPlayerToCell(player, 0).p should be(Some(player))
      }
      "be printed out" in {
        cellSpecTrue.toString should be("[" + player.consoleColor + "x" + Console.RESET + "]")
        cellSpecFalse.toString should be("[" + " " + "]")
      }
    }
  }
}
