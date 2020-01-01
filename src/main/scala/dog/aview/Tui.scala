package dog.aview

import dog.controller.{BoardChanged, ControllerTrait, InputCardMaster}

import scala.swing.Reactor


class Tui(controller: ControllerTrait) extends Reactor {

  listenTo(controller)

  reactions += {
    case event: BoardChanged =>
      println(controller.toStringBoard)
      println(controller.toStringActivePlayerHand)
  }

  def processInput(input: String): String = {
    var result: String = ""

    input.split("\\s+").toList match {

      case "n" :: "player" :: player =>
        if (player.nonEmpty) {
          controller.createPlayers(player, 4)
          result = if (player.size > 1)
            s"created ${player.size} players"
          else
            "created 1 player"
        } else {
          result = "no players created"
        }
      case "undo" :: Nil =>
        controller.undoCommand()
        result = "undone"
      case "redo" :: Nil =>
        controller.redoCommand()
        result = "redone"
      case "p" :: "card" :: Nil =>
        print(controller.toStringCardDeck)
        result = "printed cards"
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
        val actualPlayer = controller.gameState.actualPlayer
        input.toList.filter(c => c != ' ').filter(_.isDigit).map(c => c.toString.toInt) match {
          //for having full control
          case cardNum :: cardOption :: otherPlayer :: pieceNum1 :: pieceNum2 :: Nil =>
            result = controller.manageRound(InputCardMaster.UpdateCardInput()
              .withActualPlayer(actualPlayer)
              .withOtherPlayer(otherPlayer)
              .withPieceNum(List(pieceNum1, pieceNum2))
              .withCardNum((cardNum, cardOption))
              .withSelectedCard(controller.getSelectedCard(actualPlayer, (cardNum, cardOption)))
              .buildCardInput())

          //for swapping
          case cardNum :: otherPlayer :: pieceNum1 :: pieceNum2 :: Nil =>
            result = controller.manageRound(InputCardMaster.UpdateCardInput()
              .withActualPlayer(actualPlayer)
              .withOtherPlayer(otherPlayer)
              .withPieceNum(List(pieceNum1, pieceNum2))
              .withCardNum((cardNum, 0))
              .withSelectedCard(controller.getSelectedCard(actualPlayer, (cardNum, 0)))
              .buildCardInput())

          //for cards having multiple options
          case cardNum :: cardOption :: pieceNum :: Nil =>
            result = controller.manageRound(InputCardMaster.UpdateCardInput()
              .withActualPlayer(actualPlayer)
              .withPieceNum(List(pieceNum))
              .withCardNum((cardNum, cardOption))
              .withSelectedCard(controller.getSelectedCard(actualPlayer, (cardNum, cardOption)))
              .buildCardInput())

          //for easy moving
          case cardNum :: pieceNum :: Nil =>
            result = controller.manageRound(InputCardMaster.UpdateCardInput()
              .withActualPlayer(actualPlayer)
              .withPieceNum(List(pieceNum, -1))
              .withCardNum((cardNum, 0))
              .withSelectedCard(controller.getSelectedCard(actualPlayer, (cardNum, 0)))
              .buildCardInput())

          case _ => result = ""
        }

    }
    result
  }
}

