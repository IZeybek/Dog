import model.Player

object Dog {

  def main(args: Array[String]): Unit = {
    val player = Player(scala.io.StdIn.readLine())
    print(player.toString())
  }

}
