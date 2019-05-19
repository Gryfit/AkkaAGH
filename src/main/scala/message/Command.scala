package message

import akka.actor.ActorRef

//object Operation extends Enumeration{
//  type Operation = Value
//  val Order, Search, Stream = Value
//}


sealed trait Command

case class Order(title: String, ref: ActorRef) extends Command
case class Search(title: String, ref: ActorRef) extends Command
case class StreamReq(title: String, ref: ActorRef) extends Command


