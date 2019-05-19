package server

import akka.actor.SupervisorStrategy.Restart
import akka.actor.{Actor, ActorRef, OneForOneStrategy, SupervisorStrategy}
import com.typesafe.scalalogging.LazyLogging
import message.{Order, OrderPlaced}
import scala.concurrent.duration._

class OrderActor(saveRef: ActorRef) extends Actor with LazyLogging{

  override def receive: Receive = {
    case o : Order =>
      logger.info("Placing order for "+ o.title)
      saveRef ! o
    case op: OrderPlaced =>
      logger.info("Order placed for "+ op.title)
      op.ref ! op
  }


  override def supervisorStrategy: SupervisorStrategy = {
    OneForOneStrategy(maxNrOfRetries = 3, withinTimeRange = 1 minute) {
      case _ => Restart
    }
  }
}
