package aview.gui


import javafx.scene.layout.GridPane
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.image.ImageView
import scalafx.scene.layout.{BorderPane, VBox}

import scala.swing.Orientation

object GUI extends JFXApp {
  stage = new PrimaryStage {
    title = "MenuBar Test"
    val vBox = new VBox()
    scene = new Scene(300, 300) {

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

      val rootPane: BorderPane = new BorderPane {
        top = menuBar
        center = makeDrawingTab()
      }

      root = rootPane
    }
  }

  private def makeDrawingTab(): BorderPane = {

    val cardViewAndPlayView: SplitPane = new SplitPane {
      Orientation.Horizontal

      val view: GridPane = new GridPane {
        style = "-fx-background-color:#383838"
        val view1, view2, view3 = new ImageView("file:AssCard.jpg") {
          fitHeight = 150
          fitWidth = 75
        }
        add(view1, 0, 0)
        add(view2, 1, 0)
        add(view3, 2, 0)
      }
      items.add(view)
      items.add(new TextArea("ss"))
    }

    val boardView = new SplitPane {

      style = "-fx-background-color:#383838"
      Orientation.Horizontal

      dividerPositions = 0.7
    }

    val tab = new BorderPane {
      center = boardView
      bottom = cardViewAndPlayView
    }
    tab
  }
}


//val button = new Button("Click Me!")
//button.layoutX = 100
//button.layoutY = 100
//val rect = Rectangle(400,200,100,150)
//rect.fill = Color.Azure
//
//var enemies = List(Circle(10, 10, 10))
//val player = Circle(300, 300, 20)
//player.fill = Color.Green
//content = List(enemies.head, player,button,rect)
//
//
//
//var lastTime = 0L
//val enemySpeed = 20
//val playerSpeed = 25
//
//
//val timer = AnimationTimer(t => {
//if (lastTime > 0) {
//val delta = (t - lastTime) / 1e9
//for (e <- enemies) {
//val dx = player.centerX.value - e.centerX.value
//val dy = player.centerY.value - e.centerY.value
//val dist = math.sqrt(dx * dx + dy * dy)
//e.centerX = e.centerX.value + dx / dist * enemySpeed * delta
//
//e.centerY = e.centerY.value + dy / dist * enemySpeed * delta
//}
//}
//lastTime = t
//})
//timer.start