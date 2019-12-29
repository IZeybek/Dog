package dog.aview.gui

import dog.aview.gui.CardPanel.stdPath
import dog.controller.{ControllerTrait, InputCardMaster}
import dog.model.BoardComponent.BoardTrait
import dog.model.CardComponent.CardTrait
import dog.model.Player
import javafx.scene.layout.GridPane
import scalafx.Includes.when
import scalafx.geometry.Insets
import scalafx.scene.control.{Button, Label, ScrollPane}
import scalafx.scene.image.ImageView
import scalafx.scene.layout.{BorderPane, StackPane, VBox}
import scalafx.scene.paint.Color._

object CardPanel extends PanelMaster {

  //generates new Cards and puts it into Seq
  def newIcons(controller: ControllerTrait, iconAmount: Int, currentCard: CardTrait, cardIdx: Int): Seq[Button] = {
    var idx = 0
    val task = currentCard.task.split("\\s+") //GenImages.genIcon()
    val symbol: Array[String] = currentCard.symbol.split("\\s+")
    val sbsize = symbol.length
    Seq.fill(iconAmount)(new Button(
      if (symbol(0).equals("4")) (if (idx == 0) "-" else "+") + symbol(0)
      else if (sbsize > 1 && idx != sbsize - 1) "+" + symbol(idx)
      else "",
      GenImages.genIconImage(task(idx))) {
      id = idx.toString
      //style for circle Button
      idx = idx + 1
      val styleFirst: String = "-fx-background-radius: 5em; " +
        "-fx-min-width: 30px; " +
        "-fx-min-height: 30px; " +
        "-fx-max-width: 100px; " +
        "-fx-max-height: 50px;" +
        "-fx-padding:5;"
      style <== when(hover) choose styleFirst + "-fx-background-color:#d3d3d3;" otherwise styleFirst

      //PlayButton ActionListener
      onAction = _ => {
        println("----------------------------------------- Clicked IconID : " + getId.toInt)
        controller.manageRound(InputCardMaster.UpdateCardInput()
          .withActualPlayer(controller.gameState.actualPlayer)
          .withPieceNum(List(0, 0))
          .withCardNum((cardIdx, getId.toInt))
          .withSelectedCard(controller.getSelectedCard(controller.gameState.players._2, (cardIdx, getId.toInt)))
          .buildCardInput())
      }

    })
  }

  //generates new Cards and puts it into Seq
  def newCards(gridSeq: Seq[GridPane], amount: Int, cardList: List[CardTrait]): Seq[Button] = {

    var idx = 0
    Seq.fill(amount)(new Button("", GenImages.genCardImage(cardList(idx).symbol)) {
      style = bgColor
      idx = idx + 1
    })
  }

}

object GenImages {

  def genCardImage(typ: String): ImageView = new ImageView(stdPath + typ + ".png") {
    fitHeight = 200
    fitWidth = 125
  }

  def genIconImage(typ: String): ImageView = new ImageView(stdPath + typ + ".png") {
    //playButton size
    fitHeight = 20
    fitWidth = 20
  }
}

object CardDeckPanel {
  val bgColor: String = "-fx-background-color:#383838;"

  val stdPath = "file:src/main/scala/resources/"

  def newCardDeck(c: ControllerTrait): StackPane = {

    val amountLabel = new Label(c.gameState.cardDeck._2.toString)
    val stackPane = new StackPane() {
      padding = Insets(30, 30, 30, 30)
    }

    new GridPane {

      val cardDeckIcon: ImageView = new ImageView(stdPath + "green_back.png") {
        fitHeight = 200
        fitWidth = 125
      }


      stackPane.getChildren.addAll(cardDeckIcon, amountLabel)
      add(stackPane, 0, 0)
    }
    stackPane
  }
}

object PlayerStatusPanel {

  def newStatusPane(controller: ControllerTrait): BorderPane = new BorderPane() {
    top = PlayerStatusPanel.newStatusDisplay(controller)
    center = PlayerStatusPanel.newLaidCard(controller)
  }

  def newLaidCard(c: ControllerTrait): Button = {
    val lastCard = if (!c.gameStateMaster.getLastPlayedCard.symbol.equals("pseudo")) c.gameStateMaster.getLastPlayedCard.symbol else "laidcarddeck"

    new Button("", new ImageView(stdPath + lastCard + ".png") {
      fitHeight = 200
      fitWidth = 125
    }) {
      style = "-fx-background-radius: 5em; " +
        "-fx-min-width: 30px; " +
        "-fx-min-height: 30px; " +
        "-fx-max-width: 100px; " +
        "-fx-max-height: 50px;" +
        "-fx-padding:5;"
    }
  }

  val bgColor: String = "-fx-background-color:#383838;"
  val stdPath = "file:src/main/scala/resources/"

