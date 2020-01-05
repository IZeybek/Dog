package dog.controller.Component.controllerBaseImpl

import com.google.inject.name.Names
import com.google.inject.{Guice, Inject, Injector}
import dog.DogModule
import dog.controller._
import dog.model.BoardComponent.BoardTrait
import dog.model.BoardComponent.boardBaseImpl.{Board, BoardCreateStrategyRandom}
import dog.model.CardComponent.CardTrait
import dog.model.CardComponent.cardBaseImpl.CardLogic.JokerState
import dog.model.CardComponent.cardBaseImpl.{Card, CardDeck, CardLogic}
import dog.model.Player
import dog.util.{SolveCommand, UndoManager}
import net.codingwell.scalaguice.InjectorExtensions._

class Controller @Inject()(var board: BoardTrait) extends ControllerTrait {

  override val undoManager: UndoManager = new UndoManager
  val injector: Injector = Guice.createInjector(new DogModule)
  override var gameStateMaster: GameStateMasterTrait = new GameStateMaster
  override var gameState: GameState = gameStateMaster.UpdateGame().withBoard(board).buildGame

  override def clickedField(clickedFieldIdx: Int): Int = {
    gameStateMaster.UpdateGame().withClickedField(clickedFieldIdx)
    publish(new BoardChanged)
    clickedFieldIdx
  }

  override def doStep(): Unit = undoManager.doStep(new SolveCommand(this))

  override def undoCommand(): Unit = {
    undoManager.undoStep()
    publish(new BoardChanged)
  }

  override def redoCommand(): Unit = {
    undoManager.redoStep()
    publish(new BoardChanged)
  }

  /**
   * Manages the round
   *
   * @param inputCard is a CardInput builder in order to parse information out of it
   * @return a String that is returned to the TUI for more information
   */
  override def manageRound(inputCard: InputCard): String = {

    var returnString: String = ""
    val newState: (BoardTrait, Vector[Player], Int) = useCardLogic(inputCard)

    newState._3 match {
      case 0 =>
        doStep()
        this.board = newState._1
        gameState = gameStateMaster.UpdateGame()
          .withBoard(newState._1)
          .withPlayers(newState._2)
          .withClickedField(-1)
          .withNextPlayer()
          .buildGame

        removeSelectedCard(InputCardMaster.actualPlayerIdx, InputCardMaster.cardNum._1)

        returnString = s"Player ${gameState.players._1(gameState.players._2).consoleColor}${gameState.players._1(gameState.players._2).nameAndIdx}${Console.RESET}'s turn\n"
        publish(new BoardChanged)
      case 1 =>
        println("joker packingState: " + JokerState.state)
        gameState = gameStateMaster.UpdateGame()
          .withBoard(newState._1)
          .withPlayers(newState._2)
          .withClickedField(-1)
          .buildGame

        publish(new BoardChanged)
      case _ =>
        returnString = s"Move was not possible! Please retry player ${gameState.players._1(gameState.players._2).consoleColor}${gameState.players._2}${Console.RESET} ;)\n"
    }
    returnString
  }

  /**
   * uses the card and extracts its logic
   *
   * @param inputCard is a build with all the information needed for its cardLogic
   * @return a Tuple
   *         1. BoardTrait
   *         2. Vector[Player]
   *         3. Int -> can have different values depending on the operations
   */
  override def useCardLogic(inputCard: InputCard): (BoardTrait, Vector[Player], Int) = CardLogic.setStrategy(CardLogic.getLogic(inputCard.selectedCard.task), gameState, inputCard)

  /**
   * gets the selected card from the player
   *
   * @param playerIdx is the player
   * @param cardIdx
   * @return the new card
   */
  override def removeSelectedCard(playerIdx: Int, cardIdx: Int): CardTrait = {
    val player: Vector[Player] = gameState.players._1
    val oldCard = player(playerIdx).getCard(cardIdx)
    val newPlayer: Vector[Player] = player.updated(playerIdx, player(playerIdx).removeCard(oldCard))
    doStep()
    gameState = gameStateMaster.UpdateGame()
      .withLastPlayed(oldCard)
      .withPlayers(newPlayer)
      .buildGame
    oldCard
  }

