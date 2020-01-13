package dog.controller.controllerBaseImpl

import dog.controller.Component.controllerBaseImpl.Controller
import dog.controller._
import dog.model.BoardComponent.boardBaseImpl.{Board, Cell}
import dog.model.CardComponent.cardBaseImpl.Card
import dog.model.{Piece, Player}
import org.scalatest.{Matchers, WordSpec}

class ChainSpec extends WordSpec with Matchers {
  "checking" should {
    val controller: ControllerTrait = new Controller(new Board(20))
    "check if pieces on board" in {
      controller.gameStateMaster.UpdateGame().resetGame

      val player: Player = Player.PlayerBuilder()
        .withPieces(Map(0 -> Piece(5)))
        .withCards(List(Card("", "", "")))
        .build()
      var gameState: GameState = controller.gameStateMaster.UpdateGame()
        .withPlayers(Vector(player))
        .withBoard(new Board(20))
        .withActualPlayer(0).buildGame
      val board2 = gameState.board.fill(gameState.board.cell(5).addPlayerToCell(player), 5)
      gameState = controller.gameStateMaster.UpdateGame().withBoard(board2).buildGame
      val inputCard: InputCard = InputCardMaster.UpdateCardInput()
        .withActualPlayer(0)
        .buildCardInput()

      val chain: Chain = Chain(gameState, inputCard)
      chain.tryChain(chain.checkPiecesOnBoardAndPlayable.tupled andThen chain.loggingFilter.tupled)._1 should be(true)
    }
    "check if pieces on board (no Pieces On Board)" in {
      controller.gameStateMaster.UpdateGame().resetGame

      val player: Player = Player.PlayerBuilder()
        .withPiece(1, 0)
        .withCards(Nil)
        .build()

      val gameState: GameState = controller.gameStateMaster.UpdateGame()
        .withPlayers(Vector(player))
        .withBoard(new Board(20))
        .withActualPlayer(0).buildGame

      val inputCard: InputCard = InputCardMaster.UpdateCardInput()
        .withActualPlayer(0)
        .buildCardInput()

      val chain: Chain = Chain(gameState, inputCard)
      chain.tryChain(chain.checkPiecesOnBoardAndPlayable.tupled andThen chain.loggingFilter.tupled)._1 should be(false)
    }
    "check if player has hand cards" in {
      controller.gameStateMaster.UpdateGame().resetGame

      var player: Player = Player.PlayerBuilder()
        .withCards(Nil)
        .build()
      player.cardList.nonEmpty should be(false)
      var gameState: GameState = controller.gameStateMaster.UpdateGame()
        .withPlayers(Vector(player))
        .withActualPlayer(0)
        .buildGame
      val inputCard: InputCard = InputCardMaster.UpdateCardInput()
        .withActualPlayer(0)
        .buildCardInput()

      var chain: Chain = Chain(gameState, inputCard)
      chain.tryChain(chain.checkHandCard.tupled andThen chain.loggingFilter.tupled)._1 should be(false)

      player = Player.PlayerBuilder()
        .withCards(List(Card("", "", "")))
        .build()
      gameState = controller.gameStateMaster.UpdateGame()
        .withPlayers(Vector(player)).buildGame

      chain = Chain(gameState, inputCard)
      chain.tryChain(chain.checkHandCard.tupled andThen chain.loggingFilter.tupled)._1 should be(false)
    }
    "check if player has card play on hand" in {
      controller.gameStateMaster.UpdateGame().resetGame

      var player: Player = Player.PlayerBuilder()
        .withCards(List(Card("1 11 play", "move move play", "red")))
        .withPieces(Map(0 -> Piece(0)))
        .build()
      var gameState: GameState = controller.gameStateMaster.UpdateGame()
        .withPlayers(Vector(player))
        .withActualPlayer(0).buildGame
      val inputCard: InputCard = InputCardMaster.UpdateCardInput()
        .withActualPlayer(0)
        .buildCardInput()

      var chain: Chain = Chain(gameState, inputCard)
      chain.tryChain(chain.checkPiecesOnBoardAndPlayable.tupled andThen chain.loggingFilter.tupled)._1 should be(true)

      player = Player.PlayerBuilder()
        .withCards(Nil)
        .withPieces(Map(0 -> Piece(0)))
        .build()
      gameState = controller.gameStateMaster.UpdateGame()
        .withPlayers(Vector(player)).buildGame

      chain = Chain(gameState, inputCard)
      chain.tryChain(chain.checkPiecesOnBoardAndPlayable.tupled andThen chain.loggingFilter.tupled)._1 should be(false)
    }
    "check if pieces are selected" in {
      controller.gameStateMaster.UpdateGame().resetGame

      val player: Player = Player.PlayerBuilder()
        .withPieces(Map(0 -> Piece(5)))
        .build()
      val gameState: GameState = controller.gameStateMaster.UpdateGame()
        .withPlayers(Vector(player))
        .withBoard(new Board(20).fill(Map(5 -> Cell(Some(player)))))
        .withActualPlayer(0).buildGame
      val board2 = gameState.board.fill(gameState.board.cell(5).addPlayerToCell(player), 5)
      controller.gameState = controller.gameStateMaster.UpdateGame().withBoard(board2).buildGame

      val inputCard: InputCard = InputCardMaster.UpdateCardInput()
        .withActualPlayer(0)
        .buildCardInput()

      controller.selectedField(5)

      val chain: Chain = Chain(gameState, inputCard)
      chain.tryChain(chain.checkSelected.tupled andThen chain.loggingFilter.tupled)._1 should be(true)
    }
  }
}
