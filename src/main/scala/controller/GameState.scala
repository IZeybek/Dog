package controller


import controller.GameStatus.GameStatus
import model.CardComponent.{Card, CardDeck}
import model.{Board, Player}

import scala.util.Random

case class GameState(players: (Vector[Player], Int),
                     cardDeck: (Vector[Card], Int),
                     board: Board, gameState: GameStatus) {
}

object GameStatus extends Enumeration {
  type GameStatus = Value
  val WELCOME, CREATE, IDLE = Value

  val map: Map[GameStatus, String] = Map[GameStatus, String](
    WELCOME -> "welcome",
    CREATE -> "create",
    IDLE -> "main"
  )

  def message(gameStatus: GameStatus): String = {
    map(gameStatus)
  }

}

class GameStateMaster {

  var tutorial: Boolean = true
  var gameStatus: GameStatus = GameStatus.IDLE
  var colors: Array[String] = Array("gelb", "blau", "grün", "rot")
  var playerNames: Array[String] = Array("P1", "P2", "P3", "P4")
  var players: Vector[Player] = (0 until 4).map(i => Player.PlayerBuilder().withColor(colors(i)).withName(playerNames(i)).build()).toVector
  var actualPlayer: Int = 0
  var roundAndCardsToDistribute: (Int, Int) = (0, 6)

  //carddeck of game
  var cardDeck: Vector[Card] = Random.shuffle(CardDeck.apply(List(10, 10))).toVector
  var cardPointer: Int = cardDeck.length

  //board
  var board: Board = new Board(26)

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
      actualPlayer = newPlayer % players.size
      this
    }

    def withNewState(setNewState: GameStatus): UpdateGame = {
      gameStatus = setNewState
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
      GameState((players, actualPlayer), (cardDeck, cardPointer), board, gameStatus)
    }
  }

}
