package dog.controller

import dog.util.SelectedState

import scala.util.{Failure, Success, Try}


object Chain {

  val loggingFilter: (Boolean, String) => (Boolean, String) = (status: Boolean, msg: String) => {
    if (status) {
      (status, msg)
    } else {
      throw new Exception(msg)
    }
  }
  //such an expressive name :D
  val checkers: ((Boolean, String)) => (Boolean, String) =
    checkHandCard.tupled andThen
      loggingFilter.tupled andThen
      checkWon.tupled andThen
      loggingFilter.tupled andThen
      checkHandCard.tupled andThen
      loggingFilter.tupled andThen
      checkPiecesOnBoardAndPlayable.tupled andThen
      loggingFilter.tupled
  var gameState: GameState = new GameStateMaster().UpdateGame().buildGame
  var inputCard: InputCard = InputCardMaster.UpdateCardInput().buildCardInput()

  def apply(chainType: String): ((Boolean, String)) => (Boolean, String) = {
    chainType match {
      case "manageround" => checkHandCard.tupled andThen
        loggingFilter.tupled andThen
        checkPiecesOnBoardAndPlayable.tupled andThen
        loggingFilter.tupled andThen
        checkSelected.tupled andThen
        loggingFilter.tupled
    }
  }

  def checkHandCard: (Boolean, String) => (Boolean, String) = (status: Boolean, msg: String) => {
    (gameState.actualPlayer.cardList.nonEmpty && status, "handcard")
  }

  def checkPiecesOnBoardAndPlayable: (Boolean, String) => (Boolean, String) = (status: Boolean, msg: String) => {
    var isPieceOnBoard: Boolean = false
    gameState.actualPlayer.piece.foreach(x => if (gameState.board.cell(x._2.pos).isFilled) isPieceOnBoard = true)
    var isPlayable: Boolean = false
    gameState.actualPlayer.cardList.foreach(x => if (x.task.contains("play") || x.task.contains("joker")) isPlayable = true)
    (status && (isPieceOnBoard || isPlayable), "pieceonboard")
  }

  def checkSelected: (Boolean, String) => (Boolean, String) = (status: Boolean, msg: String) => {
    var isSelected: Boolean = false
    SelectedState.state match {
      case SelectedState.nothingSelected => if (inputCard.selectedCard.task.contains("play")) isSelected = true
      case SelectedState.ownPieceSelected => if (!inputCard.selectedCard.symbol.equals("swap")) isSelected = true else isSelected = false
      case SelectedState.otherPieceSelected => if (inputCard.selectedCard.symbol.equals("swap")) isSelected = true else isSelected = false
    }
    (isSelected, "selected")
  }

  def tryChain(chain: ((Boolean, String)) => (Boolean, String)): (Boolean, String) = {
    Try(chain) match {
      case Success(value) => (true, "")
      case Failure(exception) => (false, exception.getMessage)
    }
  }

  def handleFail(msg: String): String = {
    msg match {
      case "won" => "Player won"
      case _ => ""
    }
  }

  def checkWon: (Boolean, String) => (Boolean, String) = (status: Boolean, msg: String) => {
    (false, "won")
  }
}

//we could make similar compositions of the functions

object MainTest {
  def main(args: Array[String]): Unit = {
    val chain: Boolean = Try(Chain.checkers(true, "")) match {
      case Success(value) => true
      case Failure(exception) =>
        println(exception.getMessage)
        false
    }
  }
}
