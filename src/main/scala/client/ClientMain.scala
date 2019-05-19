package client

import java.io.File

import akka.actor.{ActorSystem, Props}

import scala.io.StdIn._
import cats.implicits._
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import message.{Command, Order, Search, StreamReq}

object ClientMain extends LazyLogging{
  val config = new File("conf/client.conf")
  val system = ActorSystem("ClientSystem", ConfigFactory.parseFile(config))
  val client = system.actorOf(Props[Client], name = "client")

  def main(args: Array[String]): Unit = {
    println("commands: search [title], order [title], stream [title]")
    loop()
  }

  def parse(input: String): Either[String, Command] = {
    input.split(" ") match {
      case Array("search", title) => Right(Search(title, client))
      case Array("order", title) => Right(Order(title, client))
      case Array("stream", title) => Right(StreamReq(title, client))
      case _ => Left("Wrong message")
    }
  }

  def loop(): Unit ={
    parse(readLine("> "))
      .leftMap(logger.error(_))
      .map(client ! _)
    loop()
  }
}
