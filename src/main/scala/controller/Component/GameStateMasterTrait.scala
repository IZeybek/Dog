package controller.Component

import model.BoardComponent.boardBaseImpl.Board
import model.CardComponent.CardTrait
import model.Player

trait GameStateMasterTrait {

  var tutorial: Boolean
  var colors: Array[String]
  var playerNames: Array[String]
  var players: Vector[Player]
  var actualPlayer: Int
  var roundAndCardsToDistribute: (Int, Int)
  var cardDeck: Vector[CardTrait]
  var cardPointer: Int
  var board: Board

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

    def withCardDeck(setCardDeck: Vector[CardTrait]): UpdateGame = {
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
