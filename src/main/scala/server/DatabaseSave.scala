package server

import akka.actor.SupervisorStrategy.Restart
import akka.actor.{Actor, OneForOneStrategy, SupervisorStrategy}
import com.typesafe.scalalogging.LazyLogging
import message.{Order, OrderPlaced}
import scala.concurrent.duration._

import scala.reflect.io.File

class DatabaseSave extends Actor with LazyLogging{
  val orderDb = File("resources/orders.txt")

  override def receive: Receive = {
    case o: Order =>
      orderDb.lines().find(_.startsWith(o.title)).fold(
        orderDb.writeAll(o.title + "\n")
      )(v => logger.warn("Order for "+ v + " already exists"))
      sender ! OrderPlaced(o.title, o.ref)
  }
  override def supervisorStrategy: SupervisorStrategy = {
    OneForOneStrategy(maxNrOfRetries = 3, withinTimeRange = 1 minute) {
      case _ => Restart
    }
  }
}
