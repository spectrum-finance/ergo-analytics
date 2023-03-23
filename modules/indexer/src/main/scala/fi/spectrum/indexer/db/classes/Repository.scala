package fi.spectrum.indexer.db.classes

trait Repository[T, I] extends DeleteRepository[I] with InsertRepository[T]
