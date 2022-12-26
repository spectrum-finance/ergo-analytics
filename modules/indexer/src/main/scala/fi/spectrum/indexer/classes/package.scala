package fi.spectrum.indexer

package object classes {

  object syntax {
    implicit final class ToDBOps[A, B](val a: A) extends AnyVal {
      def toDB(implicit db: ToDB[A, B]): B = db.toDB(a)
    }
  }

}
