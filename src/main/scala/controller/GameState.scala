package controller

object GameState extends Enumeration {
  type GameState = Value
  val IDLE, MOVE, CREATEPLAYER, CREATEBOARD, DRAWCARD = Value

  val map = Map[GameState, String](
    IDLE -> "",
    MOVE -> "moved player",
    CREATEPLAYER -> "created player",
    CREATEBOARD -> "created board",
    DRAWCARD -> "draw card"
  )

}
