package controller.Component

import model.CardComponent.CardTrait
import model.CardComponent.cardBaseImpl.CardDeck
import model.{Board, Player}

case class GameState(players: (Vector[Player], Int),
                     cardDeck: (Vector[CardTrait], Int),
                     board: Board) {
}

class GameStateMaster extends GameStateMasterTrait {

  //player with player pointer
  override var tutorial: Boolean = true
  override var colors: Array[String] = Array("gelb", "white", "grün", "rot")
  override var playerNames: Array[String] = Array("Player 1", "Player 2", "Player 3", "Player 4")
  override var players: Vector[Player] = (0 until 4).map(i => Player.PlayerBuilder().withColor(colors(i)).withName(playerNames(i)).build()).toVector
  override var actualPlayer: Int = 0

  //carddeck of game
  override var cardDeck: Vector[CardTrait] = CardDeck.CardDeck().withAmount(List(10, 10)).withShuffle.buildCardVector
  override var cardPointer: Int = cardDeck.length
  override var roundAndCardsToDistribute: (Int, Int) = (0, 6)

  //board
  override var board: Board = new Board(70)
}
