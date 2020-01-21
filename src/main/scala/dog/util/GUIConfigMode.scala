package dog.util

trait ConfigState {
  def changeState: ConfigState
}

object ConfigMode {
  var state: ConfigState = configDeactivated

  def handle: ConfigState = state.changeState


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
