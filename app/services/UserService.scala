package services

import jp.t2v.lab.play2.pager.{ Pager, SearchResult }
import models.User
import scalikejdbc.{ AutoSession, DBSession }

import scala.util.Try

/**
  * Created by raru on 2017/06/29.
  */
trait UserService {
  def create(user: User)(implicit session: DBSession = AutoSession): Try[Long]

  def findByEmail(email: String)(implicit session: DBSession = AutoSession): Try[Option[User]]

  def findAll(pager: Pager[User])(implicit dBSession: DBSession = AutoSession): Try[SearchResult[User]]

  def findById(id: Long)(implicit dbSession: DBSession = AutoSession): Try[Option[User]]
}
