package aview.gui

import javafx.scene.layout.GridPane
import scalafx.geometry.Insets
import scalafx.scene.control.{Button, ScrollPane}
import scalafx.scene.image.ImageView
import scalafx.scene.layout.StackPane

object CardPanel {

  val bgColor: String = "-fx-background-color:#383838;"

  def newCardPane(amount: Int): ScrollPane = {
    //each Card has 3 Button Options. Its amount is not dynamic yet ------------------------------------------------------------------------
    val gridSeq = Seq.fill(amount)(newButtonsOnCard(3))

    val grid: GridPane = new GridPane {

      setStyle(bgColor + "-fx-padding:2")
      var offset = 0
      if (amount < 9) offset = 250
      setPadding(Insets(0, 0, 0, offset))
      val cards: Seq[Button] = newCards(amount, "file:ace.png")

      val stackPane: Seq[StackPane] = Seq.fill(amount)(new StackPane())

      stackPane.indices.foreach(i => stackPane(i).getChildren.addAll(cards(i), gridSeq(i)))
      stackPane.indices.foreach(i => add(stackPane(i), i, 0))
    }

    new ScrollPane() {
      fitToWidth = true
      style = bgColor
      content() = grid
    }
  }

  def newButtonsOnCard(amount: Int): GridPane = {

    new GridPane {
      setPadding(Insets(175, 5, 5, 18))
      for (i <- 0 until amount) {
        val icon = new ImageView("file:playbutton.png") {
          //playButton size
          fitHeight = 20
          fitWidth = 20
        }
        val button = new Button("", icon) {
          //style for circle Button
          style = "-fx-background-radius: 5em; " +
            "-fx-min-width: 15px; " +
            "-fx-min-height: 15px; " +
            "-fx-max-width: 15px; " +
            "-fx-max-height: 15px;" +
            "-fx-padding:5"

          //PlayButton ActionListener
          onAction = _ => println("play Button pressed")
        }
        // add into gridPane (node, column, row)
        add(button, i, 0)
      }
    }
  }

  //generates new Cards and puts it into Seq
  def newCards(amount: Int, file: String): Seq[Button] = {

    Seq.fill(amount)(new Button("", new ImageView(file) {
      fitHeight = 200
      fitWidth = 125
    }) {
      style = bgColor
    })
  }
}

object BoardPanel {

  val bgColor: String = "-fx-background-color:#383838;"

  def newBoardPane(amount: Int): ScrollPane = {

    val fieldIconSeq = Seq.fill(amount)(new Button("", new ImageView("file:field.png") {
      fitWidth = 35
      fitHeight = 35
    }) {
      //Padding of FieldButtons
      style = bgColor + "-fx-padding:0"
      //field OnClickListener
      onAction = _ => println("field Button pressed")
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
