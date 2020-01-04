package dog.controller

import dog.model.BoardComponent.BoardTrait
import dog.model.BoardComponent.boardBaseImpl.Board
import dog.model.CardComponent.CardTrait
import dog.model.CardComponent.cardBaseImpl.{Card, CardDeck}
import dog.model.Player

trait GameStateMasterTrait {

  //player
  var colors: Array[String]
  var playerNames: Array[String]
  var players: Vector[Player]
  var actualPlayerIdx: Int
  var pieceAmount: Int
  var cardDeckActualPlayer: List[CardTrait]

  var roundAndCardsToDistribute: (Int, Int)
  var cardDeck: Vector[CardTrait]
  var lastPlayedCard: Option[CardTrait]

  var cardPointer: Int
  var board: BoardTrait
  var clickedFieldIdx: Int
  var boardSize: Int

  case class UpdateGame() {

    def withAmountDistributedCard(setAmount: Int): UpdateGame = {
      roundAndCardsToDistribute = (roundAndCardsToDistribute._1, setAmount)
      this
    }

    def withDistributedCard(): UpdateGame = {
      players.foreach(x => x.setHandCards(Card.RandomCardsBuilder().withAmount(roundAndCardsToDistribute._2).buildRandomCardList))
      this
    }

    def withLastPlayed(setCard: CardTrait): UpdateGame = {
      lastPlayedCard = Some(setCard)
      this
    }

    def withoutLastPlayed(): UpdateGame = {
      lastPlayedCard = None
      this
    }

    def withPlayers(setPlayers: Vector[Player]): UpdateGame = {
      players = setPlayers
      this
    }

    def withActualPlayer(setActualPlayer: Int): UpdateGame = {
      actualPlayerIdx = setActualPlayer
      this
    }

    def withSwappedActualPlayerCards(setHandCards: List[CardTrait]): UpdateGame = {
      cardDeckActualPlayer = players(actualPlayerIdx).cardList
      players = players.updated(actualPlayerIdx, players(actualPlayerIdx).copy(cardList = setHandCards))
      this
    }

    def withRestoredActualPlayerCards(): UpdateGame = {
      players = players.updated(actualPlayerIdx, players(actualPlayerIdx).copy(cardList = cardDeckActualPlayer))
      this
    }

    def withNextPlayer(): UpdateGame = {
      val newPlayer = actualPlayerIdx + 1
      actualPlayerIdx = newPlayer % players.size
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

    def withClickedField(clickedField: Int): UpdateGame = {
      clickedFieldIdx = clickedField
      this
    }

    def resetGame: GameState = {
      //Board
      boardSize = 64 // hast to be dividable by 4
      board = new Board(boardSize)
      clickedFieldIdx = -1

      // Player
      playerNames = Array("Player 1", "Player 2", "Player 3", "Player 4")
      colors = Array("yellow", "white", "green", "red")
      pieceAmount = 4
      players = playerNames.indices.map(i => Player.PlayerBuilder().
        withColor(colors(i)).
        withName((playerNames(i), i)).
        withPiece(pieceAmount, (boardSize / playerNames.length) * i).
        withGeneratedCards(roundAndCardsToDistribute._2).build()).toVector
      actualPlayerIdx = 0

      // Card
      cardDeck = CardDeck.CardDeckBuilder().withAmount(List(10, 10)).withShuffle.buildCardVector
      cardPointer = cardDeck.length
      roundAndCardsToDistribute = (0, 6)
      lastPlayedCard = None

      GameState((players, actualPlayerIdx), (cardDeck, cardPointer), None, board)
    }

    def buildGame: GameState = {
      GameState((players, actualPlayerIdx), (cardDeck, cardPointer), None, board)
    }
  }

}
