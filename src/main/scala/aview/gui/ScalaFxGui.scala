package aview.gui

import controller.Component.ControllerTrait
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.control.{Menu, MenuBar, MenuItem}
import scalafx.scene.layout.BorderPane
import util.Observer

class Gui(controller: ControllerTrait) extends JFXApp with Observer {

  controller.add(this)

  stage = GenGui.newGUI(controller)

  override def update: Unit = {
    println("updated something")
  }

  //
  //object SceneHandler extends Scene {
  //  def setScene(sceneName: String): Scene = {
  //    sceneName.toLowerCase match {
  ////      case "welcome" => new WelcomeScene()
  ////      case "main" =>  new MainScene()
  //    }
  //  }

  //  root = new WelcomeScene().rootPane
}

object GenGui {

  def newGUI(controller: ControllerTrait): PrimaryStage = {

    val mainScene: Scene = new Scene(1500, 700) {

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
      root = new BorderPane() {

        top = menuBar
        // has to be a number that can be devided by 4
        center = BoardPanel.newBoardPane(controller)
        //number of Cards can be set here
        bottom = CardPanel.newCardPane(controller)
      }
    }
    val stage = new PrimaryStage {
      title = "main"
    }
    //    stage.setFullScreen(true)
    stage.scene = mainScene
    stage
  }
}

//class MainScene extends Scene {
//  val rootPane: BorderPane = new BorderPane {
//    val menuBar: MenuBar = new MenuBar {
//      useSystemMenuBar = true
//      minWidth = 100
//      val menuList: Menu = new Menu("Edit") {
//        items.add(new MenuItem("Undo"))
//        items.add(new MenuItem("Redo"))
//        items.add(new MenuItem("Save"))
//      }
//      menus.add(menuList)
//    }
//    style = "-fx-background-color:#383838"
//    top = menuBar
//    // has to be a number that can be devided by 4
//    center = BoardPanel.newBoardPane(64)
//    //number of Cards can be set here
//    bottom = CardPanel.newCardPane(20)
//  }
//}
//
//object WelcomeScene extends Scene {
//
//
//
//  val primaryStage = new PrimaryStage{
//    new HBox {
//      style = "-fx-background-color:#383838"
//      padding = Insets(50, 80, 50, 80)
//      children = Seq(
//        new Text {
//          text = s"Dog - Mensch ärgere dich nicht"
//          style = "-fx-font-size: 48pt"
//          fill = new LinearGradient(
//            endX = 0,
//            stops = Stops(PaleGreen, SeaGreen))
//          effect = new DropShadow {
//            color = DodgerBlue
//            radius = 25
//            spread = 0.25
//          }
//        }
//      )
//    }
//  }
//}