  def newStatusDisplay(c: ControllerTrait): VBox = {
    val player: Player = c.gameState.players._1(c.gameState.players._2)
    val playerStateLabel = new Label(player.toString) {
      style = "-fxf-font-size: 20pt"
      textFill = player.color match {
        case "green" => Green;
        case "white" => White;
        case "yellow" => Yellow;
        case "red" => Red;
        case _ => Black
      }
    }
    var idx = 0
    val inHouse = Seq.fill(4)(new Button("") {
      val color: String = if (player.inHouse <= idx) "" else player.color
      idx = idx + 1
      val colorHouses: String = color match {
        case "green" => "-fx-background-color:#008000;";
        case "white" => "-fx-background-color:#FFFFFF;"
        case "yellow" => "-fx-background-color:#FFFF00;"
        case "red" => "-fx-background-color:#FF0000;"
        case _ => "-fx-background-color:#979797;"
      }
      style = "-fx-padding:10;-fx-background-radius: 5em; " +
        "-fx-min-width: 30px; " +
        "-fx-min-height: 30px; " +
        "-fx-max-width: 30px; " +
        "-fx-max-height: 30px;" + colorHouses

    })

    val gridHouse = new GridPane {
      setPadding(Insets(10, 20, 20, 20))
      inHouse.indices.foreach(i => add(inHouse(i), i, 0))
    }

    new VBox() {
      padding = Insets(100, 20, 20, 20)
      children.add(playerStateLabel)
      children.add(gridHouse)
    }
  }
}

object BoardPanel {

  val stdPath = "file:src/main/scala/resources/"
  val bgColor: String = "-fx-background-color:#383838;"

  def newBoardPane(controller: ControllerTrait): BorderPane = {
    new BorderPane() {
      style = bgColor

      val board: BoardTrait = controller.gameState.board
      val amount: Int = board.size
      //      println("----------------" + bm(0).getColor)
      var idx = 0
      val fieldIconSeq: Seq[Button] = Seq.fill(amount)(new Button("", new ImageView(
        stdPath +
          (if (!board.cell(idx).getColor.equals(" ")) board.cell(idx).getColor
          else "field") + ".png") {
        fitWidth = 35
        fitHeight = 35
      }) {
        id = idx.toString


        //Padding of FieldButtons
        val stdStyle: String = bgColor +
          "-fx-min-width: 30px; " +
          "-fx-min-height: 30px; " +
          "-fx-max-width: 50px; " +
          "-fx-max-height: 40px;" +
          "-fx-padding:4;" +
          "-fx-border-radius:10 ;" +
          "-fx-border-width:2;"


        val blackStyle: String = stdStyle + "-fx-background-color:#000000;"
        val whiteStyle: String = stdStyle + "-fx-background-color:#ffffff;"

        val red: String = "-fx-border-color:#ff0000;"
        val white: String = "-fx-border-color:#ffffff;"
        val yellow: String = "-fx-border-color:#ffff00;"
        val green: String = "-fx-border-color:#00FF00;"
        var borderColor = ""
        var homeColor = "test"

        controller.gameState.players._1.foreach(i => if (i.homePosition == idx && homeColor.equals("test")) homeColor = i.color)

        borderColor = homeColor match {
          case "green" => green
          case "white" => white
          case "yellow" => yellow
          case "red" => red
          case _ => "-fx-border-color:transparent;"
        }
        this.style <== when(pressed) choose blackStyle otherwise (when(hover) choose whiteStyle otherwise (if (controller.gameState.clickedFieldIdx == idx) whiteStyle else stdStyle + borderColor))
        idx = idx + 1
        //field OnClickListener
        onAction = _ => {
          println("pressed field = " + this.getId)
          val clickedCell = controller.gameState.board.cell(this.getId.toInt)
          InputCardMaster.UpdateCardInput()
            .withOtherPlayer(if (clickedCell.isFilled) clickedCell.p.head.nameAndIdx._2 else -1)
            .buildCardInput()
          controller.clickedField(this.getId.toInt)
        }
      })
      //
      center = new ScrollPane() {
        fitToWidth = true
        fitToHeight = true

        content() = newBoardGrid(amount, fieldIconSeq)
      }
      right = CardDeckPanel.newCardDeck(controller)
      left = PlayerStatusPanel.newStatusPane(controller)
    }
  }

  def newBoardGrid(amount: Int, fieldIconSeq: Seq[Button]): GridPane = {

    new GridPane {

      setPadding(Insets(50, 30, 30, 100))
      setStyle(bgColor)

      //computes and displays Board on view, as an horizontal rectangle
      val leftRightEdge: Int = (amount / 4) / 2
      val topBottomEdge: Int = (amount / 4) + leftRightEdge

      var fieldIdx = 0

      for (i <- 0 until topBottomEdge) {
        add(fieldIconSeq(fieldIdx), i + 1, 0); fieldIdx = fieldIdx + 1
      }
      for (i <- 0 until leftRightEdge) {
        add(fieldIconSeq(fieldIdx), topBottomEdge + 1, i + 1); fieldIdx = fieldIdx + 1
      }
      for (i <- topBottomEdge until 0 by -1) {
        add(fieldIconSeq(fieldIdx), i, leftRightEdge + 1); fieldIdx = fieldIdx + 1
      }
      for (i <- leftRightEdge until 0 by -1) {
        add(fieldIconSeq(fieldIdx), 0, i); fieldIdx = fieldIdx + 1
      }
    }
  }
}

