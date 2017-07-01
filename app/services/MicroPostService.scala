package services

import jp.t2v.lab.play2.pager.{ Pager, SearchResult }
import models.MicroPost
import scalikejdbc.{ AutoSession, DBSession }

import scala.util.Try

/**
  * Created by raru on 2017/07/01.
  */
trait MicroPostService {
  def create(microPost: MicroPost)(implicit dbSession: DBSession = AutoSession): Try[Long]

  def deleteById(microPostId: Long)(implicit dbSession: DBSession = AutoSession): Try[Int]

  def findByUserId(pager: Pager[MicroPost], userId: Long)(
      implicit dbSession: DBSession = AutoSession
  ): Try[SearchResult[MicroPost]]

  def countBy(userId: Long)(implicit dbSession: DBSession = AutoSession): Try[Long]

  def findAllByWithLimitOffset(pager: Pager[MicroPost], userId: Long)(
      implicit dbSession: DBSession = AutoSession
  ): Try[SearchResult[MicroPost]]
}
