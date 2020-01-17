package dog.controller.ControllerComponent.controllerBaseImpl

import com.google.inject.name.Names
import com.google.inject.{Guice, Inject, Injector}
import dog.DogModule
import dog.controller.ControllerComponent.ControllerTrait
import dog.controller.StateComponent._
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

import scala.util.{Failure, Success, Try}

class Controller @Inject()(var board: BoardTrait) extends ControllerTrait {

  override val undoManager: UndoManager = new UndoManager
  val injector: Injector = Guice.createInjector(new DogModule)
  val fileIo: FileIOTrait = injector.instance[FileIOTrait]
  override var gameStateMaster: GameStateMasterTrait = new GameStateMaster
  override var gameState: GameState = gameStateMaster.UpdateGame().withBoard(board).buildGame

  override def selectedField(clickedFieldIdx: Int): Int = {
    val clickedCell: CellTrait = gameState.board.cell(clickedFieldIdx)

    if (!clickedCell.isFilled)
      SelectedState.reset
    else {
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
  override def save(): Unit = {
    fileIo.save(gameState)
    publish(new BoardChanged)
  }

  /**
   * load the game from a file
   */
  override def load: String = {
    var returnString: String = "loading successful"
    val updatedGameState: GameState = Try(fileIo.load) match {
      case Success(value) => value
      case Failure(_) =>
        returnString = "loading failed due to missing save file"
        this.gameState
    }
    gameState = gameStateMaster.UpdateGame().loadGame(updatedGameState).buildGame
    publish(new BoardChanged)
    returnString
  }

  /**
   * Manages the round
   *
   * @param inputCard is a CardInput builder in order to parse information out of it
   * @return a String that is returned to the TUI for more information
   */
  override def manageRound(inputCard: InputCard): String = {
    var returnString: String = s"Move was not possible! Please retry player ${gameState.actualPlayer.toStringColor} ;)\n"
    val (bool: Boolean, checkStr: String) = check(inputCard, "manageround")
    if (bool) {
      val (newBoard: BoardTrait, newPlayer: Vector[Player], returnValue: Int) = useCardLogic(inputCard)
      returnValue match {
        case 0 =>
          doStep()
          this.board = newBoard
          gameState = gameStateMaster.UpdateGame().withBoard(newBoard).withPlayers(newPlayer).withLastPlayedCard(inputCard.selectedCard).withNextPlayer().buildGame
          removeSelectedCard(InputCardMaster.actualPlayerIdx, InputCardMaster.cardNum._1)
          JokerState.reset
          check(inputCard, "afterround")
          returnString = s"Player ${gameState.actualPlayer.toStringColor}${Console.RESET}'s turn\n"
        case 1 =>
          gameState = gameStateMaster.UpdateGame().withPlayers(newPlayer).withLastPlayedCard(inputCard.selectedCard).buildGame
        case _ =>
      }
    } else {
      returnString = checkStr
    }
    SelectedState.reset
    publish(new BoardChanged)
    returnString
  }

  /**
   * checks chain and handles the fail if false
   *
   * @param inputCard is needed for the chain
   * @param typeChain is used for the strategy pattern in Chain
   * @return if it failed and the message returned by handleFail in the case it failed
   */
  override def check(inputCard: InputCard, typeChain: String): (Boolean, String) = {
    val chain = Chain(gameState, inputCard)
    val (bool, str): (Boolean, String) = chain.tryChain(chain.apply(typeChain))
    (bool, if (!bool) handleFail(str) else "")
  }

  override def updateGame(): Unit = gameState = {
    gameState = gameStateMaster.UpdateGame().buildGame
    board = gameState.board
    gameState
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
    publish(new BoardChanged)
    newPlayer(playerNum)
  }

  override def toStringActivePlayerHand: String = {
    val player: Player = gameState.players._1(gameState.players._2)
    s"${gameState.actualPlayer.toStringColor}'s hand cards: " + player.cardList + "\n"
  }

  override def toStringPlayerHands: String = {
    val player: Vector[Player] = gameState.players._1
    var playerHands: String = ""
    player.foreach(x => playerHands = playerHands + s"${x.toStringColor}'s hand cards: " + x.cardList + "\n")
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

  /**
   * handles the fail
   *
   * @param msg is used to determine which error has to be handled
   * @return a message
   */
  private def handleFail(msg: String): String = {
    SelectedState.reset
    msg match {
      case "handcard" =>
        do {
          gameState = gameStateMaster.UpdateGame()
            .withNextPlayer().buildGame
          publish(new BoardChanged)
        } while (gameState.actualPlayer.cardList.isEmpty)
        s"${gameState.actualPlayer.toStringColor} has no hand cards"
      case "pieceonboard" =>
        givePlayerCards(gameState.players._2, Nil)
        gameState = gameStateMaster.UpdateGame().withNextPlayer().buildGame
        JokerState.reset
        publish(new BoardChanged)
        s"${gameState.actualPlayer.toStringColor} has neither pieces on board nor a card to play"
      case "won" =>
        s"${gameState.actualPlayer.toStringColor} won"
      case "selected" =>
        s"${gameState.actualPlayer.toStringColor} has to select a piece"
      case "noplayercards" =>
        gameState = gameStateMaster.UpdateGame().withNextRound().buildGame
        val amountCards: Int = gameStateMaster.roundAndCardsToDistribute._2
        gameState.players._1.indices.foreach(x => givePlayerCards(x, Card.RandomCardsBuilder().withAmount(amountCards).buildRandomCardList))
        "No player has cards. Cards are distributed again to all players"
      case _ => "unknown bug"
    }
  }
}
