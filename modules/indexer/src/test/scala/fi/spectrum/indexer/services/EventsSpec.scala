package fi.spectrum.indexer.services

import cats.data.NonEmptyList
import cats.effect.IO
import cats.effect.unsafe.implicits.global
import fi.spectrum.core.domain.TokenId
import fi.spectrum.indexer.db.Indexer
import fi.spectrum.indexer.db.local.storage.OrdersStorage
import fi.spectrum.indexer.mocks.{MempoolMock, MetricsMock, RedisCacheMock}
import fi.spectrum.indexer.models.BlockChainEvent
import fi.spectrum.parser.{CatsPlatform, PoolParser}
import fi.spectrum.parser.evaluation.ProcessedOrderParser
import fi.spectrum.parser.evaluation.Transactions._
import fi.spectrum.streaming.domain.TransactionEvent._
import io.github.oskin1.rocksdb.scodec.TxRocksDB
import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class EventsSpec extends AnyFlatSpec with Matchers with Indexer with BeforeAndAfterEach {

  implicit val parser  = ProcessedOrderParser.make[IO](TokenId.unsafeFromString(""))
  implicit val metrics = MetricsMock.make[IO]
  implicit val mempool = MempoolMock.make[IO]

  "Events service" should "keep valid order state after rollback" in {
    implicit val (rocks, release) = TxRocksDB.make[IO, IO]("rocks").allocated.unsafeRunSync()
    implicit val storage          = OrdersStorage.make[IO]
    val events                    = Events.make[IO]
    val deployPool                = TransactionApply(swapPoolTx, 0, 10)
    val swap1                     = TransactionApply(swapRegisterTransaction, 0, 10)
    val swapRollback1             = TransactionUnapply(swapRegisterTransaction, 0, 10)
    val swap2                     = TransactionApply(swapEvaluateTransaction, 0, 10)
    val swapRollback2             = TransactionUnapply(swapEvaluateTransaction, 0, 10)
    val swap3                     = TransactionApply(swapEvaluateTransaction, 0, 10)

    def run = for {
      stageDeploy            <- events.process(NonEmptyList.one(deployPool))
      resultDeploy           <- storage.getPool(List(swapPool.get.box.boxId))
      stageRegister          <- events.process(NonEmptyList.one(swap1))
      resultRegister         <- storage.getOrder(List(stageRegister._1.head.event.order.box.boxId))
      stageRegisterRollback  <- events.process(NonEmptyList.one(swapRollback1))
      resultRegisterRollback <- storage.getOrder(List(stageRegisterRollback._1.head.event.order.box.boxId))
      stageRegister2         <- events.process(NonEmptyList.one(swap1))
      resultRegister2        <- storage.getOrder(List(stageRegister2._1.head.event.order.box.boxId))
      stageEvaluate          <- events.process(NonEmptyList.one(swap2))
      resultEvaluate         <- storage.getOrder(List(stageEvaluate._1.head.event.order.box.boxId))
      stageEvaluateRollback  <- events.process(NonEmptyList.one(swapRollback2))
      resultEvaluateRollback <- storage.getOrder(List(stageEvaluateRollback._1.head.event.order.box.boxId))
      stageEvaluate2         <- events.process(NonEmptyList.one(swap3))
      resultEvaluate2        <- storage.getOrder(List(stageEvaluate2._1.head.event.order.box.boxId))
    } yield {
      stageDeploy._2.head.event shouldEqual swapPool.get
      resultDeploy.get shouldEqual swapPool.get

      stageRegister._1.length shouldEqual 1
      resultRegister.get shouldEqual stageRegister._1.head.event

      stageRegisterRollback._1.length shouldEqual 1
      resultRegisterRollback shouldEqual None

      stageRegister2._1.length shouldEqual 1
      resultRegister2.get shouldEqual stageRegister2._1.head.event

      stageEvaluate._1.length shouldEqual 1
      resultEvaluate.get shouldEqual stageRegister._1.head.event

      stageEvaluateRollback._1.length shouldEqual 1
      resultEvaluateRollback.get shouldEqual stageRegister._1.head.event

      stageEvaluate2._1.length shouldEqual 1
      resultEvaluate2.get shouldEqual stageRegister._1.head.event
    }

    run.unsafeRunSync()
    release.unsafeRunSync()
  }

  "Events service" should "keep valid pool state after rollback" in {
    implicit val (rocks, release) = TxRocksDB.make[IO, IO]("rocks").allocated.unsafeRunSync()
    implicit val storage          = OrdersStorage.make[IO]
    val events                    = Events.make[IO]
    val deployPool                = TransactionApply(swapPoolTx, 0, 10)
    val swap1                     = TransactionApply(swapRegisterTransaction, 0, 10)
    val swap2                     = TransactionApply(swapEvaluateTransaction, 0, 10)
    val swapRollback2             = TransactionUnapply(swapEvaluateTransaction, 0, 10)

    def run = for {
      _             <- events.process(NonEmptyList.one(deployPool))
      _             <- events.process(NonEmptyList.one(swap1))
      r1            <- events.process(NonEmptyList.one(swap2))
      pool1         <- storage.getPool(List(swapPool.get.box.boxId))
      pool2         <- storage.getPool(r1._2.map(_.event.box.boxId))
      _             <- events.process(NonEmptyList.one(swapRollback2))
      pool1Rollback <- storage.getPool(List(swapPool.get.box.boxId))
      pool2Rollback <- storage.getPool(r1._2.map(_.event.box.boxId))
      _             <- events.process(NonEmptyList.one(swap2))
      pool1Apply    <- storage.getPool(List(swapPool.get.box.boxId))
      pool2Apply    <- storage.getPool(r1._2.map(_.event.box.boxId))
    } yield {
      pool1.get shouldEqual swapPool.get
      pool2.get shouldEqual r1._2.head.event

      pool1Rollback.get shouldEqual swapPool.get
      pool2Rollback shouldEqual None

      pool1Apply.get shouldEqual swapPool.get
      pool2Apply.get shouldEqual r1._2.head.event
    }

    run.unsafeRunSync()
    release.unsafeRunSync()
  }

  "Events service" should "preserve events order" in {
    implicit val (rocks, release) = TxRocksDB.make[IO, IO]("rocks").allocated.unsafeRunSync()
    implicit val storage          = OrdersStorage.make[IO]
    val events                    = Events.make[IO]
    val deployPool                = TransactionApply(swapPoolTx, 0, 10)
    val swap1                     = TransactionApply(swapRegisterTransaction, 0, 10)
    val swapRollback1             = TransactionUnapply(swapRegisterTransaction, 0, 10)
    val swap2                     = TransactionApply(swapEvaluateTransaction, 0, 10)
    val swapRollback2             = TransactionUnapply(swapEvaluateTransaction, 0, 10)

    def run = for {
      result     <- events.process(NonEmptyList.of(deployPool, swap1, swapRollback1, swap1, swap2, swapRollback2, swap2))
      startPool  <- storage.getPool(List(swapPool.get.box.boxId))
      finishPool <- storage.getPool(List(result._2.last.event.box.boxId))
      order      <- storage.getOrder(List(result._1.head.event.order.box.boxId))
    } yield {
      val expectedOrders = List(
        BlockChainEvent.Apply(parser.registered(swap1.transaction, swap1.timestamp).unsafeRunSync().get),
        BlockChainEvent.Unapply(parser.registered(swap1.transaction, swap1.timestamp).unsafeRunSync().get),
        BlockChainEvent.Apply(parser.registered(swap1.transaction, swap1.timestamp).unsafeRunSync().get),
        BlockChainEvent.Apply(
          parser
            .evaluated(
              swap2.transaction,
              swap2.timestamp,
              parser.registered(swap1.transaction, swap1.timestamp).unsafeRunSync().get,
              swapPool.get,
              10
            )
            .unsafeRunSync()
            .get
        ),
        BlockChainEvent.Unapply(
          parser
            .evaluated(
              swap2.transaction,
              swap2.timestamp,
              parser.registered(swap1.transaction, swap1.timestamp).unsafeRunSync().get,
              swapPool.get,
              10
            )
            .unsafeRunSync()
            .get
        ),
        BlockChainEvent.Apply(
          parser
            .evaluated(
              swap2.transaction,
              swap2.timestamp,
              parser.registered(swap1.transaction, swap1.timestamp).unsafeRunSync().get,
              swapPool.get,
              10
            )
            .unsafeRunSync()
            .get
        )
      )
      result._1.zip(expectedOrders).foreach { case (actual, expected) =>
        actual shouldEqual expected
      }

      val expectedPools = List(
        BlockChainEvent.Apply(swapPool.get),
        BlockChainEvent.Apply(
          swap2.transaction.outputs.toList.flatMap(r => PoolParser.make.parse(r, swap2.timestamp, 10)).head
        ),
        BlockChainEvent.Unapply(
          swap2.transaction.outputs.toList.flatMap(r => PoolParser.make.parse(r, swap2.timestamp, 10)).head
        ),
        BlockChainEvent.Apply(
          swap2.transaction.outputs.toList.flatMap(r => PoolParser.make.parse(r, swap2.timestamp, 10)).head
        )
      )
      result._2.zip(expectedPools).foreach { case (actual, expected) =>
        actual shouldEqual expected
      }

      startPool.get shouldEqual swapPool.get
      finishPool.get shouldEqual swap2.transaction.outputs.toList
        .flatMap(r => PoolParser.make.parse(r, swap2.timestamp, 10))
        .head

      order.get shouldEqual result._1.head.event
    }

    run.unsafeRunSync()
    release.unsafeRunSync()

  }

  "Events service" should "not process invalid events order" in {
    implicit val (rocks, release) = TxRocksDB.make[IO, IO]("rocks").allocated.unsafeRunSync()
    implicit val storage          = OrdersStorage.make[IO]
    val events                    = Events.make[IO]
    val deployPool                = TransactionApply(swapPoolTx, 0, 10)
    val swap1                     = TransactionApply(swapRegisterTransaction, 0, 10)
    val swapRollback1             = TransactionUnapply(swapRegisterTransaction, 0, 10)
    val swap2                     = TransactionApply(swapEvaluateTransaction, 0, 10)

    def run = for {
      _ <- events.process(NonEmptyList.of(deployPool, swap1, swapRollback1))
      r <- events.process(NonEmptyList.of(swap2))
    } yield {
      r._1.isEmpty shouldBe true
      r._2.head.event shouldBe swap2.transaction.outputs.toList
        .flatMap(r => PoolParser.make.parse(r, swap2.timestamp, 10))
        .head
    }

    run.unsafeRunSync()
    release.unsafeRunSync()
  }

  override def afterEach(): Unit = {
    import scala.reflect.io.Directory
    import java.io.File
    new Directory(new File("rocks")).deleteRecursively()
  }

  override def beforeEach(): Unit = {
    import scala.reflect.io.Directory
    import java.io.File
    new Directory(new File("rocks")).deleteRecursively()
    Directory(new File("rocks")).createDirectory()
  }

}
