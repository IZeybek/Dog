package util

import model.BoardTrait
import model.Main.Board

trait ReadTrait {
  /**
   * prints the field
   */
  def prettyPrint(b: BoardTrait): Unit

  /**
   * reads ins field from path
   *
   * @param path to a file
   * @return a new created Board with a field
   */
  def readIn(path: String): Board
}
