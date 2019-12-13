package aview.gui

import javafx.scene.layout.GridPane
import scalafx.Includes.when
import scalafx.geometry.Insets
import scalafx.scene.Node
import scalafx.scene.control.{Button, SplitPane, TextArea}
import scalafx.scene.image.ImageView
import scalafx.scene.layout.{BorderPane, StackPane}

import scala.swing.{Component, Orientation}
import scala.swing.event.ActionEvent

object CardPanel {

  def newGridOnCard : GridPane ={
    val gridCard = new GridPane{
      setPadding(Insets(250, 10, 45, 30))
      var b1, b2, b3 = new Button("test"){
        style = "-fx-padding:5;-fx-background-color:#383838;-fx-text-fill:white"
        onAction = { _ =>
          println("pressed test")
        }
      }
      add(b1, 0, 3)
      add(b2, 1, 3)
      add(b3, 2, 3)
    }
    gridCard
  }

  def newCardViews(): SplitPane = {
    new SplitPane {
      style = "-fx-padding:5;-fx-background-color:#383838;"
      val grid = new GridPane {
        setPadding(Insets(100, 5, 10, 500))
        var b1, b2, b3 = new Button("", new ImageView("file:ace.png") {
          fitHeight = 250
          fitWidth = 150
        }) {
          style = "-fx-padding:5;-fx-background-color:#383838;"
          onAction = { _ =>
            graphic = new Button("used")
          }
        }
        val stackPane1,stackPane2,stackPane3 = new StackPane()

        stackPane1.getChildren.addAll(b1,newGridOnCard )
        stackPane2.getChildren.addAll(b2,newGridOnCard )
        stackPane3.getChildren.addAll(b3,newGridOnCard )


        add(stackPane1, 0, 0)
        add(stackPane2, 1, 0)
        add(stackPane3, 2, 0)
      }

      items.add(grid)
    }
  }
}

object BoardPanel {

  def newBoardView(): Node = {
    val b1, b2, b3, b4, b5 = new Button("", new ImageView("file:field.png")) {
      style = "-fx-padding:0;-fx-background-color:#383838"
    }

    b1.pressed onChange {
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
