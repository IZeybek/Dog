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
      <player>
        {gameState.players._1.map(x => playerToXml(x))}
      </player>
      <board>
        {boardToXml(gameState.board)}
      </board>
    </gamestate>
  }

  def playerToXml(player: Player): Elem = {
    <player idx={player.nameAndIdx._2.toString} name={player.nameAndIdx._1} color={player.color}>
      <piece>
        {player.piece.map(x => pieceToXml(x._1, x._2))}
      </piece>
      <card>
        {player.cardList.map(x => cardToXml(x))}
      </card>
    </player>
  }

  def pieceToXml(pieceNum: Int, piece: Piece): Elem = {
    <piece pieceNum={pieceNum.toString} position={piece.pos.toString}>
    </piece>
  }

  def cardToXml(card: CardTrait): Elem = {
    <card symbol={card.symbol} task={card.task} color={card.color}>
    </card>
  }

  def boardToXml(board: BoardTrait): Elem = {
    <board size={board.size.toString}>
      {(0 until board.size).map(x => cellToXml(board.cell(x)))}
    </board>
  }

  def cellToXml(cell: CellTrait): Elem = {
    <cell player={cell.p match {
      case Some(p) => p.nameAndIdx._2.toString
      case None => "-1"
    }}>
    </cell>
  }
}
