package services

import jp.t2v.lab.play2.pager.{ Pager, SearchResult }
import models.{ Favorite, MicroPost, User }
import scalikejdbc.{ AutoSession, DBSession }

import scala.util.Try

/**
  * Created by raru on 2017/07/02.
  */
trait FavoriteService {
  def create(favorite: Favorite)(implicit dbSession: DBSession = AutoSession): Try[Long]

  def findById(favoriteId: Long)(implicit dbSession: DBSession = AutoSession): Try[Option[Favorite]]

  def findByUserId(userId: Long)(implicit dbSession: DBSession = AutoSession): Try[List[Favorite]]

  def findPagedMicroPostsByUserId(pager: Pager[MicroPost], userId: Long)(
      implicit dbSession: DBSession = AutoSession
  ): Try[SearchResult[MicroPost]]

  def findPagedFavoriteUsersByMicroPostId(pager: Pager[User], microPostId: Long)(
      implicit dbSession: DBSession = AutoSession
  ): Try[SearchResult[User]]

  def countByUserId(userId: Long)(implicit dbSession: DBSession = AutoSession): Try[Long]

  def countByMicroPostId(microPostId: Long)(implicit dbSession: DBSession = AutoSession): Try[Long]

  def deleteBy(userId: Long, microPostId: Long)(implicit dbSession: DBSession = AutoSession): Try[Int]
}
