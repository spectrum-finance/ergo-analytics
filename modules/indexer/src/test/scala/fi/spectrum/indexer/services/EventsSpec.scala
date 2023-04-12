package fi.spectrum.indexer.services

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import fi.spectrum.core.storage.OrdersStorage
import fi.spectrum.indexer.db.Indexer
import fi.spectrum.indexer.mocks.MetricsMock
import fi.spectrum.indexer.models.BlockChainEvent
import fi.spectrum.parser.PoolParser
import fi.spectrum.parser.evaluation.ProcessedOrderParser
import fi.spectrum.parser.evaluation.Transactions._
import fi.spectrum.streaming.domain.TransactionEvent._
import io.github.oskin1.rocksdb.scodec.TxRocksDB
import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class EventsSpec extends AnyFlatSpec with Matchers with Indexer with BeforeAndAfterEach {

  implicit val parser  = ProcessedOrderParser.make[IO]
  implicit val metrics = MetricsMock.make[IO]

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
      stageDeploy            <- events.process(deployPool)
      resultDeploy           <- storage.getPool(List(swapPool.get.box.boxId))
      stageRegister          <- events.process(swap1)
      resultRegister         <- storage.getOrders(List(stageRegister._1.head.event.order.box.boxId))
      stageRegisterRollback  <- events.process(swapRollback1)
      resultRegisterRollback <- storage.getOrders(List(stageRegisterRollback._1.head.event.order.box.boxId))
      stageRegister2         <- events.process(swap1)
      resultRegister2        <- storage.getOrders(List(stageRegister2._1.head.event.order.box.boxId))
      stageEvaluate          <- events.process(swap2)
      resultEvaluate         <- storage.getOrders(List(stageEvaluate._1.head.event.order.box.boxId))
      stageEvaluateRollback  <- events.process(swapRollback2)
      resultEvaluateRollback <- storage.getOrders(List(stageEvaluateRollback._1.head.event.order.box.boxId))
      stageEvaluate2         <- events.process(swap3)
      resultEvaluate2        <- storage.getOrders(List(stageEvaluate2._1.head.event.order.box.boxId))
    } yield {
      stageDeploy._2.head.event shouldEqual swapPool.get
      resultDeploy.get shouldEqual swapPool.get

      stageRegister._1.nonEmpty shouldEqual true
      resultRegister.head shouldEqual stageRegister._1.head.event

      stageRegisterRollback._1.nonEmpty shouldEqual true
      resultRegisterRollback shouldEqual List.empty

      stageRegister2._1.nonEmpty shouldEqual true
      resultRegister2.head shouldEqual stageRegister2._1.head.event

      stageEvaluate._1.nonEmpty shouldEqual true
      resultEvaluate.head shouldEqual stageRegister._1.head.event

      stageEvaluateRollback._1.nonEmpty shouldEqual true
      resultEvaluateRollback.head shouldEqual stageRegister._1.head.event

      stageEvaluate2._1.nonEmpty shouldEqual true
      resultEvaluate2.head shouldEqual stageRegister._1.head.event
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
      _             <- events.process(deployPool)
      _             <- events.process(swap1)
      r1            <- events.process(swap2)
      pool1         <- storage.getPool(List(swapPool.get.box.boxId))
      pool2         <- storage.getPool(List(r1._2.map(_.event.box.boxId).get))
      _             <- events.process(swapRollback2)
      pool1Rollback <- storage.getPool(List(swapPool.get.box.boxId))
      pool2Rollback <- storage.getPool(List(r1._2.map(_.event.box.boxId).get))
      _             <- events.process(swap2)
      pool1Apply    <- storage.getPool(List(swapPool.get.box.boxId))
      pool2Apply    <- storage.getPool(List(r1._2.map(_.event.box.boxId).get))
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
      result     <- events.process(deployPool)
      o1         <- events.process(swap1)
      o2         <- events.process(swapRollback1)
      o3         <- events.process(swap1)
      o4         <- events.process(swap2)
      o5         <- events.process(swapRollback2)
      o6         <- events.process(swap2)
      startPool  <- storage.getPool(List(swapPool.get.box.boxId))
      finishPool <- storage.getPool(List(o6._2.last.event.box.boxId))
      order      <- storage.getOrders(List(o1._1.head.event.order.box.boxId))
    } yield {
      o1._1.head shouldEqual BlockChainEvent.Apply(
        parser.registered(swap1.transaction, swap1.timestamp).unsafeRunSync().head
      )
      o2._1.head shouldEqual BlockChainEvent.Unapply(
        parser.registered(swap1.transaction, swap1.timestamp).unsafeRunSync().head
      )
      o3._1.head shouldEqual BlockChainEvent.Apply(
        parser.registered(swap1.transaction, swap1.timestamp).unsafeRunSync().head
      )
      o4._1.head shouldEqual BlockChainEvent.Apply(
        parser
          .evaluated(
            swap2.transaction,
            swap2.timestamp,
            parser.registered(swap1.transaction, swap1.timestamp).unsafeRunSync().head,
            swapPool.get,
            10, List.empty
          )
          .unsafeRunSync()
          .get
      )
      o5._1.head shouldEqual BlockChainEvent.Unapply(
        parser
          .evaluated(
            swap2.transaction,
            swap2.timestamp,
            parser.registered(swap1.transaction, swap1.timestamp).unsafeRunSync().head,
            swapPool.get,
            10, List.empty
          )
          .unsafeRunSync()
          .get
      )
      o6._1.head shouldEqual BlockChainEvent.Apply(
        parser
          .evaluated(
            swap2.transaction,
            swap2.timestamp,
            parser.registered(swap1.transaction, swap1.timestamp).unsafeRunSync().head,
            swapPool.get,
            10, List.empty
          )
          .unsafeRunSync()
          .get
      )

      result._2.get shouldEqual BlockChainEvent.Apply(swapPool.get)
      o4._2.get shouldEqual BlockChainEvent.Apply(
        swap2.transaction.outputs.toList.flatMap(r => PoolParser.make.parse(r, swap2.timestamp, 10)).head
      )
      o5._2.get shouldEqual BlockChainEvent.Unapply(
        swap2.transaction.outputs.toList.flatMap(r => PoolParser.make.parse(r, swap2.timestamp, 10)).head
      )
      o6._2.get shouldEqual BlockChainEvent.Apply(
        swap2.transaction.outputs.toList.flatMap(r => PoolParser.make.parse(r, swap2.timestamp, 10)).head
      )

      startPool.get shouldEqual swapPool.get
      finishPool.get shouldEqual swap2.transaction.outputs.toList
        .flatMap(r => PoolParser.make.parse(r, swap2.timestamp, 10))
        .head

      order.head shouldEqual o1._1.head.event
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
      _ <- events.process(deployPool)
      _ <- events.process(swap1)
      _ <- events.process(swapRollback1)
      r <- events.process(swap2)
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
    import java.io.File
    import scala.reflect.io.Directory
    new Directory(new File("rocks")).deleteRecursively()
    ()
  }

  override def beforeEach(): Unit = {
    import java.io.File
    import scala.reflect.io.Directory
    new Directory(new File("rocks")).deleteRecursively()
    Directory(new File("rocks")).createDirectory()
    ()
  }

}
