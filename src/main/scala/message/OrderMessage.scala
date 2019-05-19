package message

import akka.actor.ActorRef

sealed trait OrderMessage
case class OrderPlaced(title: String, ref: ActorRef)
