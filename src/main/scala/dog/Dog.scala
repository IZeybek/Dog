package dog

import com.google.inject.{Guice, Injector}
import dog.aview.Tui
import dog.controller.ControllerTrait
import dog.gui.Gui

object Dog {
  val injector: Injector = Guice.createInjector(new DogModule)
  val controller: ControllerTrait = injector.getInstance(classOf[ControllerTrait])
  val gui = new Gui(controller)
  val tui = new Tui(controller)


  def main(args: Array[String]): Unit = {
    tui.showMenu()
    gui.main(args)

    var input: String = ""
    print(f"Welcome ${Console.UNDERLINED}${System.getProperty("user.name")}${Console.RESET}! \n")
    do {
      print("\n>> ")
      input = scala.io.StdIn.readLine()
      println(tui.processInput(input))
    } while (input != "exit")
  }
}
