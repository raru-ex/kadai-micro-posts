package services

import javax.inject.Singleton

import models.User
import scalikejdbc.{ AutoSession, DBSession }

import scala.util.Try

/**
  * Created by raru on 2017/06/29.
  */
@Singleton
class UserServiceImpl extends UserService {

  override def create(user: User)(implicit session: DBSession): Try[Long] = Try {
    User.create(user)
  }

  override def findByEmail(email: String)(implicit session: DBSession): Try[Option[User]] = Try {
    User.where('email -> email).apply().headOption
  }
}
