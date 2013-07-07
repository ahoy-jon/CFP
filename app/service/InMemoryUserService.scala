package service

import play.api.{Logger, Application}
import securesocial.core._
import providers.utils.BCryptPasswordHasher
import securesocial.core.providers.Token
import securesocial.core.UserId
import org.mindrot.jbcrypt.BCrypt
import util.Printhelper


/**
 * A Sample In Memory user service in Scala
 *
 * IMPORTANT: This is just a sample and not suitable for a production environment since
 * it stores everything in memory.
 */
class InMemoryUserService(application: Application) extends UserServicePlugin(application) {
  private var users = Map[String, Identity]()
  private var tokens = Map[String, Token]()


  //TODO remove
  {
    val userFixture = {

      securesocial.core.SocialUser(securesocial.core.UserId("jonathan.winandy@gmail.com",
        "userpass"),"Jon","Jon","Jon Jon",scala.Some("jonathan.winandy@gmail.com"),
        scala.Some("http://www.gravatar.com/avatar/4af948aed92e5a3cbe0205eb77551e1c?d=404"),
        securesocial.core.AuthenticationMethod("userPassword"),None,None,
        scala.Some(securesocial.core.PasswordInfo("bcrypt","$2a$10$llXDhy6g9L8XPJ1K73wKueNDbkWB/5xQABlxXB41BHtmuchQVt.sO",None)))

    }

    save(userFixture)

  }

  private def userIdtoString(id:UserId):String = id.id + id.providerId


  def find(id: UserId): Option[Identity] = {
    if ( Logger.isDebugEnabled ) {
      Logger.debug("users = %s".format(users))
    }
    users.get(userIdtoString(id))
  }

  def findByEmailAndProvider(email: String, providerId: String): Option[Identity] = {
    if ( Logger.isDebugEnabled ) {
      Logger.debug("users = %s".format(users))
    }
    users.values.find( u => u.email.map( e => e == email && u.id.providerId == providerId).getOrElse(false))
  }

  def save(user: Identity): Identity = {

    println(Printhelper.printcov(user))
    users = users + (userIdtoString(user.id) -> user)
    // this sample returns the same user object, but you could return an instance of your own class
    // here as long as it implements the Identity trait. This will allow you to use your own class in the protected
    // actions and event callbacks. The same goes for the find(id: UserId) method.
    user
  }

  def save(token: Token) {
    tokens += (token.uuid -> token)
  }

  def findToken(token: String): Option[Token] = {
    tokens.get(token)
  }

  def deleteToken(uuid: String) {
    tokens -= uuid
  }

  def deleteTokens() {
    tokens = Map()
  }

  def deleteExpiredTokens() {
    tokens = tokens.filter(!_._2.isExpired)
  }
}
