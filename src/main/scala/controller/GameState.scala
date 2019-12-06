package controller

import model.CardComponent.{Card, CardDeck}
import model.{Board, Player}

import scala.util.Random

case class GameState(players: (Array[Player], Int),
                     cardDeck: (Array[Card], Int),
                     board: Board) {
}

class GameStateMaster {

  //player with player pointer
  var colors = Array("gelb", "blau", "grün", "rot")
  var playerNames: Array[String] = Array("P1", "P2", "P3", "P4")
  var players: Array[Player] = (0 until 4).map(i => Player.PlayerBuilder().withColor(colors(i)).withName(playerNames(i)).build()).toArray
  var actualPlayer: Int = 0

  //carddeck of game
  var cardDeck: Array[Card] = Random.shuffle(CardDeck.apply()).toArray
  var cardPointer: Int = cardDeck.length

  //board
  var board: Board = new Board(20)

  case class UpdateGame() {

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
      //      assert(actualPlayer >= 0 && players.length - 1 >= actualPlayer)
      //      assert(cardPointer >= 0 && cardDeck.length - 1 >= cardPointer)
      GameState((players, actualPlayer), (cardDeck, cardPointer), board)
    }
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
