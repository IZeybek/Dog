package dog.model.FileIOComponent.fileIOJsonImpl

import dog.controller.GameState
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
        "playerVector" -> gameState.players._1.map(x => Json.obj("player" -> Json.toJson(x)))
      )
    )
  }

  implicit val playerWrites: Writes[Player] = (player: Player) => Json.obj(
    "idx" -> player.nameAndIdx._2,
    "name" -> player.nameAndIdx._1,
    "homepos" -> player.homePosition,
    "color" -> player.color,
    "piecesVector" -> player.piece.map(x => Json.obj("piece" -> Json.toJson(pieceMap(x._1, x._2)))),
    "CardList" -> player.cardList.map(x => Json.obj("Card" -> Json.toJson(x))),
  )

  case class pieceMap(pieceNum: Int, piece: Piece)

  implicit val pieceWrites: Writes[pieceMap] = (pieces: pieceMap) => Json.obj(
    "pieceNum" -> pieces.pieceNum,
    "position" -> pieces.piece.pos,
  )

  implicit val cardWrites: Writes[CardTrait] = (card: CardTrait) => Json.obj(
    "symbol" -> JsString(card.symbol),
    "task" -> JsString(card.task),
    "color" -> JsString(card.color),
  )
}
