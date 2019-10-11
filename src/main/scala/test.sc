case class Project(name: String, devs: Array[String]) {
  def printProject(): Unit = {
    println("Our project is named: " + name)
    printDev()
  }

  def printDev(): Unit = {
    print("Developped by ")
    printDArray()
  }

  def printDArray(): Unit = {
    for (d <- devs) {
      print(d + " ")
    }
  }

  println("Who let the dogs out")
  println("I did that")
  println("what did u ??")
}

val p = Project("dog", Array("Ismail", "Josef"))
p.printProject()