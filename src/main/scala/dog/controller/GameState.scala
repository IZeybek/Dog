package dog.controller

import dog.model.BoardComponent.BoardTrait
import dog.model.BoardComponent.boardBaseImpl.Board
import dog.model.CardComponent.CardTrait
import dog.model.CardComponent.cardBaseImpl.CardDeck
import dog.model.Player

case class GameState(players: (Vector[Player], Int),
                     cardDeck: (Vector[CardTrait], Int),
                     lastPlayedCard: Option[CardTrait],
                     board: BoardTrait, actualPlayer: Int, clickedFieldIdx: Int)

class GameStateMaster extends GameStateMasterTrait {

  val pieceAmount = 4
  val boardSize: Int = 64
  override var amountCardsInHand: Int = 6
  //player with player pointer
  override var tutorial: Boolean = true
  override var colors: Array[String] = Array("yellow", "white", "green", "red")
  override var playerNames: Array[String] = Array("Player 1", "Player 2", "Player 3", "Player 4")
  override var board: BoardTrait = new Board(boardSize)
  override var players: Vector[Player] = (0 until pieceAmount).map(i => Player.PlayerBuilder().withColor(colors(i)).withName((playerNames(i), i)).withPieceNumber(pieceAmount, (boardSize / pieceAmount) * i).withGeneratedCards(amountCardsInHand).build()).toVector
  override var actualPlayer: Int = 0
  override var clickedFieldIdx: Int = -1

  //cards of game
  override var cardDeck: Vector[CardTrait] = CardDeck.CardDeckBuilder().withAmount(List(10, 10)).withShuffle.buildCardVector
  override var cardPointer: Int = cardDeck.length
  override var roundAndCardsToDistribute: (Int, Int) = (0, 6)
  override var lastPlayedCard: Option[CardTrait] = None


}
