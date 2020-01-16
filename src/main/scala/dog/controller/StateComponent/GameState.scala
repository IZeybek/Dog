package dog.controller.StateComponent

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

  //board
  override var pieceAmount = 4
  override var boardSize: Int = 96 // hast to be dividable by 4
  override var board: BoardTrait = new Board(boardSize)

  //player
  override var colors: Array[String] = Array("yellow", "white", "green", "red")
  override var playerNames: Array[String] = Array("Player 1", "Player 2", "Player 3", "Player 4")
  override var roundAndCardsToDistribute: (Int, Int) = (0, 6)
  override var playerVector: Vector[Player] = playerNames.indices.map(i => Player.PlayerBuilder()
    .withColor(colors(i))
    .withName((playerNames(i), i))
    .withPiece(pieceAmount, (boardSize / playerNames.length) * i)
    .withGeneratedCards(roundAndCardsToDistribute._2).build()).toVector
  override var actualPlayerIdx: Int = 0

  //cards
  override var cardDeck: Vector[CardTrait] = CardDeck.CardDeckBuilder().
    withAmount(List(10, 10)).
    withShuffle.buildCardVector
  override var cardPointer: Int = cardDeck.length
  override var lastPlayedCardOpt: Option[CardTrait] = None
}
