package fi.spectrum.api.services

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import fi.spectrum.api.domain.{Fees, TotalValueLocked}
import fi.spectrum.api.models.{Currency, CurrencyId, FiatUnits}
import fi.spectrum.api.modules.AmmStatsMath
import fi.spectrum.api.v1.endpoints.models.TimeWindow
import org.scalatest.matchers.should
import org.scalatest.propspec.AnyPropSpec
import cats.syntax.option._
import fi.spectrum.api.db.models.amm.PoolInfo
import fi.spectrum.core.domain.order.PoolId
import tofu.logging.Logs

import scala.concurrent.duration.DurationInt

class AmmStatsMathSpec extends AnyPropSpec with should.Matchers  {

  // fees -> window=TimeWindow{from=1676730323161,to=1684619561161}

  //PoolStats{
  // id=9916d75132593c8b07fe18bd8d583bda1652eed7565cf41a4738ddd90fc992ec,
  // lockedX=FullAsset{id=0000000000000000000000000000000000000000000000000000000000000000,
  // amount=120752939689077,
  // ticker=ERG,decimals=9
  // },
  // lockedY=FullAsset{id=03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04,
  // amount=19787311,
  // ticker=SigUSD,decimals=2
  // },tvl=TotalValueLocked{value=41671250,units=FiatUnits{currency=Currency{id=USD,decimals=2}}},
  //  volume=Volume{value=98539,units=FiatUnits{currency=Currency{id=USD,decimals=2}},
  // window=TimeWindow{from=1676730323161,to=1684619561161}},
  // fees=Fees{value=492,units=FiatUnits{currency=Currency{id=USD,decimals=2}},window=TimeWindow{from=1676730323161,to=1684619561161}},
  // yearlyFeesPercent=0.0}

  //2125798066
  //120

  //feePercentProjection(
  //  poolId=9916d75132593c8b07fe18bd8d583bda1652eed7565cf41a4738ddd90fc992ec,
  //  tvl=TotalValueLocked{value=41670840,units=FiatUnits{currency=Currency{id=USD,decimals=2}}},
  //  fees=Fees{value=492,units=FiatUnits{currency=Currency{id=USD,decimals=2}},window=TimeWindow{from=1676732366030,to=1684621604030}},
  //  poolInfo=PoolInfo{firstSwapTimestamp=1630335881195},
  //  projectionPeriod=365 days
  //)

  //feePercentProjection(
  //  poolId=9916d75132593c8b07fe18bd8d583bda1652eed7565cf41a4738ddd90fc992ec,
  //  tvl=TotalValueLocked{value=41972768,units=FiatUnits{currency=Currency{id=USD,decimals=2}}},
  //  fees=Fees{value=496,units=FiatUnits{currency=Currency{id=USD,decimals=2}},window=TimeWindow{from=1676733878079,to=<none>}},
  //  poolInfo=PoolInfo{firstSwapTimestamp=1630335881195},
  //  projectionPeriod=365 days
  //)

  property("Get ERG/USD rate") {
    implicit val logs: Logs[IO, IO] = Logs.sync[IO, IO]
    val tw = TimeWindow(1676730323161L.some,none)
    val math = AmmStatsMath.make[IO, IO].unsafeRunSync()

    val res = math.feePercentProjection(
      PoolId.unsafeFromString(""),
      TotalValueLocked(BigDecimal(41671250),FiatUnits(Currency(CurrencyId("USD"), 2))),
      Fees(BigDecimal(492),  FiatUnits(Currency(CurrencyId("USD"), 2)), tw),
      PoolInfo(1630335881195L),
      365.days
    ).unsafeRunSync()

    println(res)
  }
}
