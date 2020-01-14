package dog.controller

import dog.model.BoardComponent.BoardTrait
import dog.model.BoardComponent.boardBaseImpl.Board
import dog.model.CardComponent.CardTrait
import dog.model.CardComponent.cardBaseImpl.CardLogic.JokerState
import dog.model.CardComponent.cardBaseImpl.{Card, CardDeck}
import dog.model.Player

trait GameStateMasterTrait {

  //player
  var colors: Array[String]
  var playerNames: Array[String]
  var playerVector: Vector[Player]
  var actualPlayerIdx: Int
  var pieceAmount: Int

  var roundAndCardsToDistribute: (Int, Int)
  var cardDeck: Vector[CardTrait]
  var lastPlayedCardOpt: Option[CardTrait]

  var cardPointer: Int
  var board: BoardTrait
  var boardSize: Int

  case class UpdateGame() {

    def withAmountDistributedCard(setAmount: Int): UpdateGame = {
      roundAndCardsToDistribute = (roundAndCardsToDistribute._1, setAmount)
      this
    }

    def withDistributedCard(): UpdateGame = {
      playerVector.foreach(x => x.setHandCards(Card.RandomCardsBuilder().withAmount(roundAndCardsToDistribute._2).buildRandomCardList))
      this
    }

    def withLastPlayedCard(setCard: CardTrait): UpdateGame = {
      lastPlayedCardOpt = Some(setCard)
      this
    }

    def withPlayers(setPlayers: Vector[Player]): UpdateGame = {
      playerVector = setPlayers
      this
    }

    def withActualPlayer(setActualPlayer: Int): UpdateGame = {
      actualPlayerIdx = setActualPlayer
      this
    }

    def withNextPlayer(): UpdateGame = {
      //do {
      actualPlayerIdx = (actualPlayerIdx + 1) % playerVector.size
      //} while (playerVector(actualPlayerIdx).cardList.isEmpty)
      this
    }


    def withNextRound(): UpdateGame = {
      roundAndCardsToDistribute = (roundAndCardsToDistribute._1 + 1, roundAndCardsToDistribute._2 - 1)
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

    def withBoard(setBoard: BoardTrait): UpdateGame = {
      board = setBoard
      this
    }

    def resetGame: GameState = {
      //Board
      boardSize = 128 // hast to be dividable by 4
      board = new Board(boardSize)

      // Player
      playerNames = Array("Player 1", "Player 2", "Player 3", "Player 4")
      colors = Array("yellow", "white", "green", "red")
      pieceAmount = 4
      playerVector = playerNames.indices.map(i => Player.PlayerBuilder().
        withColor(colors(i)).
        withName((playerNames(i), i)).
        withPiece(pieceAmount, (boardSize / playerNames.length) * i).
        withGeneratedCards(roundAndCardsToDistribute._2).build()).toVector
      actualPlayerIdx = 0

      // Card
      cardDeck = CardDeck.CardDeckBuilder().withAmount(List(10, 10)).withShuffle.buildCardVector
      cardPointer = cardDeck.length
      roundAndCardsToDistribute = (0, 6)
      lastPlayedCardOpt = Some(Card("pseudo", "pseudo", "pseudo"))

      GameState((playerVector, actualPlayerIdx), (cardDeck, cardPointer), None, board)
    }

    def loadGame(gameState: GameState): Unit = {
      JokerState.reset
      cardDeck = gameState.cardDeck._1
      cardPointer = gameState.cardDeck._2
      board = gameState.board
      playerVector = gameState.players._1
      actualPlayerIdx = gameState.players._2
      lastPlayedCardOpt = gameState.lastPlayedCardOpt
    }

    def buildGame: GameState = {
      GameState((playerVector, actualPlayerIdx), (cardDeck, cardPointer), lastPlayedCardOpt, board)
    }
  }

}
