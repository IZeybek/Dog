package dog.model.BoardComponent.boardMockImpl

import dog.model.BoardComponent.BoardTrait
import dog.model.BoardComponent.boardBaseImpl.Cell
import dog.model.Player
import dog.model.Player.PlayerBuilder

class Board(var size: Int) extends BoardTrait {

  override def updateMovePlayer(player: Player, pieceNum: Integer, setPos: Integer): BoardTrait = this

  override def updateSwapPlayers(player: Vector[Player], playerNums: List[Int], pieceNums: List[Int]): BoardTrait = this

  override def checkOverrideOtherPlayer(player: Player, pieceNum: Integer, newPos: Integer): Boolean = true

  override def getBoardMap: Map[Int, Cell] = Map(0 -> Cell(0, Some(PlayerBuilder().build())))

  override def createNewBoard: BoardTrait = this

  override def copy(boardMap: Map[Int, Cell]): BoardTrait = copy(boardMap)
}
