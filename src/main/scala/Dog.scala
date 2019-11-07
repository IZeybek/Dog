import model.BoardTrait
import model.Main.Player
import util.Read

object Dog {

  def main(args: Array[String]): Unit = {
    print("Your name is ")
    val player = Player(scala.io.StdIn.readLine())
    print(f"Welcome ${Console.UNDERLINED}${player.toString()}${Console.RESET}!\n")
    val r = Read()
    val b: BoardTrait = r.readIn("src/feld.txt")
    r.prettyPrint(b)
  }

}
