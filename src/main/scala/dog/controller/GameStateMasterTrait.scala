package dog.controller

import dog.model.BoardComponent.BoardTrait
import dog.model.CardComponent.CardTrait
import dog.model.CardComponent.cardBaseImpl.Card
import dog.model.Player

trait GameStateMasterTrait {

  var tutorial: Boolean
  var colors: Array[String]
  var playerNames: Array[String]
  var players: Vector[Player]
  var actualPlayer: Int
  var amountCards: Int
  var roundAndCardsToDistribute: (Int, Int)
  var cardDeck: Vector[CardTrait]
  var cardPointer: Int
  var board: BoardTrait
  var lastPlayedCard: Option[CardTrait]
  var clickedFieldIdx: Int

  def getLastPlayedCard: CardTrait = {
    lastPlayedCard match {
      case Some(_) => lastPlayedCard.get
      case None => Card("pseudo", "pseudo", "pseudo")
    }
  }

  case class UpdateGame() {

    def withAmountDistributedCard(setAmount: Int): UpdateGame = {
      amountCards = setAmount
      this
    }

    def withDistributedCard(): UpdateGame = {
      players.foreach(x => x.setHandCards(Card.RandomCardsBuilder().withAmount(amountCards).buildRandomCardList))
      this
    }

    def withLastPlayed(setCard: CardTrait): UpdateGame = {
      lastPlayedCard match {
        case Some(_) => lastPlayedCard = Some(setCard)
        case None => lastPlayedCard = Some(setCard)
      }
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

    def withBoard(setBoard: BoardTrait): UpdateGame = {
      board = setBoard
      this
    }

    def withTutorial(setTutorial: Boolean): UpdateGame = {
      tutorial = setTutorial
      this
    }

    def withClickedField(clickedFlied: Int): UpdateGame = {
      clickedFieldIdx = clickedFlied
      this
    }

    def buildGame: GameState = {
      GameState((players, actualPlayer), (cardDeck, cardPointer), None, board, actualPlayer, clickedFieldIdx)
    }
  }

}
