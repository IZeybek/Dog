package aview.gui


import controller.{Controller, GameStatus}
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.effect.DropShadow
import scalafx.scene.layout.{BorderPane, HBox}
import scalafx.scene.paint.Color.{DodgerBlue, PaleGreen, SeaGreen}
import scalafx.scene.paint.{Color, LinearGradient, Stops}
import scalafx.scene.text.Text

class Gui(controller: Controller) {
  PrimaryStage.setScene(GameStatus.message(controller.gameState.gameState))
  val actualStage: PrimaryStage.type = PrimaryStage
}

object PrimaryStage extends JFXApp {
  var setupScene: Scene = Scenes.getScene("welcome")

  def setScene(setScene: String): Unit = {
    setupScene = Scenes.getScene(setScene)
  }

  stage = new PrimaryStage {
    title = "Dog"
    scene = setupScene
  }
}

object Scenes {
  def getScene(sceneName: String): Scene = {
    sceneName.toLowerCase match {
      case "welcome" => new WelcomeScene()
      case "main" => new MainScene()
      case _ => null
    }
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

class WelcomeScene() extends Scene {
  content = new HBox {
    fill = Color.Black
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
