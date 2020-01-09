package dog.model.FileIOComponent.fileIOJsonImpl

import dog.controller.GameState
import dog.model.FileIOComponent.FileIOTrait
import dog.model.Player
import play.api.libs.json.{JsValue, Json}

class FileIO extends FileIOTrait {
  override def load: GameState = ???


  override def save(gameState: GameState): Unit = {
    import java.io._
    val pw = new PrintWriter(new File("grid.json"))
    pw.write(Json.prettyPrint(gameStateToJson(gameState)))
    pw.close
  }

  def gameStateToJson(gameState: GameState): JsValue = {
    Json.obj(
      "player" -> Json.toJson(gameState.players._1.foreach(x => Json.toJson(playerToJson(x)))
      )
  }

  def playerToJson(player: Player): JsValue = {

  }
}
