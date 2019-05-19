package server

import akka.actor.SupervisorStrategy.Restart
import akka.actor.{Actor, OneForOneStrategy, Props, SupervisorStrategy}
import com.typesafe.scalalogging.LazyLogging
import message.StreamReq

import scala.concurrent.duration._

class StreamActor extends Actor with LazyLogging{

  override def receive: Receive = {
    case s: StreamReq =>
      logger.info("Stream for: " + s.title)
      context.actorOf(Props[StreamHandler]) ! s
  }

  override def supervisorStrategy: SupervisorStrategy = {
    OneForOneStrategy(maxNrOfRetries = 3, withinTimeRange = 1 minute) {
      case _: Exception => Restart
    }
  }
}
