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
    println(status)
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
    var isPlayable: Boolean = false
    gameState.actualPlayer.cardList.foreach(x => if (x.task.contains("play") || x.task.contains("joker")) isPlayable = true)
    predecessorStatus && (isPieceOnBoard || isPlayable)

  }

  def checkSelected(): Boolean => Boolean = (predecessorStatus: Boolean) => {
    print("checkSelected: ")
    var isSelected: Boolean = false
    val selTask = inputCard.selectedCard.task.split("\\s+")(inputCard.cardIdxAndOption._2)

    if (predecessorStatus) {

      SelectedState.state match {
        case SelectedState.nothingSelected => if (selTask.equals("play") || selTask.equals("joker")) isSelected = true
        case SelectedState.ownPieceSelected => if (!selTask.equals("swap")) isSelected = true else isSelected = false
        case SelectedState.otherPieceSelected => if (selTask.equals("swap")) isSelected = true else isSelected = false
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


