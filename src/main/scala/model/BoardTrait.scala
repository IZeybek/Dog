package model

import model.Main.Cell

trait BoardTrait {
  val xy: Array[Int]
  val cells: Array[Cell]
}
