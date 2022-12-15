package fi.spectrum.indexer.db.v2

trait Repository[T, I] extends Delete[T, I] with Update[T] with Insert[T]

object Repository {
  implicit val swapsRepository: SwapRepository              = new SwapRepository
  implicit val redeemsRepository: RedeemRepository          = new RedeemRepository
  implicit val depositsRepository: DepositRepository        = new DepositRepository
  implicit val locksRepository: LockRepository              = new LockRepository
  implicit val offChainFeeRepository: OffChainFeeRepository = new OffChainFeeRepository
}
