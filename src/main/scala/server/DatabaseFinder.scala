package server

import akka.actor.{Actor, OneForOneStrategy, SupervisorStrategy}
import akka.actor.SupervisorStrategy.Restart
import com.typesafe.scalalogging.LazyLogging
import message.{Found, NotFound}

import scala.concurrent.duration._
import scala.reflect.io.File

class DatabaseFinder(path: String) extends Actor with LazyLogging{
  val database = File(path).lines()

  override def receive: Receive = {
    case title: String => {
      logger.info("Search database " + path + " for " + title)
      database
        .find(_.startsWith(title))
        .map(parseRow)
        .foreach{
          case Some(price) => sender ! Found(title, price)
          case None => sender ! NotFound(title)
        }
    }
  }

  override def supervisorStrategy: SupervisorStrategy = {
    OneForOneStrategy(maxNrOfRetries = 3, withinTimeRange = 1 minute) {
      case _ => Restart
    }
  }

  private def parseRow(row: String): Option[Double] ={
    row.split(" ").lift(1).map(_.toDouble)
  }


}
