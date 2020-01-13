package dog

import com.google.inject.AbstractModule
import com.google.inject.name.Names
import dog.controller.Component.controllerBaseImpl.Controller
import dog.controller.ControllerTrait
import dog.model.BoardComponent.BoardTrait
import dog.model.BoardComponent.boardAdvancedImpl.Board
import dog.model.FileIOComponent._
import net.codingwell.scalaguice.ScalaModule


class DogModule extends AbstractModule with ScalaModule {

  val defaultSize: Int = 96

  def configure(): Unit = {
    bindConstant().annotatedWith(Names.named("DefaultSize")).to(defaultSize)
    bind[BoardTrait].to[Board]
    bind[ControllerTrait].to[Controller]
    bind[BoardTrait].annotatedWithName(name = "nano").toInstance(new Board(4))
    bind[BoardTrait].annotatedWithName(name = "micro").toInstance(new Board(8))
    bind[BoardTrait].annotatedWithName(name = "small").toInstance(new Board(20))
    bind[BoardTrait].annotatedWithName(name = "normal").toInstance(new Board(64))
    bind[BoardTrait].annotatedWithName(name = "big").toInstance(new Board(80))
    bind[BoardTrait].annotatedWithName(name = "extra big").toInstance(new Board(96))
    bind[BoardTrait].annotatedWithName(name = "ultra big").toInstance(new Board(128))

    //    bind[FileIOTrait].to[fileIOXmlImpl.FileIO]
    bind[FileIOTrait].to[fileIOJsonImpl.FileIO]
  }

}
