package aview.gui

import controller.Component.ControllerTrait
import javafx.scene.layout.GridPane
import model.BoardComponent.boardBaseImpl.Cell
import model.CardComponent.CardTrait
import model.Player
import scalafx.Includes.when
import scalafx.geometry.Insets
import scalafx.scene.control.{Button, Label, ScrollPane}
import scalafx.scene.image.ImageView
import scalafx.scene.layout.{BorderPane, StackPane, VBox}
import scalafx.scene.paint.Color._

object CardPanel {

  val stdPath = "file:src/main/scala/resources/"
  val bgColor: String = "-fx-background-color:#383838;"

  def newCardPane(controller: ControllerTrait): ScrollPane = {
    val actualPlayer = controller.gameState.players._2
    val cardList = controller.gameState.players._1(actualPlayer).cardList
    val amount = cardList.size

    //each Card has 3 Button Options. Its amount is not dynamic yet ------------------------------------------------------------------------
    var idx = 0;
    val gridSeq = {
      Seq.fill(amount)(new GridPane {
        setPadding(Insets(0, 5, 5, 55))
        val card: CardTrait = cardList(idx)
        val task: Array[String] = card.getTask.split("\\s+")
        val amount: Int = task.length

        if (amount == 2) setPadding(Insets(0, 5, 5, 24))
        else if (amount == 3) setPadding(Insets(0, 5, 5, 43))

        val icon: Seq[Button] = newIcons(controller, amount, card, idx);
        println("------------------------------::::: + " + idx)
        println(amount)
        // add into gridPane (node, column, row)
        if (amount == 3) icon.indices.foreach(i => add(icon(i), 0, i))
        else icon.indices.foreach(i => add(icon(i), i, 0))
        idx = idx + 1
      })

    }

    val grid: GridPane = new GridPane {
      setStyle(bgColor + "-fx-padding:2")
      var offset = 0
      if (amount < 9) offset = 250
      setPadding(Insets(0, 0, 0, offset))
      val cards: Seq[Button] = newCards(gridSeq, amount, cardList)

      val stackPane: Seq[StackPane] = Seq.fill(amount)(new StackPane())
      stackPane.indices.foreach(i => stackPane(i).getChildren.addAll(cards(i), gridSeq(i)))
      stackPane.indices.foreach(i => add(stackPane(i), i, 0))
    }

    new ScrollPane() {

      fitToWidth = true
      fitToHeight = true
      style = bgColor
      content() = grid
    }
  }


  //generates new Cards and puts it into Seq
  def newIcons(controller: ControllerTrait, amount: Int, card: CardTrait, cardNum: Int): Seq[Button] = {
    var idx = 0;
    val task = card.getTask.split("\\s+") //GenImages.genIcon()
    val symbol: Array[String] = card.getSymbol.split("\\s+")
    val sbsize = symbol.length
    Seq.fill(amount)(new Button(
      if (symbol(0).equals("4")) (if (idx == 0) "-" else "+") + symbol(0)
      else if (sbsize > 1 && idx != sbsize - 1) "+" + symbol(idx)
      else "",
      GenImages.genIcon(task(idx))) {

      //style for circle Button
      print(task(idx) + ", ")
      idx = idx + 1
      val styleFirst = "-fx-background-radius: 5em; " +
        "-fx-min-width: 30px; " +
        "-fx-min-height: 30px; " +
        "-fx-max-width: 100px; " +
        "-fx-max-height: 50px;" +
        "-fx-padding:5;"
      style <== when(armed) choose styleFirst + "-fx-background-color:#d3d3d3;" otherwise styleFirst

      //PlayButton ActionListener
      onAction = _ => {
        print("--------------------------------------------------------> " + cardNum)
        controller.manageRound(0, List(0, 0), cardNum)
      }

    })
  }

