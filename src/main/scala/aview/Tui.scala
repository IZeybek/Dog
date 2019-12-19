package aview

import aview.gui.{GenGui, Gui}
import controller.Component.ControllerTrait
import util.Observer


class Tui(gui: Gui,controller: ControllerTrait) extends Observer {
  controller.add(this)

  def showMenu(): Unit = {
    print("Menu\n")
    println("normal -> yourCard <-> myPiece <-> (OtherPiece) <-> myPlayerNum <-> (otherPlayerNum)")
    println("swap   -> yourCard <-> myPiece <-> OtherPiece <-> myPlayerNum <-> (otherPlayerNum)")
    controller.doStep()
  }

  def processInput(input: String): String = {
    var result: String = ""

    input.split("\\s+").toList match {

      case "n" :: "player" :: player =>
        if (player.nonEmpty) {
          controller.createPlayers(player)
          result = if (player.size > 1)
            s"created ${player.size} players"
          else
            "created 1 player"
        } else {
          result = "no players created"
        }
      case "p" :: "card" :: Nil =>
        print(controller.toStringCardDeck)
        result = "printed cards"
      case "undo" :: Nil =>
        controller.undoCommand()
        result = "undone"
      case "redo" :: Nil =>
        controller.redoCommand()
        result = "redone"
      case "p" :: "board" :: Nil =>
        print(controller.toStringBoard)
        result = "printed board"
      case "p" :: "hands" :: Nil =>
        print(controller.toStringPlayerHands)
        result = "printed board"
      case "p" :: Nil =>
        print(controller.toStringBoard)
        print(controller.toStringPlayerHands)
        result = "printed game"
      case _ =>
        input.toList.filter(c => c != ' ').filter(_.isDigit).map(c => c.toString.toInt) match {
          case cardNum :: otherPlayer :: pieceNum1 :: pieceNum2 :: Nil =>
            controller.manageRound(otherPlayer, pieceNum = List(pieceNum1, pieceNum2), cardNum)
          case cardNum :: pieceNums =>
            result = controller.manageRound(-1, pieceNums, cardNum)
          case _ => println("try again!")
        }
    }
    result
  }

  override def update: Unit = {
    //TODO add Publisher instead of this ----------------------------------------------------------------------------------------------------------
    gui.stage = GenGui.newGUI(controller)
    println(controller.toStringBoard)
    println(controller.toStringPlayerHands)
  }

}

