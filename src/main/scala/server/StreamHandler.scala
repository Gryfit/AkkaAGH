package server

import akka.actor.SupervisorStrategy.Restart
import akka.actor.{Actor, OneForOneStrategy, PoisonPill, SupervisorStrategy}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}
import com.typesafe.scalalogging.LazyLogging
import message.StreamReq

import scala.concurrent.duration._
import scala.reflect.io.File

class StreamHandler extends Actor with LazyLogging{
  implicit val mat: ActorMaterializer = ActorMaterializer()

  override def receive: Receive = {
    case s: StreamReq =>
      val file = File("resources/" + s.title)
      Source.fromIterator(() => file.lines())
        .throttle(1, 1 seconds)
        .via(Flow[String].alsoTo(Sink.onComplete(_ => self ! PoisonPill)))
        .runWith(Sink.actorRef(s.ref, "EOF"))
  }
  override def supervisorStrategy: SupervisorStrategy = {
    OneForOneStrategy(maxNrOfRetries = 3, withinTimeRange = 1 minute) {
      case _: Exception => Restart
    }
  }
}
