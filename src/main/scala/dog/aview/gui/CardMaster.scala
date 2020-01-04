package dog.aview.gui

import dog.aview.gui.CardPanel.{newCards, newIcons}
import dog.controller.ControllerTrait
import dog.model.CardComponent.CardTrait
import javafx.scene.layout.GridPane
import scalafx.geometry.Insets
import scalafx.geometry.Pos.Center
import scalafx.scene.control.{Button, ScrollPane}
import scalafx.scene.layout.StackPane

trait CardMaster {

  val stdPath = "file:src/main/scala/resources/"
  val bgColor: String = "-fx-background-color:#383838;"
  //  val newCardPane : ScrollPane
  var icons: Seq[Button] = Nil
  var cards: Seq[Button] = Nil
  var iconGrids: Seq[GridPane] = Nil
  var cardGrids: GridPane = new GridPane

  case class CardPaneBuilder(controller: ControllerTrait) {

    val actualPlayer: Int = controller.gameState.players._2
    val cardList: List[CardTrait] = controller.gameState.players._1(actualPlayer).cardList
    val amount: Int = cardList.size

    def withIcons(): CardPaneBuilder = {
      var idx = 0

      iconGrids = Seq.fill(amount)(new GridPane {
        setPadding(Insets(0, 5, 5, 55))
        val card: CardTrait = cardList(idx)
        val task: Array[String] = card.task.split("\\s+")
        val iconAmount: Int = task.length

        if (iconAmount == 2) setPadding(Insets(0, 5, 5, 24))
        else if (iconAmount == 3) setPadding(Insets(0, 5, 5, 43))
        val cardOptionAmount: Int = card.task.split("\\s+").length
        val icon: Seq[Button] = newIcons(controller, iconAmount, card, idx)
        if (iconAmount == 3) icon.indices.foreach(i => add(icon(i), 0, i))
        else icon.indices.foreach(i => add(icon(i), i, 0))
        idx = idx + 1
      })

      this
    }

    def withCards(): CardPaneBuilder = {

      cardGrids = new GridPane {

        setStyle(bgColor + "-fx-padding:2")
        var offset = 0
        if (amount < 9) offset = 250
        setPadding(Insets(0, 0, 0, offset))
        setAlignment(Center)
        cards = newCards(iconGrids, amount, cardList)
      }
      this
    }

    def buildCardPane(): ScrollPane = {

      //each Card has 3 Button Options. Its amount is not dynamic yet ------------------------------------------------------------------------
      val stackPane: Seq[StackPane] = Seq.fill(amount)(new StackPane())
      stackPane.indices.foreach(i => stackPane(i).getChildren.addAll(cards(i), iconGrids(i)))
      stackPane.indices.foreach(i => cardGrids.add(stackPane(i), i, 0))
      new ScrollPane() {

        prefHeight = 229
        fitToWidth = true
        fitToHeight = true
        maxHeight = 229
        minHeight = 150
        this.style = bgColor
        content() = cardGrids
      }
    }
  }

}