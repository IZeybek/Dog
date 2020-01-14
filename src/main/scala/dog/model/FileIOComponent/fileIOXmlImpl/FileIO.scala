package dog.model.FileIOComponent.fileIOXmlImpl

import java.io.{File, PrintWriter}

import dog.controller.GameState
import dog.model.BoardComponent.boardBaseImpl.{Board, Cell}
import dog.model.BoardComponent.{BoardTrait, CellTrait}
import dog.model.CardComponent.CardTrait
import dog.model.CardComponent.cardBaseImpl.Card
import dog.model.FileIOComponent.FileIOTrait
import dog.model.{Piece, Player}

import scala.xml.{Elem, Node, NodeSeq, PrettyPrinter}

//@TODO implement last played Card
//@TODO implement card deck
class FileIO extends FileIOTrait {
  override def load: GameState = {
    val file: Elem = scala.xml.XML.loadFile("gamestate.xml")
    xmlToGameState(file)
  }

  def xmlToGameState(elem: Elem): GameState = {
    val actPlayer: Int = (elem \\ "gamestate" \ "@actPlayer").text.toInt
    var player: Vector[Player] = Vector.empty[Player]
    (elem \\ "player").foreach(x => player = player.:+(xmlToPlayer(x)))
    val board: BoardTrait = xmlToBoard((elem \\ "board").head, player)
    val cardDeckPointer: Int = (elem \\ "gamestate" \ "@cardDeckPointer").text.toInt

    var lastPlayedCard: CardTrait = xmlToCard((elem \\ "lastPlayed"))
    //    val card = xmlToCard( elem \\ "gamestate" \ "@lastPlayed"))
    println(lastPlayedCard)
    GameState(players = (player, actPlayer), (Vector.empty[CardTrait], cardDeckPointer), Option(lastPlayedCard), board)
  }

  def xmlToPlayer(elem: Node): Player = {
    val idx: Int = (elem \ "@idx").text.toInt
    val name: String = (elem \ "@name").text
    val color: String = (elem \ "@color").text
    val homePosition: Int = (elem \ "@homepos").text.toInt
    var inHouse: List[Int] = List.empty
    (elem \\ "inHouse").foreach(x => inHouse = xmlToInHouse(x) :: inHouse)
    var piece: Map[Int, Piece] = Map.empty
    (elem \\ "piece").foreach(x => piece = piece.+(xmlToPiece(x)))
    var card: List[CardTrait] = List.empty[CardTrait]
    (elem \\ "card").foreach(x => card = card.:+(xmlToCard(x)))
    Player((name, idx), color, piece, inHouse, card, homePosition)
  }

  def xmlToInHouse(elem: Node): Int = {
    val pieceNum: Int = (elem \ "@pieceNum").text.toInt
    pieceNum
  }

  def xmlToPiece(elem: Node): (Int, Piece) = {
    val pieceNum: Int = (elem \ "@pieceNum").text.toInt
    val pos: Int = (elem \ "@position").text.toInt
    (pieceNum, Piece(pos))
  }

  def xmlToCard(elem: NodeSeq): CardTrait = {
    val symbol: String = (elem \ "@symbol").text
    val task: String = (elem \ "@task").text
    val color: String = (elem \ "@color").text
    Card(symbol, task, color)
  }

  def xmlToBoard(elem: Node, player: Vector[Player]): BoardTrait = {
    val size: Int = (elem \ "@size").text.toInt
    var board: List[CellTrait] = List.empty[CellTrait]
    (elem \ "cell").foreach(x => board = board.:+(xmlToCell(x, player)))
    Board(Map.empty).fill((0 until size).map(x => (x, board(x))).toMap)
  }

  def xmlToCell(elem: Node, players: Vector[Player]): CellTrait = {
    val player: Int = (elem \ "@player").text.toInt
    Cell(player match {
      case -1 => None
      case _ => Some(players(player))
    })
  }

  //------------------------------------------------------------------------------------------------------------------

  override def save(gameState: GameState): Unit = {
    val pw = new PrintWriter(new File("gamestate.xml"))
    val prettyPrinter = new PrettyPrinter(120, 4)
    val xml = prettyPrinter.format(gameStateToXml(gameState))
    pw.write(xml)
    pw.close()
  }

  def gameStateToXml(gameState: GameState): Elem = {
    <gamestate actPlayer={gameState.players._2.toString} cardDeckPointer={gameState.cardDeck._2.toString}>
      {gameState.players._1.map(x => playerToXml(x))}{boardToXml(gameState.board)}{lastPlayedCardToXml(gameState.lastPlayedCardOpt)}
    </gamestate>
  }

  def lastPlayedCardToXml(card: Option[CardTrait]): Elem = {
    {
      card match {
        case Some(c) => <lastPlayed symbol={c.symbol} task={c.task} color={c.color}></lastPlayed>
        case None => <lastPlayed></lastPlayed>
      }
    }

  }

  def playerToXml(player: Player): Elem = {
    <player idx={player.nameAndIdx._2.toString} name={player.nameAndIdx._1} homepos={player.homePosition.toString} color={player.color}>
      {player.inHouse.map(x => inhouseToXml(x))}{player.piece.map(x => pieceToXml(x._1, x._2))}{player.cardList.map(x => cardToXml(x))}
    </player>
  }

  def inhouseToXml(x: Int): Elem = {
    <inHouse pieceNum={x.toString}></inHouse>
  }


  def pieceToXml(pieceNum: Int, piece: Piece): Elem = {
    <piece pieceNum={pieceNum.toString} position={piece.pos.toString}></piece>
  }

  def cardToXml(card: CardTrait): Elem = {
    <card symbol={card.symbol} task={card.task} color={card.color}></card>
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
