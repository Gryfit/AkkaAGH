package client

import akka.actor.SupervisorStrategy.Restart
import akka.actor.{Actor, OneForOneStrategy, SupervisorStrategy}
import com.typesafe.scalalogging.LazyLogging
import message.{Found, NotFound, Order, OrderPlaced, Search, StreamReq}

import scala.concurrent.duration._

class Client extends Actor with LazyLogging {
    override def receive: Receive = {
      case search: Search =>
        logger.info("propagate command " + search + " to server")
        val searchActor = context.actorSelection("akka.tcp://ServerSystem@127.0.0.1:2552/user/SearchActor")
        searchActor ! search
      case order: Order =>
        logger.info("propagate command " + order + " to server")
        val orderActor = context.actorSelection("akka.tcp://ServerSystem@127.0.0.1:2552/user/OrderActor")
        orderActor ! order
      case stream: StreamReq =>
        logger.info("propagate command " + stream + " to server")
        val streamActor = context.actorSelection("akka.tcp://ServerSystem@127.0.0.1:2552/user/StreamActor")
        streamActor ! stream
      case f: Found => logger.info(f.toString)
      case nf: NotFound => logger.info(nf.toString)
      case ord: OrderPlaced => logger.info(ord.toString)
      case s: String => println(s)
    }

  override def supervisorStrategy: SupervisorStrategy = {
    OneForOneStrategy(maxNrOfRetries = 3, withinTimeRange = 1 minute) {
      case _ => Restart
    }
  }
}
