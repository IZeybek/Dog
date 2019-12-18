package aview.gui

import controller.Controller
import javafx.scene.layout.GridPane
import model.CardComponent.Card
import scalafx.Includes.when
import scalafx.geometry.Insets
import scalafx.scene.control.{Button, ScrollPane}
import scalafx.scene.image.ImageView
import scalafx.scene.layout.StackPane

object CardPanel {

  val stdPath = "file:src/main/scala/resources/"
  val bgColor: String = "-fx-background-color:#383838;"

  def newCardPane(controller: Controller): ScrollPane = {
    val actualPlayer = controller.gameState.players._2
    val cardList = controller.gameState.players._1(actualPlayer).cardList
    val amount = cardList.size

    //each Card has 3 Button Options. Its amount is not dynamic yet ------------------------------------------------------------------------
    var idx = 0;
    val gridSeq = {
      Seq.fill(amount)(new GridPane {
        setPadding(Insets(0, 5, 5, 55))
        val card: Card = cardList(idx)
        val task: Array[String] = card.getTask.split("\\s+")
        val amount: Int = task.length

        if (amount == 2) setPadding(Insets(0, 5, 5, 24))
        else if (amount == 3) setPadding(Insets(0, 5, 5, 43))

        val icon: Seq[Button] = newIcons(amount, card);
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
      val cards: Seq[Button] = newCards(amount, cardList)

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
  def newIcons(amount: Int, card: Card): Seq[Button] = {
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
      style <== when(hover) choose styleFirst + "-fx-background-color:#d3d3d3;" otherwise styleFirst


      //PlayButton ActionListener
      onAction = _ => println(s"${card.getSymbol} Button pressed")

    })
  }

  //generates new Cards and puts it into Seq
  def newCards(amount: Int, cardList: List[Card]): Seq[Button] = {
    var idx = 0;
    Seq.fill(amount)(new Button("", GenImages.genCard(cardList(idx).getSymbol)) {
      style = bgColor
      idx = idx + 1

    })
  }

  object GenImages {

    def genCard(typ: String): ImageView = new ImageView(stdPath + typ + ".png") {
      fitHeight = 200;
      fitWidth = 125
    }

    def genIcon(typ: String) = new ImageView(stdPath + typ + ".png") {
      //playButton size
      fitHeight = 20
      fitWidth = 20
    }
  }

}

object BoardPanel {
  val stdPath = "file:src/main/scala/resources/"
  val bgColor: String = "-fx-background-color:#383838;"

  def newBoardPane(controller: Controller): ScrollPane = {
    val amount = controller.gameState.board.boardMap.size
    val fieldIconSeq = Seq.fill(amount)(new Button("", new ImageView(stdPath + "field.png") {
      fitWidth = 35
      fitHeight = 35
    }) {
      //Padding of FieldButtons
      val styleFirst = bgColor + "-fx-padding:0;"
      style <== when(hover) choose styleFirst + "-fx-background-color:#d3d3d3;" otherwise styleFirst

      //field OnClickListener
      onAction = _ => print(controller.gameState.gameState)
    })

    new ScrollPane() {
      fitToWidth = true
      fitToHeight = true
      style = bgColor
      content() = newBoardGrid(amount, fieldIconSeq)
    }
  }

  def newBoardGrid(amount: Int, fieldIconSeq: Seq[Button]): GridPane = {

    new GridPane {

      var offset = 20
      if (amount < 106) offset = (1920 - ((amount * 2 / 4) + 2) * 35) / 3 + 60
      setPadding(Insets(75, offset, 75, offset))
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

