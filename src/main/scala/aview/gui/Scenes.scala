package aview.gui

import controller.Controller
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.{Menu, MenuBar, MenuItem}
import scalafx.scene.effect.DropShadow
import scalafx.scene.layout.{BorderPane, HBox}
import scalafx.scene.paint.Color.{DodgerBlue, PaleGreen, SeaGreen}
import scalafx.scene.paint.{LinearGradient, Stops}
import scalafx.scene.text.Text
import util.Observer

class Gui(controller: Controller) extends JFXApp with Observer {
  controller.add(this)
  stage = new PrimaryStage {
    title = "Dog"
    scene = new SceneHandler
  }

  override def update: Unit = {
    println("updated something")
  }
}

class SceneHandler extends Scene {
  def setScene(sceneName: String): Unit = {
    sceneName.toLowerCase match {
      case "welcome" => root = new WelcomeScene().rootPane
      case "main" => root = new MainScene().rootPane
    }
  }

  root = new WelcomeScene().rootPane
}

class MainScene extends Scene {
  val rootPane: BorderPane = new BorderPane {
    val menuBar: MenuBar = new MenuBar {
      useSystemMenuBar = true
      minWidth = 100
      val menuList: Menu = new Menu("Edit") {
        items.add(new MenuItem("Undo"))
        items.add(new MenuItem("Redo"))
        items.add(new MenuItem("Save"))
      }
      menus.add(menuList)
    }
    style = "-fx-background-color:#383838"
    top = menuBar
    // has to be a number that can be devided by 4
    center = BoardPanel.newBoardPane(64)
    //number of Cards can be set here
    bottom = CardPanel.newCardPane(20)
  }
}

class WelcomeScene extends Scene {

  val rootPane: HBox = new HBox {
    style = "-fx-background-color:#383838"
    padding = Insets(50, 80, 50, 80)
    children = Seq(
      new Text {
        text = s"Dog - Mensch ärgere dich nicht"
        style = "-fx-font-size: 48pt"
        fill = new LinearGradient(
          endX = 0,
          stops = Stops(PaleGreen, SeaGreen))
        effect = new DropShadow {
          color = DodgerBlue
          radius = 25
          spread = 0.25
        }
      }
    )
  }
}

