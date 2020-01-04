package dog.aview.gui

import dog.controller.{BoardChanged, ControllerTrait, GuiChanged, JokerState}
import dog.model.CardComponent.cardBaseImpl.CardDeck
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.control.{Menu, MenuBar, MenuItem}
import scalafx.scene.layout.BorderPane

import scala.swing.Reactor

class Gui(controller: ControllerTrait) extends JFXApp with Reactor {

  listenTo(controller)

  stage = GenGui.newGUI(controller)

  reactions += {
    case event: BoardChanged => this.stage = GenGui.newGUI(controller)
    case event: GuiChanged => this.stage = GenGui.newGUI(controller)
  }
}

object GenGui {

  def newGUI(controller: ControllerTrait): PrimaryStage = {

    val mainScene: Scene = new Scene(1550, 800) {

      val menuBar: MenuBar = new MenuBar {
        useSystemMenuBar = true
        minWidth = 100
        val menuList: Menu = new Menu("Edit") {
          val undo: MenuItem = new MenuItem("Undo") {
            onAction = _ => controller.undoCommand()
          }
          val redo: MenuItem = new MenuItem("Redo") {
            onAction = _ => controller.redoCommand()
          }
          val save: MenuItem = new MenuItem("Save")

          items.add(undo)
          items.add(redo)
          items.add(save)
        }
        menus.add(menuList)
      }
      root = new BorderPane() {
        style = "-fx-background-color:#3d3d3d"
        top = menuBar
        // has to be a number that can be devided by 4
        center = BoardPanel.newBoardPane(controller)
        //number of Cards can be set here
        bottom = if (JokerState.state.equals(JokerState.unpacked))
          CardMaster.CardPaneBuilder(controller)
            .withCardList(CardDeck.CardDeckBuilder().withAmount(List(1, 1)).buildCardList)
            .withCards()
            .withIcons()
            .buildCardPane()
        else
          CardMaster.CardPaneBuilder(controller)
            .withCardList(controller.gameState.actualPlayer.cardList)
            .withCards()
            .withIcons()
            .buildCardPane()
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

