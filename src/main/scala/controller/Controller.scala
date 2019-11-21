package controller

import model._
import util.Observable

import scala.util.Random

class Controller() extends Observable {

  var board: Board = createBoard
  var player: Array[Player] = createPlayer(Array("p1", "p2", "p3", "p4"))
  var card: Array[CardTrait] = createRandomCards()
  var cardIndex: Integer = 0

  //Board

  def setNewBoard: Board = {
    board = createBoard
    notifyObservers
    board
  }

  def createBoard: Board = {

    var boardMap = Map(0 -> Cell(0, false, -1))

    for {
      i <- 0 until 15
    } boardMap += (i -> Cell(i, false, -1))

    notifyObservers
    Board(boardMap)
  }


  def toStringBoard(): String = {
    val board: String = getBoard.toString()
    val up = "‾" * player.size * 3
    val down = "_" * player.size * 3
    var house = ""
    for (i <- 0 until player.size) {
      house = house + s" ${
        i match {
          case 0 => Console.YELLOW;
          case 1 => Console.BLUE;
          case 2 => Console.GREEN;
          case 3 => Console.RED
        }
      }${player(i).inHouse}${Console.RESET} "
    }
    down + "\n" + house + "\n" + up + "\n" + board
  }

  def getBoard: Board = board

  //Player

  def setPlayer(name: Array[String]): Array[Player] = {
    player = createPlayer(name)
    notifyObservers
    player
  }

  def createPlayer(name: Array[String]): Array[Player] = {
    val player = new Array[Player](4)
    val map1, map2, map3, map4 = Map(0 -> Piece(0), 1 -> Piece(0), 2 -> Piece(0), 3 -> Piece(0), 4 -> Piece(0))

    player(0) = Player(name(0), "gelb", map1, 4)
    player(1) = Player(name(1), "blau", map2, 4)
    player(2) = Player(name(2), "grün", map3, 4)
    player(3) = Player(name(3), "rot", map4, 4)

    notifyObservers
    player
  }

  def movePlayer(playerNum: Integer, pieceNum: Integer, moveBy: Integer): Boolean = {
    val oldPos = player(playerNum).piece(pieceNum).position
    //move piece of specific player by returning a copy of the piece to the copy constructor player and returning a copy of the player
    if (!board.boardMap(moveBy + player(playerNum).piece(pieceNum).position).isFilled) {
      if (oldPos == 0)
        player(playerNum) = player(playerNum).copy(inHouse = player(playerNum).inHouse - 1)
      player(playerNum) = player(playerNum).copy(piece = player(playerNum).piece.updated(pieceNum, player(playerNum).piece(pieceNum).copy(player(playerNum).piece(pieceNum).position + moveBy)))
      board = board.copy(boardMap = board.boardMap.updated(oldPos + moveBy, board.boardMap(oldPos).copy(filled = true, player = playerNum))) //set new position
      board = board.copy(boardMap = board.boardMap.updated(oldPos, board.boardMap(oldPos).copy(filled = false, player = -1))) //update old cell to filled=false
      notifyObservers
      //oldPos + moveBy
      true
    } else {
      notifyObservers
      false
    }
  }

  //Cards

  def createRandomCards(): Array[CardTrait] = {
    val c = List.newBuilder[CardTrait]
    for (i <- 0 until 20) yield i match {
      case i if 0 until 10 contains i =>
        c += SevenCard()
      case i if 10 until 20 contains i =>
        c += ChangeCard()
    }
    val cards = c.result()
    Random.shuffle(cards)
    cardIndex = 0
    cards.toArray
  }

  def drawCard(): CardTrait = {
    if (cardIndex == 20)
      createRandomCards()
    val c = card(cardIndex)
    cardIndex += 1
    c
  }
}
