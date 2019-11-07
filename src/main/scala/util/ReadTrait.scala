package util

import model.Main.Board

trait ReadTrait {
  /**
   * prints the field
   */
  def prettyPrint(): Unit

  /**
   * reads ins field from path
   *
   * @param path to a file
   * @return a new created Board with a field
   */
  def readIn(path: String): Board
}
