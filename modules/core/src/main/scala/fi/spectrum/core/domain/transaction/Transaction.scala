package fi.spectrum.core.domain.transaction

import cats.data.NonEmptyList
import fi.spectrum.core.domain.TxId

final case class Transaction(id: TxId, inputs: NonEmptyList[Input], outputs: NonEmptyList[Output])

object Transaction
