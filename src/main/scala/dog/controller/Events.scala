package dog.controller

import scala.swing.event.Event

class CardChanged extends Event

case class BoardChanged() extends Event

class CandidatesChanged extends Event
