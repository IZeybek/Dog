package dog.model.FileIOComponent.fileIOJsonImpl

import dog.controller.GameState
import dog.model.BoardComponent.{BoardTrait, CellTrait}
import dog.model.CardComponent.CardTrait
import dog.model.FileIOComponent.FileIOTrait
import dog.model.{Piece, Player}
import play.api.libs.json._

class FileIO extends FileIOTrait {
  override def load: GameState = ???


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
