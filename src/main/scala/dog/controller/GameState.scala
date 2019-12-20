package dog.controller

import dog.model.BoardComponent.BoardTrait
import dog.model.BoardComponent.boardBaseImpl.Board
import dog.model.CardComponent.CardTrait
import dog.model.CardComponent.cardBaseImpl.CardDeck
import dog.model.Player

case class GameState(players: (Vector[Player], Int),
                     cardDeck: (Vector[CardTrait], Int),
                     lastPlayedCard: Option[CardTrait],
                     board: BoardTrait)

class GameStateMaster extends GameStateMasterTrait {

  //player with player pointer
  override var tutorial: Boolean = true
  override var colors: Array[String] = Array("gelb", "white", "grün", "rot")
  override var playerNames: Array[String] = Array("Player 1", "Player 2", "Player 3", "Player 4")
  override var players: Vector[Player] = (0 until 4).map(i => Player.PlayerBuilder().withColor(colors(i)).withName(playerNames(i)).withGeneratedCards(6).build()).toVector
  override var amountCards: Int = 6
  override var actualPlayer: Int = 0

  //carddeck of game
  override var cardDeck: Vector[CardTrait] = CardDeck.CardDeckBuilder().withAmount(List(10, 10)).withShuffle.buildCardVector
  override var cardPointer: Int = cardDeck.length
  override var roundAndCardsToDistribute: (Int, Int) = (0, 6)
  override var lastPlayedCard: Option[CardTrait] = None

  override var board: BoardTrait = new Board(20)
}
