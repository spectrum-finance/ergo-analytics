package fi.spectrum.core.domain.order

import cats.Show
import enumeratum.{CirceEnum, Enum, EnumEntry}
import tofu.logging.Loggable

/** Order has two stages:
  *  1. Registration - order request appears in a network.
  *  2. Execution - off-chain operator takes order request and executes it
  *
  * Because of it following statuses exist:
  *  1. WaitingRegistration - the order request is in mempool but has yet to be executed.
  *  2. Registered - node executed order request.
  *  3. WaitingEvaluation - off-chain operator executed order.
  *  4. Evaluated - order executed by a node.
  *  5. WaitingRefund - refund order is in network.
  *  6. Refunded - a user refunded the order.
  */
sealed abstract class OrderStatus extends EnumEntry

object OrderStatus extends Enum[OrderStatus] with CirceEnum[OrderStatus] {

  case object WaitingRegistration extends OrderStatus

  case object Registered extends OrderStatus

  case object WaitingEvaluation extends OrderStatus

  case object Evaluated extends OrderStatus

  case object WaitingRefund extends OrderStatus

  case object Refunded extends OrderStatus

  val values = findValues

  implicit val show: Show[OrderStatus] = _.entryName

  implicit val loggable: Loggable[OrderStatus] = Loggable.show

  def mapToMempool(order: OrderStatus): OrderStatus =
    order match {
      case OrderStatus.Registered => OrderStatus.WaitingRegistration
      case OrderStatus.Evaluated  => OrderStatus.WaitingEvaluation
      case OrderStatus.Refunded   => OrderStatus.WaitingRefund
      case s                      => s
    }

}
