package dog.model.FileIOComponent.fileIOJsonImpl

import dog.controller.StateComponent.GameState
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
    val source = Source.fromFile("gameState.json")
    val sourceString: String = source.getLines.mkString
    val json: JsValue = Json.parse(sourceString)
    source.close()
    jsonToGameState(json)
  }

  def jsonToGameState(json: JsValue): GameState = {
    val actPlayer: Int = (json \ "gameState" \ "actPlayer").as[Int]
    var playerVector: Vector[Player] = Vector.empty[Player]
    (json \ "gameState" \ "playerVector" \\ "player").foreach(x => playerVector = playerVector.:+(jsonToPlayer(x)))
    val board: BoardTrait = jsonToBoard((json \\ "board").head, playerVector)
    val cardDeckPointer: Int = (json \ "gameState" \ "cardDeckPointer").as[Int]
    val lastPlayedCard: CardTrait = jsonToCard((json \ "gameState" \\ "lastPlacedCard").head)
    GameState(players = (playerVector, actPlayer), (Vector.empty[CardTrait], cardDeckPointer), Option(lastPlayedCard), board, None)
  }

  implicit val playerWrites: Writes[Player] = (player: Player) => Json.obj(
    "idx" -> player.nameAndIdx._2,
    "name" -> JsString(player.nameAndIdx._1),
    "homepos" -> JsNumber(player.homePosition),
    "inHouse" -> player.inHouse,
    "garage" -> Json.toJson(Json.toJson(player.garage)),
    "color" -> JsString(player.color),
    "piecesVector" -> player.piece.map(x => Json.obj("piece" -> Json.toJson(pieceMap(x._1, x._2)))),
    "CardList" -> player.cardList.map(x => Json.obj("card" -> Json.toJson(x))),
  )

  def jsonToPiece(elem: JsValue): (Int, Piece) = {
    val pieceNum: Int = (elem \ "pieceNum").as[Int]
    val pos: Int = (elem \ "position").as[Int]
    (pieceNum, Piece(pos))
  }

  def jsonToCard(elem: JsValue): CardTrait = {
    val symbol: String = (elem \ "symbol").as[String]
    val task: String = (elem \ "task").as[String]
    val color: String = (elem \ "color").as[String]
    Card(symbol, task, color)
  }

  def jsonToBoard(elem: JsValue, playerVector: Vector[Player]): BoardTrait = {
    val size: Int = (elem \ "size").as[Int]
    var board: List[CellTrait] = List.empty[CellTrait]

    (elem \ "boardMap" \\ "cell").foreach(x => board = board.:+(jsonToCell(x, playerVector)))

    Board(Map.empty).fill((0 until size).map(x => (x, board(x))).toMap)
  }

  def jsonToCell(elem: JsValue, players: Vector[Player]): CellTrait = {

    val player: Int = (elem \ "player").as[Int]
    Cell(player match {
      case -1 => None
      case _ => Some(players(player))
    })
  }


  override def save(gameState: GameState): Unit = {
    import java.io._
    val pw = new PrintWriter(new File("gameState.json"))
    pw.write(Json.prettyPrint(gameStateToJson(gameState)))
    pw.close()
  }

  def gameStateToJson(gameState: GameState): JsValue = {
    Json.obj(
      "gameState" -> Json.obj(
        "actPlayer" -> JsNumber(gameState.actualPlayer.nameAndIdx._2),
        "cardDeckPointer" -> JsNumber(gameState.cardDeck._2),
        "playerVector" -> gameState.players._1.map(x => Json.obj("player" -> Json.toJson(x))),
        "board" -> Json.toJson(gameState.board),
        "lastPlacedCard" -> Json.toJson(gameState.lastPlayedCard)
      )
    )
  }

  implicit val boardWrites: Writes[BoardTrait] = (board: BoardTrait) => Json.obj(
    "size" -> JsNumber(board.size),
    "boardMap" -> (for {
      idx <- 0 until board.size
    } yield {
      Json.obj("idx" -> idx, "cell" -> Json.toJson(board.cell(idx)))
    })
  )

  implicit val cellWrites: Writes[CellTrait] = (cell: CellTrait) => Json.obj(
    "player" -> {
      cell.p match {
        case Some(p) => p.nameAndIdx._2
        case None => -1
      }
    },
  )

  def jsonToPlayer(elem: JsValue): Player = {
    val idx: Int = (elem \ "idx").as[Int]
    val name: String = (elem \ "name").as[String]
    val color: String = (elem \ "color").as[String]
    val homePosition: Int = (elem \ "homepos").as[Int]
    val inHouse = (elem \ "inHouse").as[List[Int]]
    var pieceMap: Map[Int, Piece] = Map.empty
    (elem \\ "piece").foreach(x => pieceMap = pieceMap.+(jsonToPiece(x)))
    var card: List[CardTrait] = List.empty[CardTrait]
    (elem \\ "card").foreach(x => card = card.:+(jsonToCard(x)))
    Player((name, idx), color, pieceMap, inHouse, null, card, homePosition)
  }

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
