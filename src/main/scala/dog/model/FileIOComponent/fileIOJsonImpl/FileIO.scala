package dog.model.FileIOComponent.fileIOJsonImpl

//class FileIO extends FileIOTrait {
//  override def load: GameState = ???
//
//
//  override def save(gameState: GameState): Unit = {
//    import java.io._
//    val pw = new PrintWriter(new File("grid.json"))
//    pw.write(Json.prettyPrint(gameStateToJson(gameState)))
//    pw.close
//  }
//

//  def gameStateToJson(gameState: GameState): JsValue = {
//    Json.obj(fields =
//      "players" -> Json.toJson(gameState.players._1.foreach(x => Json.toJson(x))
//      )
//  }
//
//  implicit val playerWrites: Writes[Player] = (player: Player) => Json.obj(fields =
//    "nameAndIdx" -> player.nameAndIdx,
//    "color" -> player.color,
//    "piece" -> Json.toJson(
//      player.piece.foreach(x => Json.obj(fields = "pos" -> x._2.pos))
//    )
//
//  )
//
//}
