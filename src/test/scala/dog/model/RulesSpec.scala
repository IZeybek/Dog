//package dog.model
//
//import dog.controller.Component.controllerBaseImpl.Controller
//import dog.controller.{ControllerTrait, GameState, InputCard, InputCardMaster}
//import dog.model.BoardComponent.boardBaseImpl.{Board, Cell}
//import dog.model.CardComponent.cardBaseImpl.Card
//import org.scalatest.{Matchers, WordSpec}
//
//class LevelSpec extends WordSpec with Matchers {
//  "Levels" when {
//    "created" should {
//      val pro: Level = new Level(None, 2)
//      val advanced: Level = new Level(Some(pro), 1)
//      val basic: Level = new Level(Some(advanced), 0)
//      "have attributes" in {
//
//        basic.successor.get should be(advanced)
//        basic.level should be(0)
//
//        pro.successor should be(None)
//      }
//    }
//  }
//  "Events" when {
//    "created" should {
//      Event.setAttributes(new Controller(new Board(20)).gameState, InputCardMaster.UpdateCardInput().buildCardInput())
//      val event: Event = Event.setStrategy(0)
//      "have a level" in {
//        event.level should be(0)
//      }
//      "have a check" in {
//        event.check should be(Event.getLogic(0)._1)
//      }
//      "have a GameState" in {
//        event.gameState should not be null
//      }
//      "have an InputCard" in {
//        event.inputCard should not be null
//      }
//      "have a message" in {
//        event.message should be(Event.getLogic(0)._2)
//      }
//    }
//  }
//  "checking" should {
//    val controller: ControllerTrait = new Controller(new Board(20))
//    "check if pieces on board" in {
//      controller.gameStateMaster.UpdateGame().resetGame
//
//      val player: Player = Player.PlayerBuilder()
//        .withPieces(Map(0 -> Piece(5)))
//        .withCards(List(Card("", "", "")))
//        .build()
//      var gameState: GameState = controller.gameStateMaster.UpdateGame()
//        .withPlayers(Vector(player))
//        .withBoard(new Board(20))
//        .withActualPlayer(0).buildGame
//      val board2 = gameState.board.fill(gameState.board.cell(5).addPlayerToCell(player), 5)
//      gameState = controller.gameStateMaster.UpdateGame().withBoard(board2).buildGame
//      val inputCard: InputCard = InputCardMaster.UpdateCardInput()
//        .withActualPlayer(0)
//        .buildCardInput()
//      println("is cell 5 filled?: " + board2.cell(5).isFilled)
//      Event.checkPiecesOnBoardAndPlayable(gameState, inputCard) should be(true)
//    }
//    "check if pieces on board (no PiecesOnBoard)" in {
//      controller.gameStateMaster.UpdateGame().resetGame
//
//      val player: Player = Player.PlayerBuilder()
//        .withPieces(Map(0 -> Piece(0)))
//        .withCards(List(Card("3", "move", "blue")))
//        .build()
//      val gameState: GameState = controller.gameStateMaster.UpdateGame()
//        .withPlayers(Vector(player))
//        .withBoard(new Board(20))
//        .withActualPlayer(0).buildGame
//      val inputCard: InputCard = InputCardMaster.UpdateCardInput()
//        .withActualPlayer(0)
//        .buildCardInput()
//      (0 until 20).foreach(x => print(gameState.board.cell(x).isFilled + " "))
//      println()
//      println(gameState.actualPlayer.cardList)
//      Event.checkPiecesOnBoardAndPlayable(gameState, inputCard) should be(false)
//    }
//    "check if player has hand cards" in {
//      controller.gameStateMaster.UpdateGame().resetGame
//
//      var player: Player = Player.PlayerBuilder()
//        .withCards(Nil)
//        .build()
//      var gameState: GameState = controller.gameStateMaster.UpdateGame()
//        .withPlayers(Vector(player))
//        .withActualPlayer(0).buildGame
//      val inputCard: InputCard = InputCardMaster.UpdateCardInput()
//        .withActualPlayer(0)
//        .buildCardInput()
//      Event.checkHandCards(gameState, inputCard) should be(false)
//
//      player = Player.PlayerBuilder()
//        .withCards(List(Card("", "", "")))
//        .build()
//      gameState = controller.gameStateMaster.UpdateGame()
//        .withPlayers(Vector(player)).buildGame
//      Event.checkHandCards(gameState, inputCard) should be(true)
//    }
//    "check if player has card play on hand" in {
//      controller.gameStateMaster.UpdateGame().resetGame
//
//      var player: Player = Player.PlayerBuilder()
//        .withCards(List(Card("1 11 play", "move move play", "red")))
//        .build()
//      var gameState: GameState = controller.gameStateMaster.UpdateGame()
//        .withPlayers(Vector(player))
//        .withActualPlayer(0).buildGame
//      val inputCard: InputCard = InputCardMaster.UpdateCardInput()
//        .withActualPlayer(0)
//        .buildCardInput()
//      Event.checkPiecesOnBoardAndPlayable(gameState, inputCard) should be(true)
//
//      player = Player.PlayerBuilder()
//        .withCards(Nil)
//        .build()
//      gameState = controller.gameStateMaster.UpdateGame()
//        .withPlayers(Vector(player)).buildGame
//      Event.checkPiecesOnBoardAndPlayable(gameState, inputCard) should be(false)
//    }
//    "check if pieces are selected" in {
//      controller.gameStateMaster.UpdateGame().resetGame
//
//      val player: Player = Player.PlayerBuilder()
//        .withPieces(Map(0 -> Piece(5)))
//        .build()
//      var gameState: GameState = controller.gameStateMaster.UpdateGame()
//        .withPlayers(Vector(player))
//        .withBoard(new Board(20).fill(Map(5 -> Cell(Some(player)))))
//        .withActualPlayer(0).buildGame
//      val board2 = gameState.board.fill(gameState.board.cell(5).addPlayerToCell(player), 5)
//      controller.gameState = controller.gameStateMaster.UpdateGame().withBoard(board2).buildGame
//
//      val inputCard: InputCard = InputCardMaster.UpdateCardInput()
//        .withActualPlayer(0)
//        .buildCardInput()
//
//      controller.clickedField(5)
//      Event.checkSelected(gameState, inputCard) should be(true)
//    }
//    "check if won" in {
//      controller.gameStateMaster.UpdateGame().resetGame
//
//      val gameState: GameState = controller.gameStateMaster.UpdateGame().buildGame
//      val inputCard: InputCard = InputCardMaster.UpdateCardInput().buildCardInput()
//      Event.checkWon(gameState, inputCard) should be(true)
//    }
//  }
//}
