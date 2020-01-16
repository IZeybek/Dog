package dog.model.FileIOComponent

import dog.controller.StateComponent.GameState

trait FileIOTrait {
  def load: GameState

  def save(gameState: GameState): Unit
}
