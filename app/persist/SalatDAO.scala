package persist

import com.novus.salat.dao.SalatDAO
import mongoModel.AuthenticatorForMongo
import com.novus.salat.{EnumStrategy, Context}
import com.novus.salat.json.{StringObjectIdStrategy, JSONConfig}
import com.mongodb.casbah.{MongoCollection, MongoConnection}
import play.api.Logger
import com.mongodb.casbah.commons.MongoDBObject


object SalatDAOFactory {
  lazy val get:SalatDAOFactory = {
    import Preamble._
    new SalatDAOFactory(new MongoCollectionProvider(mongoHostName,mongoPort))
  }
}

class SalatDAOFactory(val collectionProvider: MongoCollectionProvider) {
  private implicit val ctx = SalatContextHolder.salatContext

  private def DAO[T <: AnyRef : Manifest,ID:Manifest](collectionName:String):SalatDAO[T,ID] = {
    val collection = collectionProvider.getCollection(collectionName)
    new SalatDAO[T,ID](collection) {}
  }

  val cacheSalatDAO = DAO[AuthenticatorForMongo,String]("auth_token")
}



object SalatContextHolder {

  val salatContext: Context = new Context {
    val name = "Custom Salat Context"
    // some overrides or custom behavior
    override val jsonConfig = JSONConfig(
      objectIdStrategy = StringObjectIdStrategy
    )
    override val defaultEnumStrategy = EnumStrategy.BY_VALUE
  }

}


object Preamble {

  //Set the debug flag
  val DEBUG = true
  val PROFILER = true

  val mongoHostName: String = "127.0.0.1"

  val mongoPort: Int = 27017

  //The mongo db name
  val DB_NAME = "cfp"


}

object MongoUtils {

  type $ = MongoDBObject
  val $ = MongoDBObject
}



class MongoCollectionProvider(host: String,port: Int) {

  import Preamble._

  println("Connection to mongo = " + this)

  private val mongoConnection = MongoConnection(host,port)

  //Get all collections from database and put them in a map
  private val collections: Map[String, MongoCollection] = {
    (
      for (
        c <- mongoConnection(DB_NAME).getCollectionNames()
      ) yield ( c -> mongoConnection(DB_NAME)(c) )
      ).toMap
  }

  // Get a collection
  def getCollection(collectionName: String): MongoCollection = {
    import MongoUtils._
    collections.get(collectionName) match {
      case Some(collection) => collection
      case None => {
        // TODO rework: temporary fix dynamic collection creation but not optimized
        mongoConnection(DB_NAME).createCollection(collectionName,$())
        Logger.warn("MongoDB collection created with name: " + collectionName)
        mongoConnection(DB_NAME)(collectionName)
      }
    }
  }


}



