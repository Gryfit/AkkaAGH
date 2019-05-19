package server

import akka.actor.SupervisorStrategy.Restart
import akka.actor.{Actor, OneForOneStrategy, Props, SupervisorStrategy}
import com.typesafe.scalalogging.LazyLogging
import message.{Found, NotFound, Search}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.duration._
import cats.implicits._

import scala.util.Success

class SearchActor extends Actor with LazyLogging{

  implicit val timeout = Timeout(5 seconds)
  implicit val ec = context.system.dispatcher

  val finder1 = context.actorOf(Props(new DatabaseFinder("resources/db1.txt")))
  val finder2 = context.actorOf(Props(new DatabaseFinder("resources/db2.txt")))

  override def receive: Receive = {
    case search: Search => {
      logger.info("Searching for " + search.title)
      (ask(finder1, search.title), ask(finder2, search.title))
        .bisequence
        .map{
          case (Found(title,price), _) => Found(title, price)
          case (_, Found(title, price)) => Found(title, price)
          case _ => NotFound(search.title)
        }
        .onComplete{
          case Success(value) =>
            logger.info(value.toString)
            search.ref ! value
          case _ =>
            logger.error("Failure!")
            search.ref ! NotFound(search.title)
        }
    }
  }

  override def supervisorStrategy: SupervisorStrategy = {
    OneForOneStrategy(maxNrOfRetries = 5, withinTimeRange = 1 minute) {
      case _: Exception => Restart
    }
  }

}
