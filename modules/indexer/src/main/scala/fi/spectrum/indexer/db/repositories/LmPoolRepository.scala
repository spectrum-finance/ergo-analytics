package fi.spectrum.indexer.db.repositories

import fi.spectrum.core.domain.BoxId
import fi.spectrum.indexer.db.classes.DeleteRepository
import fi.spectrum.indexer.db.models.LmPoolDB

class LmPoolRepository extends Repository[LmPoolDB, BoxId] with DeleteRepository[LmPoolDB, BoxId] {

  val field: String = "pool_state_id"

  val tableName: String = "lm_pools"

  val fields: List[String] =
    List(
      "pool_state_id",
      "pool_id",
      "reward_id",
      "reward_amount",
      "lq_id",
      "lq_amount",
      "v_lq_id",
      "v_lq_amount",
      "tmp_id",
      "tmp_amount",
      "epoch_length",
      "epochs_num",
      "program_start",
      "redeem_blocks_delta",
      "program_budget",
      "max_rounding_error",
      "execution_budget",
      "epoch_index",
      "timestamp",
      "version",
      "height",
      "protocol_version"
    )

}
