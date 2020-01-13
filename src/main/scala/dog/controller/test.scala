package dog.controller

import dog.util.SelectedState

object Chain {

  var gameState: GameState = _
  var inputCard: InputCard = _

  def processChain(setGameState: GameState, setInputCard: InputCard): Boolean = {
    gameState = setGameState
    inputCard = setInputCard
    val isSuccess = processCheck(true)
    if (isSuccess) println("-> success") else println("-> failed")
    isSuccess
  }

  def processStdCheck(setGameState: GameState, setInputCard: InputCard): Boolean = {
    gameState = setGameState
    inputCard = setInputCard
    val isSuccess = stdCheck(true)
    if (isSuccess) println("-> success") else println("-> failed")
    isSuccess
  }


  val loggingFilter: Boolean => Boolean = (status: Boolean) => {
    if (status) println("SUCCESS") else println("FAILED")
    status
  }

  val stdCheck: Boolean => Boolean =

    checkHandCards() andThen
    loggingFilter andThen
    checkPiecesOnBoardAndPlayable() andThen
    loggingFilter

  //such an expressive name :D
  val processCheck: Boolean => Boolean =

    checkHandCards() andThen
      loggingFilter andThen
      checkPiecesOnBoardAndPlayable() andThen
      loggingFilter andThen
      checkSelected() andThen
      loggingFilter

  def checkHandCards(): Boolean => Boolean = (predecessorStatus: Boolean) => {
    print("checkHandCards: ")
    predecessorStatus && gameState.actualPlayer.cardList.nonEmpty
  }

  def checkPiecesOnBoardAndPlayable(): Boolean => Boolean = (predecessorStatus: Boolean) => {
    print("checkPiecesOnBoardAndPlayable: ")
    var isPieceOnBoard: Boolean = false
    gameState.actualPlayer.piece.foreach(x => if (gameState.board.cell(x._2.pos).isFilled) {
      isPieceOnBoard = true
    })
    println("isPieceOnBoard?: " + isPieceOnBoard)
    var isPlayable: Boolean = false
    //println("isPlayable?: " + isPlayable)
    gameState.actualPlayer.cardList.foreach(x => if (x.task.contains("play") || x.task.contains("joker")) isPlayable = true)
    predecessorStatus && (isPieceOnBoard || isPlayable)

  }

  def checkSelected(): Boolean => Boolean = (predecessorStatus: Boolean) => {
    print("checkSelected: ")
    var isSelected: Boolean = false
    if (predecessorStatus) {

      SelectedState.state match {
        case SelectedState.nothingSelected => if (inputCard.selectedCard.task.contains("play") || inputCard.selectedCard.task.contains("joker")) isSelected = true
        case SelectedState.ownPieceSelected => if (!inputCard.selectedCard.task.contains("swap")) isSelected = true else isSelected = false
        case SelectedState.otherPieceSelected => if (inputCard.selectedCard.task.contains("swap")) isSelected = true else isSelected = false
      }
    }

    predecessorStatus && isSelected
  }

  //we could make similar compositions of the functions
}

//object test {
//Test it out!
// def process() = {
//   if (Chain.processMsg(true)) println("success") else println("FAAAAAIIILED")
// }

//}

//object Main {
//  def main(args: Array[String]): Unit = {
//    test.process()
//  }
//}


