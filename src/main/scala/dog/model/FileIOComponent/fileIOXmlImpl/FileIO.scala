package dog.model.FileIOComponent.fileIOXmlImpl

import java.io.{File, PrintWriter}

import dog.controller.GameState
import dog.model.BoardComponent.{BoardTrait, CellTrait}
import dog.model.CardComponent.CardTrait
import dog.model.FileIOComponent.FileIOTrait
import dog.model.{Piece, Player}

import scala.xml.{Elem, PrettyPrinter}

class FileIO extends FileIOTrait {
  override def load: GameState = ???


  override def save(gameState: GameState): Unit = {
    val pw = new PrintWriter(new File("gamestate.xml"))
    val prettyPrinter = new PrettyPrinter(120, 4)
    val xml = prettyPrinter.format(gameStateToXml(gameState))
    pw.write(xml)
    pw.close()
  }

  def gameStateToXml(gameState: GameState): Elem = {
    <gamestate actPlayer={gameState.players._2.toString} cardDeckPointer={gameState.cardDeck._2.toString}>
    </gamestate>
  }

  def playerToXml(player: Player): Elem = {
    <player name={player.nameAndIdx._1} color={player.color} piece={pieceToXml(player.piece)}>
    </player>
  }

  def pieceToXml(piece: Map[Int, Piece]): Elem = {
    <piece>
      {piece.foreach(x => x._2.pos)}
    </piece>
  }

  def cardToXml(card: CardTrait): Elem = {
    <card>
      {card}
    </card>
  }

  def boardToXml(board: BoardTrait): Elem = {
    <board size={board.size.toString}>
      {(0 until board.size).foreach(x => cellToXml(board.cell(x)))}
    </board>
  }

  def cellToXml(cell: CellTrait): Elem = {
    <cell player={cell.p match {
      case Some(p) => playerToXml(p).toString()
    }}>
    </cell>
  }
}
