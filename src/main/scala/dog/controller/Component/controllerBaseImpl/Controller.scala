package dog.controller.Component.controllerBaseImpl

import com.google.inject.name.Names
import com.google.inject.{Guice, Inject, Injector}
import dog.DogModule
import dog.controller._
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

  override def doStep(): Unit = undoManager.doStep(new SolveCommand(this))

  override def undoCommand(): Unit = {
    undoManager.undoStep()
    publish(new BoardChanged)
  }

  override def clickedButton(clickedFieldIdx: Int): Int = {
    gameState = gameStateMaster.UpdateGame().withClickedField(clickedFieldIdx).buildGame
    publish(new BoardChanged)
    clickedFieldIdx
  }

  override def redoCommand(): Unit = {
    undoManager.redoStep()
    publish(new BoardChanged)
  }

  /**
   * Manages the round
   *
   * @param inputCard     is a CardInput builder in order to parse information out of it
   *                      @ otherPlayer   is not -1 when e.g. swapping -> user has to know when to insert more or less commands
   *                      @ pieceNum      is a List of indexes for the pieces of each player for e.g. swapping, only first is used when its about a move
   *                      @ cardNum       is a tuple
   *                    1. is the card number
   *                    2. is which options of the card have to be selected
   *                      e.g. "4" "forward backward" => using parse
   * @return a String that is returned to the TUI for more information
   */
  override def manageRound(inputCard: InputCard): String = {

    var returnString: String = ""

    if (useCardLogic(inputCard) == 0) {
      gameState = gameStateMaster.UpdateGame().withClickedField(-1).withNextPlayer().buildGame
      returnString = s"Player ${gameState.players._1(gameState.players._2).consoleColor}${gameState.players._1(gameState.players._2).nameAndIdx}${Console.RESET}'s turn\n"
      publish(new BoardChanged)
    } else {
      undoCommand()
      undoCommand()
      returnString = s"Move was not possible! Please retry player ${gameState.players._1(gameState.players._2).consoleColor}${gameState.players._2}${Console.RESET} ;)\n"
    }
    returnString
  }

  /**
   * uses the card and extracts its logic
   *
   * @param inputCard is a build with all the information needed for its cardLogic
   *                  @ param selectedPlayerList is the list of Players -> first one is the actual player =>
   *                  managed by manageRound but can also be set manually for testing purposes
   *                  @ param pieceNum           is a List of indexes for the pieces of each player for e.g. swapping, only first is used when its about a move
   *                  @ param selectedCard       is the index of the card in a CardList of the player that is played
   * @return
   */
  override def useCardLogic(inputCard: InputCard): Int = {

    if (gameState.players._1(gameState.actualPlayer).cardList.nonEmpty) {
      println("--------------------------> " + inputCard.selectedCard.task)
      val strategy = CardLogic.getLogic(inputCard.selectedCard.task)
      val updateGame: (BoardTrait, Vector[Player], Int) = CardLogic.setStrategy(strategy, gameState, inputCard)
      if (updateGame._3 == 0) {
        doStep()
        this.board = updateGame._1
        gameState = gameStateMaster.UpdateGame().withPlayers(updateGame._2).withBoard(board).buildGame
      }
      return updateGame._3
    }
    -1
  }

  /**
   * gets the selected card from the player
   *
   * @param playerNum     is the player
   * @param cardAndOption is a tuple
   *                1. is the card number
   *                2. is which options of the card have to be selected
   *                  e.g. "4" "forward backward" => using parse
   * @return the new card
   */
  override def getSelectedCard(playerNum: Int, cardAndOption: (Int, Int)): CardTrait = {
    val player: Vector[Player] = gameState.players._1
    val oldCard = player(playerNum).getCard(cardAndOption._1)
    val newPlayer: Vector[Player] = player.updated(playerNum, player(playerNum).removeCard(oldCard))
    doStep()
    gameState = gameStateMaster.UpdateGame().withLastPlayed(oldCard).buildGame
    gameState = gameStateMaster.UpdateGame().withPlayers(newPlayer).buildGame
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
  override def createPlayers(playerNames: List[String]): GameState = {
    val colors = gameStateMaster.colors
    val players: Vector[Player] = playerNames.indices.map(i => Player.PlayerBuilder().withColor(colors(i)).withName((playerNames(i), i)).build()).toVector
    gameState = gameStateMaster.UpdateGame().withPlayers(players).buildGame
    gameState
  }

  override def createCardDeck(amounts: List[Int]): (Vector[CardTrait], Int) = CardDeck.CardDeckBuilder().withAmount(List(2, 2)).withShuffle.buildCardVectorWithLength

  override def getBoard: BoardTrait = gameState.board

  //Cards
  override def drawFewCards(amount: Int): List[CardTrait] = Card.RandomCardsBuilder().withAmount(amount).buildRandomCardList

  override def drawCardFromDeck: CardTrait = {
    var cardDeck: (Vector[CardTrait], Int) = gameState.cardDeck
    if (cardDeck._2 != 0)
      cardDeck = cardDeck.copy(cardDeck._1, cardDeck._2 - 1)
    doStep()
    gameState = gameStateMaster.UpdateGame().withCardDeck(cardDeck._1).withCardPointer(cardDeck._2).buildGame
    cardDeck._1(cardDeck._2)
  }

  //AREA 51-------------------------------------------------------------------------------------------------------

  override def testDistributeCardsToPlayer(playerNum: Int, cards: List[CardTrait]): Player = {
    val player: Vector[Player] = gameState.players._1
    val newPlayer: Vector[Player] = player.updated(playerNum, player(playerNum).setHandCards(cards))
    gameState = gameStateMaster.UpdateGame().withPlayers(newPlayer).buildGame
    newPlayer(playerNum)
  }

  override def toStringActivePlayerHand: String = {
    val player: Player = gameState.players._1(gameState.players._2)
    s"${player.consoleColor}${player.nameAndIdx}${Console.RESET}'s hand cards: " + player.cardList + "\n"
  }

  override def toStringPlayerHands: String = {
    val player: Vector[Player] = gameState.players._1
    var playerHands: String = ""
    player.foreach(x => playerHands = playerHands + s"${x.consoleColor}${x.nameAndIdx} ${Console.RESET} --> myHand: " + x.cardList + "\n")
    playerHands
  }

  /**
   * prints the board and houses
   *
   * @return the board and houses in a String
   */
  override def toStringBoard: String = toStringHouse + board.toString

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

  override def toStringCardDeck: String = CardDeck.toStringCardDeck(gameState.cardDeck)


}
