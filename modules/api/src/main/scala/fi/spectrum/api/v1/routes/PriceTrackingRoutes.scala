package fi.spectrum.api.v1.routes

import cats.effect.kernel.Async
import fi.spectrum.api.v1.endpoints.PriceTrackingEndpoints
import fi.spectrum.api.v1.services.PriceTracking
import fi.spectrum.common.http.AdaptThrowable.AdaptThrowableEitherT
import fi.spectrum.common.http.HttpError
import cats.syntax.semigroupk._
import fi.spectrum.common.http.syntax._
import org.http4s.HttpRoutes
import sttp.tapir.server.http4s.{Http4sServerInterpreter, Http4sServerOptions}

final class PriceTrackingRoutes[
  F[_]: Async: AdaptThrowableEitherT[*[_], HttpError]
](pt: PriceTracking[F])(implicit
  opts: Http4sServerOptions[F]
) {

  private val endpoints = new PriceTrackingEndpoints
  import endpoints._

  private val interpreter = Http4sServerInterpreter(opts)

  def routes: HttpRoutes[F] =
    getTokenSupplyR <+> getVerifiedMarketsR <+> getMarketsR <+> getPairsCoinGeckoR <+> getTickersCoinGeckoR <+> getSpfTokenPriceR

  def getVerifiedMarketsR: HttpRoutes[F] = interpreter.toRoutes(getVerifiedMarketsE.serverLogic { _ =>
    pt.getVerifiedMarkets.adaptThrowable.value
  })

  def getMarketsR: HttpRoutes[F] = interpreter.toRoutes(getMarketsE.serverLogic { tw =>
    pt.getMarkets(tw).adaptThrowable.value
  })

  def getPairsCoinGeckoR: HttpRoutes[F] = interpreter.toRoutes(
    getPairsCoinGeckoE.serverLogic(_ => pt.getPairsCoinGecko.adaptThrowable.value)
  )

  def getTickersCoinGeckoR: HttpRoutes[F] = interpreter.toRoutes(
    getTickersCoinGeckoE.serverLogic(_ => pt.getTickersCoinGecko.adaptThrowable.value)
  )

  def getSpfTokenPriceR: HttpRoutes[F] = interpreter.toRoutes(
    getSpfTokenPriceE.serverLogic(_ => pt.getSpfPrice.adaptThrowable.orNotFound(s"SPF price}").value)
  )

  def getTokenSupplyR: HttpRoutes[F] = interpreter.toRoutes(getTokenSupplyE.serverLogic { _ =>
    pt.getTokenSupply.adaptThrowable.orNotFound(s"TokenSupply").value
  })
}

object PriceTrackingRoutes {

  def make[
    F[_]: Async: AdaptThrowableEitherT[*[_], HttpError]
  ](implicit
    pt: PriceTracking[F],
    opts: Http4sServerOptions[F]
  ): HttpRoutes[F] =
    new PriceTrackingRoutes[F](pt).routes
}
