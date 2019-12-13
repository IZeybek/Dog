package aview.gui

import scalafx.Includes._
import scalafx.geometry.Orientation
import scalafx.scene.Group
import scalafx.scene.control.{Label, Button, ToolBar}


object Sidebar extends ToolBar {
  orientation = Orientation.Vertical

  class SidebarButton(labelText: String) extends Button {
    private val buttonLabel = new Label(labelText) {
      rotate = -90
    }

    def buttonText = buttonLabel.text
    def buttonText_=(inText: String): Unit = {
      buttonLabel.text = inText
    }

    graphic = new Group(buttonLabel)
  }

  private lazy val factsButton = new SidebarButton("Hide facts") {
    onAction = { _ =>
        buttonText = "Show facts"
    }
  }

  items += factsButton

}