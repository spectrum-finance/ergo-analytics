package fi.spectrum.indexer.classes

import cats.Applicative
import cats.data.NonEmptyList
import fi.spectrum.indexer.db.persistence.Persist
import fi.spectrum.indexer.classes.syntax._
import mouse.all._
import cats.syntax.applicative._

/** Keeps both ToSchema from A to B and Persist for B.
  *  Contains evidence that A can be mapped into B and B can be persisted.
  *
  *  Takes batch of T elements, maps them using ToSchema, persists them using Persist
  */
trait Handle[T, F[_]] {
  def handle(in: NonEmptyList[T]): F[Int]
}

object Handle {

  def makeOptional[A, B, F[_]: Applicative](implicit
    schema: ToSchema[A, Option[B]],
    persist: Persist[B, F]
  ): Handle[A, F] =
    new Live[A, B, F]

  final private class Live[A, B, F[_]: Applicative](implicit schema: ToSchema[A, Option[B]], persist: Persist[B, F])
    extends Handle[A, F] {

    override def handle(in: NonEmptyList[A]): F[Int] =
      in.map(_.transform).toList.flatten match {
        case x :: xs => NonEmptyList.of(x, xs: _*) |> persist.persist
        case Nil     => 0.pure[F]
      }
  }
}
