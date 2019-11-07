package util


trait Observer {
  def update: Unit
}

class Observable {
  var subscriber: Vector[Observer] = Vector()

  def add(s: Observer): Unit = subscriber = subscriber :+ s

  def remove(s: Observer): Unit = subscriber = subscriber.filterNot(o => o == s)

  def notifyObservers: Unit = subscriber.foreach(o => o.update)
}
