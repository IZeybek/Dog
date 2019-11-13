import model.boardComponent.Cell

case class Board(size:Int) {
  val boardMap: Map[Int, Cell] = createBoard(size)

  def createBoard(size: Int): Map[Int, Cell] = {

    val boardMap = Map(0 -> Cell(false))

    for {
      i <- 0 until size

    } boardMap ++ createBoardPieces(i)

    boardMap
  }


  def createBoardPieces(player: Int): Map[Int, Cell] = {
    var startIndex = 0;
    player match {
      case 0 =>
        startIndex = 0;
      case 1 =>
        startIndex = 16;
      case 2 =>
        startIndex = 32;
      case 3 =>
        startIndex = 48;
      case 4 =>
        startIndex = 64;
      case 5 =>
        startIndex = 80;
      case _ =>
    }
    var boardPiece = Map(startIndex -> Cell(false))

    for {
      i <- (startIndex + 1) until (startIndex + 15)

    } boardPiece += (i -> Cell(false))
    boardPiece
  }


  override def toString(): String = {
    val blocknum = 25;
    val lineseparator = ("+-" + ("--" * blocknum)) * blocknum + "+\n"
    val line = ("| " + ("x " * blocknum)) * blocknum + "|\n"
    var box = "\n" + (lineseparator + (line * blocknum)) * blocknum + lineseparator
    for {
      i <- 0 until 64

    } box += box.replaceFirst("x", boardMap.get(i).toString)
    box
  }
}