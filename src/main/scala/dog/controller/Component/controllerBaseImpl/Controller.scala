package dog.controller.Component.controllerBaseImpl

import controller.BoardChanged
import controller.Component.{ControllerTrait, GameState, GameStateMaster, GameStateMasterTrait}
import model.BoardComponent.boardBaseImpl.BoardCreateStrategyRandom
import model.CardComponent.CardTrait
import model.CardComponent.cardBaseImpl.{CardDeck, CardLogic}
import model._
import util.{SolveCommand, UndoManager}
import com.google.inject.name.Names
import com.google.inject.{Guice, Inject, Injector}
import dog.DogModule
import dog.controller.{ControllerTrait, GameState, GameStateMaster, GameStateMasterTrait}
import dog.model.BoardComponent.BoardTrait
import dog.model.BoardComponent.boardBaseImpl.{Board, BoardCreateStrategyRandom}
import dog.model.CardComponent.CardTrait
import dog.model.CardComponent.cardBaseImpl.{Card, CardDeck, CardLogic}
import dog.model.Player
import dog.util.{SolveCommand, UndoManager}
import net.codingwell.scalaguice.InjectorExtensions._

class Controller @Inject()(var board: BoardTrait) extends ControllerTrait {

  override val undoManager: UndoManager = new UndoManager
  val injector: Injector = Guice.createInjector(new DogModule)
  override var gameStateMaster: GameStateMasterTrait = new GameStateMaster
  override var gameState: GameState = gameStateMaster.UpdateGame().withBoard(board).buildGame

  override def redoCommand(): Unit = {
    undoManager.redoStep()
    publish(new BoardChanged)
  }

  //Board
  override def createNewBoard(size: Int): BoardTrait = {
    val board = new Board(size)
    gameState = gameStateMaster.UpdateGame().withBoard(board).buildGame
    publish(new BoardChanged)
    board
  }

  override def createNewBoard: Unit = {
    board.getBoardMap.size match {
      case 1 => board = injector.instance[BoardTrait](Names.named("nano"))
      case 9 => board = injector.instance[BoardTrait](Names.named("micro"))
      case 20 => board = injector.instance[BoardTrait](Names.named("small"))
      case 64 => board = injector.instance[BoardTrait](Names.named("normal"))
      case _ =>
    }
    gameState = gameStateMaster.UpdateGame().withBoard(board).buildGame
    println(gameState.board.toString)
  }

  def createRandomBoard(size: Int): BoardTrait = {
    val board = new BoardCreateStrategyRandom().createNewBoard(size)
    gameState = gameStateMaster.UpdateGame().withBoard(board).buildGame
    publish(new BoardChanged)
    board
  }

  /**
   * prints the board and houses
   *
   * @return the board and houses in a String
   */
  override def toStringBoard: String = toStringHouse + gameState.board.toString

  /**
   * prints the houses of each player
   *
   * @return the houses in a String
   */
  override def toStringHouse: String = {
    val players: Vector[Player] = gameState.players._1
    val title: String = s"${Console.UNDERLINED}Houses${Console.RESET}"
    val up: String = "‾" * players.length * 3
    val down: String = "_" * players.length * 3
    var house: String = ""
    players.indices.foreach(i => house = house + s" ${players(i).consoleColor}${players(i).inHouse}${Console.RESET} ")
    "\n" + down + "\n" + house + "\t" + title + "\n" + up + "\n"
  }

  override def getBoard: BoardTrait = gameState.board

  //Player
  //@TODO: extend method to dynamic playerADD with color algorithm, later... bitches
  override def createPlayers(playerNames: List[String]): GameState = {
    val colors = gameStateMaster.colors
    val players: Vector[Player] = playerNames.indices.map(i => Player.PlayerBuilder().withColor(colors(i)).withName(playerNames(i)).build()).toVector
    gameState = gameStateMaster.UpdateGame().withPlayers(players).buildGame
    gameState
  }

