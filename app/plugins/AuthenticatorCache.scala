package plugins

import play.api.Application
import play.api.cache.Cache
import securesocial.core.{Authenticator, AuthenticatorStore}
import com.mongodb.casbah.commons.MongoDBObject
import com.novus.salat.dao.SalatDAO
import persist.mongoModel.AuthenticatorForMongo
import persist.SalatDAOFactory

/**
 * We use mongo a the cache for cookies (it's WEBSCALE).
 * @param app
 */
class AuthenticatorCache(app: Application) extends AuthenticatorStore(app) {

  def save(authenticator: Authenticator): Either[Error, Unit] = {
    cacheDao.save(AuthenticatorForMongo.fromAuthenticator(authenticator))
    Right(())
  }

  def find(id: String): Either[Error, Option[Authenticator]] = {
    Right(cacheDao.findOneById(id).map(AuthenticatorForMongo.toAuthenticator))
  }

  def delete(id: String): Either[Error, Unit] = {
    cacheDao.removeById(id)
    Right(())
  }

  private def cacheDao: SalatDAO[AuthenticatorForMongo, String] = {
    SalatDAOFactory.get.cacheSalatDAO
  }
}