package model

import org.scalatest.{Matchers, WordSpec}

class CellSpec extends WordSpec with Matchers {
  "A Cell" when {
    "created" should {
      val player: Player = new Player(name = "CellSpec", c = "grün", pieceQuantity = 4)
      val cellSpecTrue: Cell = Cell(42, Some(player))
      val cellSpecFalse: Cell = Cell(24, None())
      "be filled" in {
        cellSpecTrue.p should be(Some(player))
      }
      "not be filled" in {
        cellSpecFalse.p should be(None())
      }
      "have idx" in {
        cellSpecTrue.idx should be(42)
      }
    }
  }
}
