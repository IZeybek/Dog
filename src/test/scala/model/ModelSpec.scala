package model

import model.boardComponent.{Board, Cell, Read}
import org.scalatest.{Matchers, WordSpec}

class ModelSpec extends WordSpec with Matchers {
  "A Model" when {
    "initialized" should {
      val m: ModelTrait = Model()
      val b: Board = Read.createBoard("src/feld.txt")
      "have an uninitialized board" in {
        m.board should be(null)
      }
      "have an uninitialized player" in {
        m.player should be(null)
      }
      "create a new Board" in {

        def checkCells(c1: Array[Cell], c2: Array[Cell]): Boolean = {
          var z = -1
          for (elem <- c1) {
            z += 1
            if (elem != c2(z)) false
          }
          true
        }

        checkCells(m.createBoard.cells, b.cells) should be(true)
      }
      "return a consisting Board" in {
        m.board should be(b)
      }
    }
  }
}
