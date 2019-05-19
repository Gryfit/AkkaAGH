package server

import java.io.File

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory

object ServerMain extends App{
  val config = new File("conf/server.conf")

  val system = ActorSystem("ServerSystem", ConfigFactory.parseFile(config))

  system.actorOf(Props[SearchActor], name = "SearchActor")

  val dbSave = system.actorOf(Props[DatabaseSave], name = "SaveActor")
  system.actorOf(Props(new OrderActor(dbSave)), name = "OrderActor")

  system.actorOf(Props[StreamActor], name= "StreamActor")
}
