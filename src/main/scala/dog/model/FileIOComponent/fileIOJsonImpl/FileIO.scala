package dog.model.FileIOComponent.fileIOJsonImpl

import dog.controller.GameState
import dog.model.BoardComponent.boardBaseImpl.{Board, Cell}
import dog.model.BoardComponent.{BoardTrait, CellTrait}
import dog.model.CardComponent.CardTrait
import dog.model.CardComponent.cardBaseImpl.Card
import dog.model.FileIOComponent.FileIOTrait
import dog.model.{Piece, Player}
import play.api.libs.json._

import scala.io.Source

class FileIO extends FileIOTrait {
  override def load: GameState = {
    val source: String = Source.fromFile("grid.json").getLines.mkString
    val json: JsValue = Json.parse(source)
    jsonToGameState(json)
  }

  def jsonToGameState(json: JsValue): GameState = {
    val actPlayer: Int = (json \ "gameState" \ "actPlayer").get.toString.toInt
    var player: Vector[Player] = Vector.empty[Player]
    (json \\ "playerVector").indices.foreach(x => player = player.:+(jsonToPlayer((json \\ "playerVector") (x))))
    val board: BoardTrait = jsonToBoard((json \\ "board").head, player)
    val cardDeckPointer: Int = (json \ "gameState" \ "cardDeckPointer").get.toString().toInt
    GameState(players = (player, actPlayer), (Vector.empty[CardTrait], cardDeckPointer), lastPlayedCardOpt = None, board)
  }

  def jsonToPlayer(elem: JsValue): Player = {
    val idx: Int = (elem \ "idx").get.toString.toInt
    val name: String = (elem \ "name").get.toString
    val color: String = (elem \ "color").get.toString
    val homePosition: Int = (elem \ "homepos").get.toString.toInt
    var piece: Map[Int, Piece] = Map.empty
    (elem \\ "piece").foreach(x => piece = piece.+(jsonToPiece(x)))
    var card: List[CardTrait] = List.empty[CardTrait]
    (elem \\ "card").foreach(x => card = card.:+(xmlToCard(x)))
    Player((name, idx), color, piece, Nil, card, homePosition)
  }

  def jsonToPiece(elem: JsValue): (Int, Piece) = {
    val pieceNum: Int = (elem \ "pieceNum").get.toString.toInt
    val pos: Int = (elem \ "@position").get.toString.toInt
    (pieceNum, Piece(pos))
  }

  def xmlToCard(elem: JsValue): CardTrait = {
    val symbol: String = (elem \ "symbol").get.toString
    val task: String = (elem \ "task").get.toString
    val color: String = (elem \ "color").get.toString
    Card(symbol, task, color)
  }

  def jsonToBoard(elem: JsValue, player: Vector[Player]): BoardTrait = {
    val size: Int = (elem \ "size").get.toString.toInt
    var board: List[CellTrait] = List.empty[CellTrait]

    (elem \\ "cell").foreach(x => board = board.:+(jsonToCell(x, player)))

    Board(Map.empty).fill((0 until size).map(x => (x, board(x))).toMap)
  }

  def jsonToCell(elem: JsValue, players: Vector[Player]): CellTrait = {
    val player: Int = (elem \ "player").get.toString.toInt
    Cell(player match {
      case -1 => None
      case _ => Some(players(player))
    })
  }



  override def save(gameState: GameState): Unit = {
    import java.io._
    val pw = new PrintWriter(new File("grid.json"))
    pw.write(Json.prettyPrint(gameStateToJson(gameState)))
    pw.close()
  }

  def gameStateToJson(gameState: GameState): JsValue = {
    Json.obj(
      "gameState" -> Json.obj(
        "actPlayer" -> JsNumber(gameState.actualPlayer.nameAndIdx._2),
        "cardDeckPointer" -> JsNumber(gameState.cardDeck._2),
        "playerVector" -> gameState.players._1.map(x => Json.obj("player" -> Json.toJson(x))),
        "board" -> Json.toJson(gameState.board)
      )
    )
  }

  implicit val boardWrites: Writes[BoardTrait] = (board: BoardTrait) => Json.obj(
    "size" -> JsNumber(board.size),
    "boardMap" -> (for {
      idx <- 0 until board.size
    } yield {
      Json.obj("idx" -> idx, "Cell" -> Json.toJson(board.cell(idx)))
    })
  )

  implicit val cellWrites: Writes[CellTrait] = (cell: CellTrait) => Json.obj(
    "player" -> JsBoolean(cell.isFilled),
  )

  implicit val playerWrites: Writes[Player] = (player: Player) => Json.obj(
    "idx" -> JsNumber(player.nameAndIdx._2),
    "name" -> JsString(player.nameAndIdx._1),
    "homepos" -> JsNumber(player.homePosition),
    "color" -> JsString(player.color),
    "piecesVector" -> player.piece.map(x => Json.obj("piece" -> Json.toJson(pieceMap(x._1, x._2)))),
    "CardList" -> player.cardList.map(x => Json.obj("Card" -> Json.toJson(x))),
  )

  case class pieceMap(pieceNum: Int, piece: Piece)

  implicit val pieceWrites: Writes[pieceMap] = (pieces: pieceMap) => Json.obj(
    "pieceNum" -> JsNumber(pieces.pieceNum),
    "position" -> JsNumber(pieces.piece.pos),
  )

  implicit val cardWrites: Writes[CardTrait] = (card: CardTrait) => Json.obj(
    "symbol" -> JsString(card.symbol),
    "task" -> JsString(card.task),
    "color" -> JsString(card.color),
  )
}
