package aview.gui

import controller.Controller
import util.Observer

import scala.swing._

class SwingGui(controller: Controller) extends Frame with Observer {
  title = "Dog a huge fucking mess of a game"
  visible = true
  centerOnScreen()
  resizable = false
  pack()


  override def update: Unit = {
    contents = SwingGui.getPanel(controller)
    repaint()
  }
}

object SwingGui {
  def getPanel(controller: Controller): Panel = {
    new WelcomePanel(controller)
  }
}
