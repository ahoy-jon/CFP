package persist.mongoModel

import com.novus.salat.annotations._
import securesocial.core.{Authenticator, UserId}
import org.joda.time.DateTime

case class AuthenticatorForMongo(
                                  @Key("_id") id : String,
                                  userId :UserId,
                                  creationDate : Long,
                                  lastUsed : Long,
                                  expirationDate : Long)

object AuthenticatorForMongo {

  object impl {
    implicit def dateToLong(d:DateTime):Long = d.getMillis
    implicit def longToDate(l:Long):DateTime = new DateTime(l)
  }


  def toAuthenticator(afm:AuthenticatorForMongo):Authenticator = {
    import afm._
    import impl._
    Authenticator(id, userId, creationDate, lastUsed, expirationDate)
  }

  def fromAuthenticator(a:Authenticator):AuthenticatorForMongo = {
    import a._
    import impl._
    AuthenticatorForMongo(id,userId, creationDate, lastUsed, expirationDate)
  }

}