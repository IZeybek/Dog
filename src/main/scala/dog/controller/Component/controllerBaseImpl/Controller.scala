package dog.controller.Component.controllerBaseImpl

import com.google.inject.name.Names
import com.google.inject.{Guice, Inject, Injector}
import dog.DogModule
import dog.controller._
import dog.model.BoardComponent.boardBaseImpl.{Board, BoardCreateStrategyRandom}
import dog.model.BoardComponent.{BoardTrait, CellTrait}
import dog.model.CardComponent.CardTrait
import dog.model.CardComponent.cardBaseImpl.CardLogic.JokerState
import dog.model.CardComponent.cardBaseImpl.{Card, CardDeck, CardLogic}
import dog.model.FileIOComponent.FileIOTrait
import dog.model.Player
import dog.util.{SelectedState, SolveCommand, UndoManager}
import net.codingwell.scalaguice.InjectorExtensions._

class Controller @Inject()(var board: BoardTrait) extends ControllerTrait {

  override val undoManager: UndoManager = new UndoManager
  val injector: Injector = Guice.createInjector(new DogModule)
  val fileIo: FileIOTrait = injector.instance[FileIOTrait]
  override var gameStateMaster: GameStateMasterTrait = new GameStateMaster
  override var gameState: GameState = gameStateMaster.UpdateGame().withBoard(board).buildGame

