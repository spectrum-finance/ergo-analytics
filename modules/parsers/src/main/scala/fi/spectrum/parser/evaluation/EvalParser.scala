package fi.spectrum.parser.evaluation

import fi.spectrum.core.domain.transaction.Transaction
import fi.spectrum.parser.OrderParser

class EvalParser(orderParser: OrderParser) {

  def parse(tx: Transaction) = {
    def executed = tx.inputs.map(in => orderParser.parse(in.output))
      .collectFirst { case Some(order) => order }

    def registered = tx.outputs.map(out => orderParser.parse(out))
      .collectFirst { case Some(order) => order }
  }

}
