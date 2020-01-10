package dog.controller


trait Function1[-T1, +R] {
  def apply(v1: T1): R

  def andThen[A](g: R => A): T1 => A = { x => g(apply(x)) }
}

object test {

  val loggingFilter: String => String = (message: String) => {
    println(message)
    message
  }
  //such an expressive name :D
  val rejectRejectableThenLogThenFilterRespectableThenLogAgain: String => String =
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

  object Main {
    def main(args: Array[String]): Unit = {

      //Test it out!
      test.rejectRejectableThenLogThenFilterRespectableThenLogAgain("[MrRespectable007] curse all of you, I curse the world!!")
      test.rejectRejectableThenLogThenFilterRespectableThenLogAgain("[Suresh01] Not you again Ramesh! curse you!")
      test.rejectRejectableThenLogThenFilterRespectableThenLogAgain("[TheMostRejectableGuyInTown] Yo wazzup people.")
    }
  }

}