package aview

case class Tui() {

  def println(input: String) {
    System.out.println(f"I'm impressed! u can write! Let me guess...it was: $input")
  }


  def input(input: String): Unit = {
    val commands = input.split("\\s+")
    var result = ""
    commands(0) match {
      case "create" => result = "empty board created"
      case "set" => result = "Player set"
      case "p" => result = "board printed"
      case "s" => result = "player cell can not be found since its not created yet"
      case _ => println(input)
    }
    if (result != "") println(f"ur commands result is: $result")
  }
}


