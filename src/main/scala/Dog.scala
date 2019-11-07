import model.Main.Player

object Dog {

  def main(args: Array[String]): Unit = {
    print("Your name is ")
    val player = Player(scala.io.StdIn.readLine())
    print(f"Welcome ${player.toString()}!\n")
  }

}
