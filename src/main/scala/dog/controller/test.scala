package dog.controller

object chain {
  val loggingFilter: String => String = (message: String) => {
    println(message)
    message
  }
  //such an expressive name :D
  val processMsg: String => String =
    rejectionFilter("TheMostRejectableGuyInTown") andThen
      loggingFilter andThen
      profanityFilter("MrRespectable007", "curse") andThen
      loggingFilter

  def profanityFilter(selectedUser: String, profaneWord: String): String => String = (message: String) => {
    if (message.startsWith(s"[$selectedUser]")) message.replaceAll(profaneWord, "love") else message
  }

  def rejectionFilter(rejectedUser: String): String => String = (message: String) => {
    if (!message.startsWith(s"[$rejectedUser]")) message else ""
  }

  //we could make similar compositions of the functions
}
object test {
  //Test it out!
  def process() = {
    chain.processMsg("[MrRespectable007] curse all of you, I curse the world!!")
    chain.processMsg("[Suresh01] Not you again Ramesh! curse you!")
    chain.processMsg("[TheMostRejectableGuyInTown] Yo wazzup people.")
  }

}

object Main {
  def main(args: Array[String]): Unit = {
    test.process()
  }
}
