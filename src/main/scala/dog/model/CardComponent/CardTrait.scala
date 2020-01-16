package dog.model.CardComponent
import dog.model.CardComponent.cardBaseImpl.Card

trait CardTrait {
  def symbol: String

  def task: String

  def color: String

  def parseToList: List[String]

  def parse(select: Int): Card
}
