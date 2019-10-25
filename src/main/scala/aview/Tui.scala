package aview

case class Tui() {

  def print(input: String) {
    println(f"I'm impressed! u can write! Let me guess...it was: $input")
  }


  def input(input: String): Unit = {
    val commands = input.split("\\s+")
    var result = ""
    commands(0) match {
      case "create" => result = "empty board created"
      case "set" => result = "Player set"
      case "p" => result = "board printed"
      case "s" => result = "player cell can not be found since its not created yet"
      case _ => System.out.println("wrong command! try again")
    }
    if (result != "") println(f"ur commands result is: $result")
  }
}


