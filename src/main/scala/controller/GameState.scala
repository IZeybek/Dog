package controller

import model.CardComponent.{Card, CardDeck}
import model.{Board, Player}

import scala.util.Random

case class GameState(players: (Vector[Player], Int),
                     cardDeck: (Vector[Card], Int),
                     board: Board) {
}

class GameStateMaster {

  //player with player pointer
  var tutorial: Boolean = true
  var colors: Array[String] = Array("gelb", "blau", "grün", "rot")
  var playerNames: Array[String] = Array("P1", "P2", "P3", "P4")
  var players: Vector[Player] = (0 until 4).map(i => Player.PlayerBuilder().withColor(colors(i)).withName(playerNames(i)).build()).toVector
  var actualPlayer: Int = 0
  var roundAndCardsToDistribute: (Int, Int) = (0, 6)

  //carddeck of game
  var cardDeck: Vector[Card] = Random.shuffle(CardDeck.apply(List(10, 10))).toVector
  var cardPointer: Int = cardDeck.length

  //board
  var board: Board = new Board(20)

  case class UpdateGame() {

    def distributeCards(): UpdateGame = {
      this
    }

    def withPlayers(setPlayers: Vector[Player]): UpdateGame = {
      players = setPlayers
      this
    }

    def withActualPlayer(setActualPlayer: Int): UpdateGame = {
      actualPlayer = setActualPlayer
      this
    }

    def withNextPlayer(): UpdateGame = {
      val newPlayer = actualPlayer + 1
      actualPlayer = (newPlayer % players.size)
      this
    }

    def withCardDeck(setCardDeck: Vector[Card]): UpdateGame = {
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

    def withTutorial(setTutorial: Boolean): UpdateGame = {
      tutorial = setTutorial
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
