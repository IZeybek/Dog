package dog.aview

import dog.controller.BoardChanged
import dog.controller.ControllerComponent.ControllerTrait
import dog.controller.StateComponent.InputCardMaster


class Tui(controller: ControllerTrait) extends Reactor {

  var result = ""
  listenTo(controller)

  reactions += {
    case event: BoardChanged =>
      println(controller.toStringBoard)
      println(controller.toStringGarage)
      println(controller.toStringActivePlayerHand)
      println(controller.lastMessage)
  }

  def processInput(input: String): String = {
    input.split("\\s+").toList match {
      case "n" :: "player" :: player =>
        if (player.nonEmpty) {
          controller.createPlayers(player, 4, 6)
          result = if (player.size > 1)
            s"created ${player.size} players"
          else
            "created 1 player"
        } else result = "no players created"
      case "save" :: Nil =>
        controller.save()
        result = "saved game"
      case "load" :: Nil =>
        result = controller.load
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
        input.toList.filter(c => c != ' ').filter(_.isDigit).map(c => c.toString.toInt) match {
          //for swapping now with Clicked field which is necessary
          case cardNum :: otherPlayer :: pieceNum1 :: pieceNum2 :: Nil =>
            val fieldPosOwn = controller.gameState.actualPlayer.piece(pieceNum1).pos
            val fieldPosOther = controller.gameState.players._1(otherPlayer).piece(pieceNum2).pos
            controller.selectedField(fieldPosOwn)
            controller.selectedField(fieldPosOther)
            result = controller.manageRound(InputCardMaster.UpdateCardInput()
              .withOtherPlayer(otherPlayer)
              .withCardNum((cardNum, 0))
              .withSelectedCard(controller.actualPlayedCard(cardNum))
              .buildCardInput())

          //for cards having multiple options
          case cardNum :: cardOption :: pieceNum :: Nil =>
            val fieldPos = controller.gameState.actualPlayer.piece(pieceNum).pos
            controller.selectedField(fieldPos)
            result = controller.manageRound(InputCardMaster.UpdateCardInput()
              .withCardNum((cardNum, cardOption))
              .withSelectedCard(controller.actualPlayedCard(cardNum))
              .buildCardInput())

          //for play Cards
          // clicked field is not necessary for the cards with the task 'play' as u can't select a Piece
          case cardNum :: cardOption :: Nil =>
            result = controller.manageRound(InputCardMaster.UpdateCardInput()
              .withCardNum((cardNum, cardOption))
              .withSelectedCard(controller.actualPlayedCard(cardNum))
              .buildCardInput())

          //for easy moving
          //@TODO: this one can't be used anymore, as u have to select a Piece. it can be used for joker only
          case cardNum :: Nil =>
            result = controller.manageRound(InputCardMaster.UpdateCardInput()
              .withCardNum((cardNum, 0))
              .withSelectedCard(controller.actualPlayedCard(cardNum))
              .buildCardInput())

          case _ => result = ""
        }
    }
    result
  }
}