  //Board
  override def createNewBoard(size: Int): BoardTrait = {
    val newBoard = new Board(size)
    board = newBoard
    gameState = gameStateMaster.UpdateGame().withBoard(newBoard).buildGame
    publish(new BoardChanged)
    newBoard
  }

  override def createNewBoard: BoardTrait = {
    board.size match {
      case 1 => board = injector.instance[BoardTrait](Names.named("nano"))
      case 9 => board = injector.instance[BoardTrait](Names.named("micro"))
      case 20 => board = injector.instance[BoardTrait](Names.named("small"))
      case 64 => board = injector.instance[BoardTrait](Names.named("normal"))
      case _ =>
    }
    gameState = gameStateMaster.UpdateGame().withBoard(board).buildGame
    publish(new BoardChanged)
    board
  }

  def createRandomBoard(size: Int): BoardTrait = {
    val board = new BoardCreateStrategyRandom().createNewBoard(size)
    this.board = board
    gameState = gameStateMaster.UpdateGame().withBoard(board).buildGame
    publish(new BoardChanged)
    board
  }

  //Player

  //@TODO: extend method to dynamic playerADD with color algorithm, later... bitches
  override def createPlayers(playerNames: List[String], pieceAmount: Int): GameState = {
    val colors = gameStateMaster.colors
    val players: Vector[Player] = playerNames.indices.map(i => Player.PlayerBuilder()
      .withColor(colors(i))
      .withName((playerNames(i), i))
      .withPiece(pieceAmount, (gameState.board.size / pieceAmount) * i)
      .build()).toVector
    gameState = gameStateMaster.UpdateGame()
      .withPlayers(players)
      .buildGame
    gameState
  }

  override def createCardDeck(amounts: List[Int]): (Vector[CardTrait], Int) = CardDeck.CardDeckBuilder()
    .withAmount(List(2, 2))
    .withShuffle
    .buildCardVectorWithLength

  override def drawCards(amount: Int): List[CardTrait] = Card.RandomCardsBuilder().withAmount(amount).buildRandomCardList

  override def drawCardFromDeck: CardTrait = {
    var cardDeck: (Vector[CardTrait], Int) = gameState.cardDeck
    if (cardDeck._2 != 0)
      cardDeck = cardDeck.copy(cardDeck._1, cardDeck._2 - 1)
    doStep()
    gameState = gameStateMaster.UpdateGame()
      .withCardDeck(cardDeck._1)
      .withCardPointer(cardDeck._2)
      .buildGame
    cardDeck._1(cardDeck._2)
  }

  override def givePlayerCards(playerNum: Int, cards: List[CardTrait]): Player = {
    val player: Vector[Player] = gameState.players._1
    val newPlayer: Vector[Player] = player.updated(playerNum, player(playerNum).setHandCards(cards))
    gameState = gameStateMaster.UpdateGame()
      .withPlayers(newPlayer)
      .buildGame
    newPlayer(playerNum)
  }

  override def toStringActivePlayerHand: String = {
    val player: Player = gameState.players._1(gameState.players._2)
    s"${player.consoleColor}${player.nameAndIdx._1}${Console.RESET}'s hand cards: " + player.cardList + "\n"
  }

  override def toStringPlayerHands: String = {
    val player: Vector[Player] = gameState.players._1
    var playerHands: String = ""
    player.foreach(x => playerHands = playerHands + s"${x.consoleColor}${x.nameAndIdx} ${Console.RESET} --> myHand: " + x.cardList + "\n")
    playerHands
  }

  override def toStringBoard: String = toStringHouse + board.toString

  override def toStringHouse: String = {
    val players: Vector[Player] = gameState.players._1
    val title: String = s"${Console.UNDERLINED}Houses${Console.RESET}"
    val up: String = "‾" * players.length * 3
    val down: String = "_" * players.length * 3
    var house: String = ""
    players.indices.foreach(i => house = house + s" ${players(i).consoleColor}${players(i).inHouse.size}${Console.RESET} ")
    "\n" + down + "\n" + house + "\t" + title + "\n" + up + "\n"
  }

  override def toStringCardDeck: String = CardDeck.toStringCardDeck(gameState.cardDeck)
}