  /**
   * Manages the round
   *
   * @param otherPlayer is not -1 when e.g. swapping -> user has to know when to insert more or less commands
   * @param pieceNum    is a List of indexes for the pieces of each player for e.g. swapping, only first is used when its about a move
   * @param cardNum     is the index of the card in a CardList of the player that is played
   * @return a String that is returned to the TUI for more information
   */
  override def manageRound(otherPlayer: Int, pieceNum: List[Int], cardNum: Int): String = {
    val selectedPlayerList: List[Int] = gameState.players._2 :: otherPlayer :: Nil
    var returnString: String = ""
    if (useCardLogic(selectedPlayerList, pieceNum, cardNum) == 0) {
      gameState = gameStateMaster.UpdateGame().withNextPlayer().buildGame
      returnString = s"Player ${gameState.players._1(gameState.players._2).color}${gameState.players._1(gameState.players._2).name}${Console.RESET}'s turn\n"
      publish(new BoardChanged)
    } else {
      undoCommand()
      returnString = s"Move was not possible! Please retry player ${gameState.players._1(gameState.players._2).color}${gameState.players._2}${Console.RESET} ;)\n"
    }
    returnString
  }

  override def undoCommand(): Unit = {
    undoManager.undoStep()
    publish(new BoardChanged)
  }

  /**
   * uses the card and extracts its logic
   *
   * @param selectedPlayerList is the list of Players -> first one is the actual player =>
   *                           managed by manageRound but can also be set manually for testing purposes
   * @param pieceNum           is a List of indexes for the pieces of each player for e.g. swapping, only first is used when its about a move
   * @param cardNum            is the index of the card in a CardList of the player that is played
   * @return
   */
  override def useCardLogic(selectedPlayerList: List[Int], pieceNum: List[Int], cardNum: Int): Int = {
    val board: BoardTrait = gameState.board
    if (selectedPlayerList != Nil && gameState.players._1(selectedPlayerList.head).cardList.nonEmpty) {

      doStep()
      val selectedCard: CardTrait = getSelectedCard(selectedPlayerList.head, cardNum)

      // will be changed later as well since other logic's aren't implemented yet
      val taskMode = CardLogic.getLogic(selectedCard.getTask)
      val moveInInt = if (selectedCard.getTask == "move") selectedCard.getSymbol.toInt else 0
      val updateGame: (BoardTrait, Vector[Player], Int) = CardLogic.setStrategy(taskMode, gameState.players._1, board, selectedPlayerList, pieceNum, moveInInt)
      if (updateGame._3 == 0) {
        this.board = updateGame._1
        gameState = gameStateMaster.UpdateGame().withPlayers(updateGame._2).withBoard(updateGame._1).buildGame

      }
      return updateGame._3
    }
    -1
  }

  override def doStep(): Unit = undoManager.doStep(new SolveCommand(this))

  //Cards

  override def getSelectedCard(playerNum: Int, cardNum: Integer): CardTrait = {
    val player: Vector[Player] = gameState.players._1
    val oldCard = player(playerNum).getCard(cardNum)
    val newPlayer: Vector[Player] = player.updated(playerNum, player(playerNum).removeCard(player(playerNum).getCard(cardNum)))
    gameState = gameStateMaster.UpdateGame().withPlayers(newPlayer).buildGame
    println(s"$oldCard with ${oldCard.getTask}")
    oldCard
  }

  override def createCardDeck(amounts: List[Int]): (Vector[CardTrait], Int) = CardDeck.CardDeckBuilder().withAmount(List(2, 2)).withShuffle.buildCardVectorWithLength

  override def toStringCardDeck: String = CardDeck.toStringCardDeck(gameState.cardDeck)

  override def drawFewCards(amount: Int): List[CardTrait] = Card.RandomCardsBuilder().withAmount(amount).buildRandomCardList

  override def drawCardFromDeck: CardTrait = {
    var cardDeck: (Vector[CardTrait], Int) = gameState.cardDeck
    if (cardDeck._2 != 0)
      cardDeck = cardDeck.copy(cardDeck._1, cardDeck._2 - 1)
    gameState = gameStateMaster.UpdateGame().withCardDeck(cardDeck._1).withCardPointer(cardDeck._2).buildGame
    cardDeck._1(cardDeck._2)
  }

  override def toStringPlayerHands: String = {
    val player: Vector[Player] = gameState.players._1
    var playerHands: String = ""
    player.foreach(x => playerHands = playerHands + s"${x.consoleColor}${x.name} ${Console.RESET} --> myHand: " + x.cardList + "\n")
    playerHands
  }


  //AREA 51-------------------------------------------------------------------------------------------------------

  override def testDistributeCardsToPlayer(playerNum: Int, cards: List[CardTrait]): Player = {
    val player: Vector[Player] = gameState.players._1
    val newPlayer: Vector[Player] = player.updated(playerNum, player(playerNum).setHandCards(cards))
    gameState = gameStateMaster.UpdateGame().withPlayers(newPlayer).buildGame
    newPlayer(playerNum)
  }
}
