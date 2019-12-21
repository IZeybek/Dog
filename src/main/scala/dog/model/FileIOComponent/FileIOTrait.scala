package dog.model.FileIOComponent

import dog.controller.GameState

trait FileIOTrait {
  def load: GameState

  def save(gameState: GameState): Unit
}
