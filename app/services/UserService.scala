package services

import models.User
import scalikejdbc.{ AutoSession, DBSession }

import scala.util.Try

/**
  * Created by raru on 2017/06/29.
  */
trait UserService {
  def create(user: User)(implicit session: DBSession = AutoSession): Try[Long]

  def findByEmail(email: String)(implicit session: DBSession = AutoSession): Try[Option[User]]

  def findAll(implicit dBSession: DBSession = AutoSession): Try[List[User]]

  def findById(id: Long)(implicit dbSession: DBSession = AutoSession): Try[Option[User]]
}
