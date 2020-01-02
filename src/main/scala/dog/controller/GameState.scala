package dog.controller

import dog.model.BoardComponent.BoardTrait
import dog.model.BoardComponent.boardBaseImpl.Board
import dog.model.CardComponent.CardTrait
import dog.model.CardComponent.cardBaseImpl.{Card, CardDeck}
import dog.model.Player

case class GameState(players: (Vector[Player], Int),
                     cardDeck: (Vector[CardTrait], Int),
                     lastPlayedCardOpt: Option[CardTrait],
                     board: BoardTrait) {

  def actualPlayer: Player = players._1(players._2)

  def lastPlayedCard: CardTrait = {
    lastPlayedCardOpt match {
      case Some(card) => card
      case None => Card("pseudo", "pseudo", "pseudo")
    }
  }
}

class GameStateMaster extends GameStateMasterTrait {

  override var pieceAmount = 4
  override var boardSize: Int = 64 // hast to be dividable by 4
  //player with player pointer
  override var tutorial: Boolean = true
  override var colors: Array[String] = Array("yellow", "white", "green", "red")
  override var playerNames: Array[String] = Array("Player 1", "Player 2", "Player 3", "Player 4")
  override var board: BoardTrait = new Board(boardSize)
  override var roundAndCardsToDistribute: (Int, Int) = (0, 6)
  override var players: Vector[Player] = playerNames.indices.map(i => Player.PlayerBuilder().
    withColor(colors(i))
    .withName((playerNames(i), i))
    .withPiece(pieceAmount, (boardSize / playerNames.length) * i)
    .withGeneratedCards(roundAndCardsToDistribute._2).build()).toVector
  override var actualPlayer: Int = 0

  override var clickedFieldIdx: Int = -1
  //cards of game
  override var cardDeck: Vector[CardTrait] = CardDeck.CardDeckBuilder().withAmount(List(10, 10)).withShuffle.buildCardVector
  override var cardPointer: Int = cardDeck.length
  override var lastPlayedCard: Option[CardTrait] = None
}
