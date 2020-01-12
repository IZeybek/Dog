package dog

import com.google.inject.{Guice, Injector}
import dog.aview.Tui
import dog.aview.gui.Gui
import dog.controller.{BoardChanged, ControllerTrait}

object Dog {
  val injector: Injector = Guice.createInjector(new DogModule)
  val controller: ControllerTrait = injector.getInstance(classOf[ControllerTrait])
  val tui = new Tui(controller)
  val gui = new Gui(controller)

  controller.publish(new BoardChanged)

  def main(args: Array[String]): Unit = {
    //    gui.main(Array(""))

    var input: String = ""
    print(f"Welcome ${Console.UNDERLINED}${System.getProperty("user.name")}${Console.RESET}! \n")
    do {
      print("\n>> ")
      input = scala.io.StdIn.readLine()
      println(tui.processInput(input))
    } while (input != "exit")
  }
}
