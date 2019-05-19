package message

sealed trait SearchMessage
case class Found(title: String, price: Double)
case class NotFound(title: String)
