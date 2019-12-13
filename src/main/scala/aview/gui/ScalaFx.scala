package aview.gui


import controller.Controller
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.layout.BorderPane

class Gui(controller: Controller) {
  val actualStage: JFXApp = Stages.getStage(0)
}

object Stages {
  def getStage(stageNum: Int): JFXApp = {
    stageNum match {
      case 0 => PrimaryStage
      case _ => null
    }
  }
}

object Scenes {
  def getScene(sceneName: String): Scene = {
    sceneName.toLowerCase match {
      case "main" => new MainScene()
      case _ => null
    }
  }
}

object PrimaryStage extends JFXApp {

  stage = new PrimaryStage {
    title = "Dog"
    scene = Scenes.getScene("main")
  }
}

class MainScene() extends Scene {
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
    center = BoardPanel.newBoardView()
    bottom = CardPanel.newCardViews()
  }
  root = rootPane
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