  //generates new Cards and puts it into Seq
  def newCards(gridSeq: Seq[GridPane], amount: Int, cardList: List[CardTrait]): Seq[Button] = {

    var idx = 0;
    Seq.fill(amount)(new Button("", GenImages.genCard(cardList(idx).getSymbol)) {
      style = bgColor
      idx = idx + 1
    })
  }

  object GenImages {

    def genCard(typ: String): ImageView = new ImageView(stdPath + typ + ".png") {
      fitHeight = 200
      fitWidth = 125
    }

    def genIcon(typ: String): ImageView = new ImageView(stdPath + typ + ".png") {
      //playButton size
      fitHeight = 20
      fitWidth = 20
    }
  }

}

object CardDeckPanel {
  val bgColor: String = "-fx-background-color:#383838;"

  val stdPath = "file:src/main/scala/resources/"

  def newCardDeck(c: ControllerTrait): StackPane = {

    val amountLabel = new Label(c.gameState.cardDeck._1.size.toString)
    val stackPane = new StackPane() {
      padding = Insets(30, 30, 30, 30)
    }

    new GridPane {

      val cardDeckIcon = new ImageView(stdPath + "green_back.png") {
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
  val bgColor: String = "-fx-background-color:#383838;"
  val stdPath = "file:src/main/scala/resources/"

  def newStatus(c: ControllerTrait): VBox = {
    val player: Player = c.gameState.players._1(c.gameState.players._2)
    val playerStateLabel = new Label(player.toString) {
      style = "-fxf-font-size: 20pt"
      textFill = player.c match {
        case "grün" => Green;
        case "white" => White;
        case "gelb" => Yellow;
        case "rot" => Red;
        case _ => Black
      }
    }
    val inHouse = Seq.fill(player.inHouse)(new Button("") {

      val colorHouses: String = player.c match {
        case "grün" => "-fx-background-color:#008000;";
        case "white" => "-fx-background-color:#FFFFFF;"
        case "gelb" => "-fx-background-color:#FFFF00;"
        case "rot" => "-fx-background-color:#FF0000;"
        case _ => "-fx-background-color:#000000;"
      }
      style = "-fx-padding:10;-fx-background-radius: 5em; " +
        "-fx-min-width: 30px; " +
        "-fx-min-height: 30px; " +
        "-fx-max-width: 30px; " +
        "-fx-max-height: 30px;" + colorHouses

    })

    val layedCard: Button = new Button("", new ImageView(stdPath + "layedcarddeck.png") {
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

    val gridHouse = new GridPane {
      setPadding(Insets(10, 20, 20, 20))
      inHouse.indices.foreach(i => add(inHouse(i), i, 0))
      //      add(layedCard, 0, 10)
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
      val amount: Int = controller.gameState.board.getBoardMap.size
      val bm: Map[Int, Cell] = controller.gameState.board.getBoardMap
      println("----------------" + bm(0).getColor)
      var idx = 0;
      val fieldIconSeq: Seq[Button] = Seq.fill(amount)(new Button("", new ImageView(
        stdPath +
          (if (!bm(idx).getColor.equals(" ")) bm(idx).getColor
          else "field") + ".png") {
        fitWidth = 35
        fitHeight = 35
      }) {
        id = idx.toString
        idx = idx + 1

        //Padding of FieldButtons
        val styleFirst: String = bgColor + "-fx-padding:0;"
        style <== when(hover) choose styleFirst + "-fx-background-color:#d3d3d3;" otherwise styleFirst
        //field OnClickListener
        onAction = _ => {
          println("pressed field = " + this.getId)
        }
      })
      //
      center = new ScrollPane() {
        fitToWidth = true
        fitToHeight = true

        content() = newBoardGrid(amount, fieldIconSeq)
      }
      right = CardDeckPanel.newCardDeck(controller)
      left = PlayerStatusPanel.newStatus(controller)
    }
  }

  def newBoardGrid(amount: Int, fieldIconSeq: Seq[Button]): GridPane = {

    new GridPane {

      setPadding(Insets(50, 30, 30, 30))
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

