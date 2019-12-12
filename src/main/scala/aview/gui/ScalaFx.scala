package aview.gui


import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.layout.BorderPane

import scala.swing.Orientation

object MenuBarTest extends JFXApp {
  stage = new PrimaryStage {
    title = "MenuBar Test"
    scene = new Scene {

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
      //        val tabPane = new BorderPane(){
      //            cente
      //        }
      //        tabPane += makeDrawingTab()

        val rootPane: BorderPane = new BorderPane{
          top = menuBar
          center = makeDrawingTab()
        }
      root = rootPane
//        center = tabPane
//        //        center = new ImageView("file:groot.jpg") {
//        //          fitHeight = 1080 - 300
//        //          fitWidth = 1920
//        //        }
//        //
//        bottom = new Button("asdasd")



    }
  }

  private def makeDrawingTab(): BorderPane = {

    val leftSplit = new SplitPane {
      Orientation.Horizontal
      items.add(new TextArea("first"))
      items.add(new TextArea("second"))
    }

    //    val topRightBorder = new BorderPane {
    //      top = new Slider(0, 1000, 0)
    //      center = new Canvas
    //    }
    //
    //    val bottomRightBorder = new BorderPane {
    //      top = new TextArea("hallo")
    //      center = new TextArea("ssdsad") {
    //        editable = false
    //      }
    //    }

    val rightSplit = new SplitPane {

      Orientation.Horizontal
      items.add(new TextArea("left"))
      items.add(new TextArea("right"))
      dividerPositions = 0.7
    }

    val topSplit = new SplitPane {
      items.add(leftSplit)
      items.add(rightSplit)
      dividerPositions = 0.3
    }

    val tab = new BorderPane {
      center = rightSplit
      bottom = leftSplit
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