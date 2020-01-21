package dog.util

trait ConfigState {
  def changeState: ConfigState
}

object ConfigMode {
  var state: ConfigState = configDeactivated
  var value: Int = 44
  def handle: ConfigState = state.changeState


  def saveValue(setValue: Int): Int = {
    value = setValue
    value
  }
  //noinspection DuplicatedCode
  object configActivated extends ConfigState {
    override def changeState: ConfigState = {
      state = configDeactivated
      state
    }
  }


  object configDeactivated extends ConfigState {
    override def changeState: ConfigState = {
      state = configActivated
      state

    }
  }

}
