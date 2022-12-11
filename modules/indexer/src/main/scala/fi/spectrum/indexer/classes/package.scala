package fi.spectrum.indexer

package object classes {

  object syntax {
    implicit final class ToSchemaOps[A, B](val a: A) extends AnyVal {
      def transform(implicit schema: ToSchema[A, B]): B = schema.transform(a)
    }
  }

}
