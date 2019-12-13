package aview.gui

import javafx.scene.layout.GridPane
import scalafx.Includes.when
import scalafx.geometry.Insets
import scalafx.scene.Node
import scalafx.scene.control.{Button, SplitPane, TextArea}
import scalafx.scene.image.ImageView
import scalafx.scene.layout.BorderPane

import scala.swing.{Component, Orientation}
import scala.swing.event.ActionEvent

object CardPanel {

  def newCardViews(): BorderPane = {

    val cardViewAndPlayView: SplitPane = new SplitPane {
      Orientation.Horizontal
      style = "-fx-background-color:#383838"
      val view: GridPane = new GridPane {
        padding = Insets(100, 5, 10, 500)
        val b1, b2, b3 = new Button("", new ImageView("file:ace.png") {
          fitHeight = 250
          fitWidth = 150
        }) {
          //style = "-fx-padding:5;-fx-background-color:#383838"
        }
        b1.pressed onChange {
          println("HALLO IHR MOTHER FUCKER!")
        }
        add(b1, 0, 0)
        add(b2, 1, 0)
        add(b3, 2, 0)
      }
      items.add(view)
      // items.add(new TextArea("ss"))
    }

    val cardPane = new BorderPane {
      center = cardViewAndPlayView
    }
    cardPane
  }
}

object BoardPanel {

  def newBoardView(): Node = {
    val b1, b2, b3, b4, b5 = new Button("", new ImageView("file:field.png")) {
      style = "-fx-padding:0;-fx-background-color:#383838"
    }
    On
    b1.pressed onChange{
      println("I am a field")
    }

    val splitPane = new SplitPane {
      val view: GridPane = new GridPane {
        padding = Insets(100, 0, 10, 500)
        style = "-fx-background-color:#383838"



        add(b1, 5, 0)
        add(b2, 6, 0)
        add(b3, 7, 0)
      }
      items.add(view)
    }
    splitPane
  }
}
