package dog.util

import dog.controller.StateComponent.{GameState, InputCardMaster}

trait SelectionState {
  def changeState(gameState: GameState, clickedIdx: Int): SelectionState
}

object SelectedState {
  var state: SelectionState = nothingSelected
  var ownFieldClicked: Int = -1
  var otherFieldClicked: Int = -1
  var ownPieceClicked: Int = -1
  var otherPieceClicked: Int = -1

  def handle(gameState: GameState, idx: Int): SelectionState = state.changeState(gameState, idx)

  def reset: SelectionState = {
    ownFieldClicked = -1
    otherFieldClicked = -1
    ownPieceClicked = -1
    otherPieceClicked = -1
    InputCardMaster.UpdateCardInput().reset()
    state = nothingSelected
    state
  }

  //noinspection DuplicatedCode
  object nothingSelected extends SelectionState {
    override def changeState(gameState: GameState, clickedIdx: Int): SelectionState = {

      ownPieceClicked = gameState.board.getPieceIndex(clickedIdx)
      InputCardMaster.UpdateCardInput()
        .withPieceNum(List(ownPieceClicked))

      ownFieldClicked = clickedIdx
      state = ownPieceSelected
      state
    }
  }

  //noinspection DuplicatedCode
  object ownPieceSelected extends SelectionState {
    override def changeState(gameState: GameState, clickedIdx: Int): SelectionState = {

      otherPieceClicked = gameState.board.getPieceIndex(clickedIdx)
      InputCardMaster.UpdateCardInput()
        .withOtherPlayer(gameState.board.cell(clickedIdx).p.head.nameAndIdx._2)
        .withPieceNum(List(ownPieceClicked, otherPieceClicked))

      otherFieldClicked = clickedIdx
      state = otherPieceSelected
      state
    }
  }

  object otherPieceSelected extends SelectionState {
    override def changeState(gameState: GameState, clickedIdx: Int): SelectionState = {
      reset
    }
  }

}
