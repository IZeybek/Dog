package dog.model.FileIOComponent.fileIOXmlImpl

import java.io.{File, PrintWriter}

import dog.controller.GameState
import dog.model.BoardComponent.{BoardTrait, CellTrait}
import dog.model.FileIOComponent.FileIOTrait
import dog.model.{Piece, Player}

import scala.xml.PrettyPrinter

class FileIO extends FileIOTrait {
  override def load: GameState = ???


  override def save(gameState: GameState): Unit = {
    val pw = new PrintWriter(new File("gamestate.xml"))
    val prettyPrinter = new PrettyPrinter(120, 4)
    val xml = prettyPrinter.format(gameStateToXml(gameState))
    pw.write(xml)
    pw.close()
  }

  def gameStateToXml(gameState: GameState): _root_.scala.xml.Node = {
    <gamestate>
      {gameState.players._1.foreach(x => playerToXml(x))
    gameState.board}
    </gamestate>
  }

  def playerToXml(player: Player) = {
    <player name={player.name} color={player.color} piece={pieceToXml(player.piece)}>
    </player>
  }

  def pieceToXml(piece: Map[Int, Piece]) = {
    <piece>
      {piece.foreach(x => x._2.position)}
    </piece>
  }

  def gridToXml(board: BoardTrait) = {
    <board size={board.getBoardMap.size.toString}>
      {board.getBoardMap.foreach(x => cellToXml(x))}
    </board>
  }

  def cellToXml(cell: CellTrait) = {
    <cell>
    </cell>
  }
}
