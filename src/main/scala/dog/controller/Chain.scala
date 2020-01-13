package dog.controller

import dog.util.SelectedState

import scala.util.{Failure, Success, Try}


case class Chain(gameState: GameState, inputCard: InputCard) {

  def apply(chainType: String): ((Boolean, String)) => (Boolean, String) = {
    chainType match {
      case "manageround" =>
        checkHandCard.tupled andThen
          loggingFilter.tupled andThen
          checkPiecesOnBoardAndPlayable.tupled andThen
          loggingFilter.tupled andThen
          checkPlayCard.tupled andThen
          loggingFilter.tupled andThen
          checkSelected.tupled andThen
          loggingFilter.tupled
      case _ => loggingFilter.tupled
    }
  }

  def loggingFilter: (Boolean, String) => (Boolean, String) = (status: Boolean, msg: String) => {
    if (status) {
      (status, msg)
    } else {
      throw new Exception(msg)
    }
  }

  def checkHandCard: (Boolean, String) => (Boolean, String) = (status: Boolean, msg: String) => {
    (gameState.players._1(gameState.players._2).cardList.nonEmpty && status, "handcard")
  }

  def checkPiecesOnBoardAndPlayable: (Boolean, String) => (Boolean, String) = (status: Boolean, msg: String) => {
    var isPieceOnBoard: Boolean = false
    gameState.actualPlayer.piece.foreach(x => if (gameState.board.cell(x._2.pos).isFilled) isPieceOnBoard = true)
    var isPlayable: Boolean = false
    gameState.actualPlayer.cardList.foreach(x => if (x.task.contains("play") || x.task.contains("joker")) isPlayable = true)
    ((isPieceOnBoard || isPlayable) && status, "pieceonboard")
  }

  def checkPlayCard: (Boolean, String) => (Boolean, String) = (status: Boolean, msg: String) => {
    val opt: Int = inputCard.cardIdxAndOption._2
    ((inputCard.selectedCard.parse(opt).task.contains("play") || inputCard.selectedCard.task.contains("joker")) && status, "play")
  }

  def checkSelected: (Boolean, String) => (Boolean, String) = (status: Boolean, msg: String) => {
    var isSelected: Boolean = false
    SelectedState.state match {
      case SelectedState.nothingSelected => if (inputCard.selectedCard.task.contains("play")) isSelected = true
      case SelectedState.ownPieceSelected => if (!inputCard.selectedCard.symbol.equals("swap")) isSelected = true else isSelected = false
      case SelectedState.otherPieceSelected => if (inputCard.selectedCard.symbol.equals("swap")) isSelected = true else isSelected = false
    }
    (isSelected && status, "selected")
  }

  def checkWon: (Boolean, String) => (Boolean, String) = (status: Boolean, msg: String) => {
    (false, "won")
  }

  def tryChain(chain: ((Boolean, String)) => (Boolean, String)): (Boolean, String) = {
    Try(chain(true, "")) match {
      case Success(value) => (true, "")
      case Failure(exception) => (false, exception.getMessage)
    }
  }
}

//we could make similar compositions of the functions

object MainTest {
  def main(args: Array[String]): Unit = {
    val gameState: GameState = new GameStateMaster().UpdateGame().buildGame
    val inputCard: InputCard = InputCardMaster.UpdateCardInput().buildCardInput()
    val chain: Chain = Chain(gameState, inputCard)
    println(chain.tryChain(chain.apply("manageround")))
  }
}
