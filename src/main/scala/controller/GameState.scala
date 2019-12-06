package controller

import model.CardComponent.Card
import model.{Board, Player}

case class GameState(players: (Array[Player], Int),
                     cardDeck: (Array[Card], Int),
                     board: Board) {
  def this(player: (Array[Player], Int), cardDeck: (Array[Card], Int), board: Board) = {
    this((player._1, player._2), (cardDeck._1, cardDeck._2), board = board)
  }

}

case class UpdateGame(controller: Controller) {
  var players: Array[Player] = controller.createPlayers(List("p1", "p2", "p3", "p4")).players._1
  var actualPlayer: Int = 0
  var cardDeck: Array[Card] = controller.createCardDeck._1
  var cardPointer: Int = cardDeck.length
  var board: Board = controller.createNewBoard(20)

  def withPlayers(setPlayers: Array[Player]): UpdateGame = {
    players = setPlayers
    this
  }

  def withActualPlayer(setActualPlayer: Int): UpdateGame = {
    actualPlayer = setActualPlayer
    this
  }

  def withCardDeck(setCardDeck: Array[Card]): UpdateGame = {
    cardDeck = setCardDeck
    this
  }

  def withCardPointer(setCardPointer: Int): UpdateGame = {
    cardPointer = setCardPointer
    this
  }

  def withBoard(setBoard: Board): UpdateGame = {
    board = setBoard
    this
  }

  def buildGame: GameState = {
    assert(actualPlayer >= 0 && players.length - 1 >= actualPlayer)
    assert(cardPointer >= 0 && cardDeck.length - 1 >= cardPointer)
    GameState((players, actualPlayer), (cardDeck, cardPointer), board)
  }
}

//object GameState extends Enumeration {
//  type GameState = Value
//  val IDLE, MOVE, CREATEPLAYER, CREATEBOARD, DRAWCARD = Value
//
//  val map = Map[GameState, String](
//    IDLE -> "",
//    MOVE -> "moved player",
//    CREATEPLAYER -> "created player",
//    CREATEBOARD -> "created board",
//    DRAWCARD -> "draw card"
//  )
//
//}
