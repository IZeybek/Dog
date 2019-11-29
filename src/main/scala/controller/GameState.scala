package controller

object GameState extends Enumeration {
  type GameState = Value
  val IDLE, MOVE = Value

  val map = Map[GameState, String](
    IDLE -> "",
    MOVE -> "moved player"
  )

}