  override def clickedField(clickedFieldIdx: Int): Int = {
    val clickedCell: CellTrait = gameState.board.cell(clickedFieldIdx)

    if (!clickedCell.isFilled) {

      SelectedState.reset

    } else {
      val isOwnField = clickedCell.p.get.nameAndIdx._2 == gameStateMaster.actualPlayerIdx
      val isOtherField = if (SelectedState.state.equals(SelectedState.ownPieceSelected) && !isOwnField) true else false
      if (SelectedState.state.equals(SelectedState.nothingSelected) && isOwnField) SelectedState.handle(gameState, clickedFieldIdx)
      else if (SelectedState.state.equals(SelectedState.ownPieceSelected) && isOtherField) SelectedState.handle(gameState, clickedFieldIdx)
      else SelectedState.reset
    }
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
   * save the game to a JSON or XML file
   * specified thru dependency injection
   */
  override def save: Unit = {
    fileIo.save(gameState)
    publish(new BoardChanged)
  }

  /**
   * load the game from a file
   */
  override def load: Unit = {
    gameState = fileIo.load
    gameStateMaster.UpdateGame().loadGame(gameState)
    publish(new BoardChanged)
  }

  /**
   * Manages the round
   *
   * @param inputCard is a CardInput builder in order to parse information out of it
   * @return a String that is returned to the TUI for more information
   */
  override def manageRound(inputCard: InputCard): String = {

    if (Chain.processChain(gameState, inputCard)) {
      var returnString: String = ""
      val newState: (BoardTrait, Vector[Player], Int) = useCardLogic(inputCard)

      newState._3 match {
        case 0 =>
          doStep()
          this.board = newState._1
          gameState = gameStateMaster.UpdateGame()
            .withBoard(newState._1)
            .withPlayers(newState._2)
            .withLastPlayedCard(inputCard.selectedCard)
            .withNextPlayer()
            .buildGame

          removeSelectedCard(InputCardMaster.actualPlayerIdx, InputCardMaster.cardNum._1)
          SelectedState.reset
          JokerState.reset
          returnString = s"Player ${gameState.players._1(gameState.players._2).consoleColor}${gameState.players._1(gameState.players._2).nameAndIdx}${Console.RESET}'s turn\n"
          publish(new BoardChanged)
        case 1 =>
          println("joker packingState: " + (if (JokerState.state.equals(JokerState.unpacked)) "unpacked" else "packed"))
          gameState = gameStateMaster.UpdateGame()
            .withBoard(newState._1)
            .withPlayers(newState._2)
            .withLastPlayedCard(inputCard.selectedCard)
            .buildGame
          SelectedState.reset
          publish(new BoardChanged)
        case _ =>
          returnString = s"Move was not possible! Please retry player ${gameState.players._1(gameState.players._2).consoleColor}${gameState.players._2}${Console.RESET} ;)\n"
      }
      returnString
    } else {
      if (!Chain.processStdCheck(gameState, inputCard)) {
        gameState = gameStateMaster.UpdateGame()
          .withLastPlayedCard(inputCard.selectedCard)
          .buildGame
        noMovesPossible(inputCard)
      }
      "failed Check !!!"
    }
  }

  def noMovesPossible(inputCard: InputCard): Unit = {
    val player = gameState.actualPlayer.copy(cardList = Nil)
    val playerVector = gameState.players._1.updated(player.nameAndIdx._2, player)
    gameState = gameStateMaster.UpdateGame()
      .withPlayers(playerVector)
      .withNextPlayer()
      .buildGame
    publish(new BoardChanged)
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
   * @param cardIdx   is the index of the removed card
   * @return the new card
   */
  override def removeSelectedCard(playerIdx: Int, cardIdx: Int): CardTrait = {
    val player: Vector[Player] = gameState.players._1
    val oldCard = player(playerIdx).getCard(cardIdx)
    val newPlayer: Vector[Player] = player.updated(playerIdx, player(playerIdx).removeCard(oldCard))
    doStep()
    gameState = gameStateMaster.UpdateGame()
      .withLastPlayedCard(oldCard)
      .withPlayers(newPlayer)
      .buildGame
    oldCard
  }

  /**
   * creates a new Board with <code>size</code>
   *
   * @param size is the size of the new Board
   * @return a new Board
   */
  override def createNewBoard(size: Int): BoardTrait = {
    board = new Board(size)
    gameState = gameStateMaster.UpdateGame().withBoard(board).buildGame
    publish(new BoardChanged)
    board
  }

  /**
   * creates a new Board using dependency injection
   *
   * @return a new Board
   */
  override def createNewBoard: BoardTrait = {
    board.size match {
      case 4 => board = injector.instance[BoardTrait](Names.named("nano"))
      case 8 => board = injector.instance[BoardTrait](Names.named("micro"))
      case 20 => board = injector.instance[BoardTrait](Names.named("small"))
      case 64 => board = injector.instance[BoardTrait](Names.named("normal"))
      case 80 => board = injector.instance[BoardTrait](Names.named("big"))
      case 96 => board = injector.instance[BoardTrait](Names.named("extra big"))
      case 128 => board = injector.instance[BoardTrait](Names.named("ultra big"))
      case _ =>
    }
    gameState = gameStateMaster.UpdateGame().withBoard(board).buildGame
    publish(new BoardChanged)
    board
  }

  /**
   * creates a new random Board with <code>size</code>
   *
   * @param size is the size of the new Board
   * @return a new random Board
   */
  def createRandomBoard(size: Int): BoardTrait = {
    val board = new BoardCreateStrategyRandom().createNewBoard(size)
    this.board = board
    gameState = gameStateMaster.UpdateGame().withBoard(board).buildGame
    publish(new BoardChanged)
    board
  }


  /**
   * create a Vector[Player]
   *
   * @param playerNames is a List of player names
   * @param pieceAmount is the amount of pieces each player gets
   * @return a Vector
   */
  override def createPlayers(playerNames: List[String], pieceAmount: Int): Vector[Player] = {
    val players: Vector[Player] = playerNames.indices.map(i => Player.PlayerBuilder()
      .withColor(gameStateMaster.colors(i))
      .withName((playerNames(i), i))
      .withPiece(pieceAmount, (gameState.board.size / pieceAmount) * i)
      .build()).toVector
    gameState = gameStateMaster.UpdateGame()
      .withPlayers(players)
      .buildGame
    players
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
    player.foreach(x => playerHands = playerHands + s"${x.consoleColor}${x.nameAndIdx._1}${Console.RESET} --> myHand: " + x.cardList + "\n")
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
