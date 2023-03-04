package fi.spectrum.indexer.db

package object repositories {
  implicit val swapsRepository: AmmSwapRepository            = new AmmSwapRepository
  implicit val redeemsRepository: AmmRedeemRepository        = new AmmRedeemRepository
  implicit val depositsRepository: AmmDepositRepository      = new AmmDepositRepository
  implicit val lmDepositsRepository: LmDepositRepository     = new LmDepositRepository
  implicit val lmCompoundRepository: LmCompoundRepository    = new LmCompoundRepository
  implicit val lmRedeemRepository: LmRedeemRepository        = new LmRedeemRepository
  implicit val locksRepository: LockRepository               = new LockRepository
  implicit val offChainsFeeRepository: OffChainFeeRepository = new OffChainFeeRepository
  implicit val poolsRepository: PoolRepository               = new PoolRepository
  implicit val blocksRepository: BlockRepository             = new BlockRepository
  implicit val assetInsert: AssetInsert                      = new AssetInsert
  implicit val lmPoolsRepo: LmPoolRepository                 = new LmPoolRepository
}
