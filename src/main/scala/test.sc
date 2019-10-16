/*
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
      print(d + " & ")
    }
  }
}

println("hallihallo")

val p = Project("dog", Array("Ismail", "Josef"))
p.printProject()
*/

//Testing a project

class Board() {

}

case class BoardUtility() {
  def jump(): Unit = {
    val r = scala.util.Random
    printf("jumping %d fields", r.nextInt(5) + 1)
  }

  def printArray(a: Array[Array[String]]): Unit = {
    for (i <- a) {
      for (cell <- i) {
        print(cell)
        print(" ")
      }
      println()
    }
  }

  def fillArray(a: Array[Array[String]]): Unit = {
    for (i <- a.indices; j <- a(i).indices)
      a(i)(j) = "o"
  }
}

val field = Array.ofDim[String](3, 3)
val p = BoardUtility()
p.fillArray(field)
p.printArray(field)
p.jump()
