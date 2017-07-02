package models

import java.time.ZonedDateTime

import scalikejdbc._
import jsr310._
import skinny.orm._
import jp.t2v.lab.play2.pager.{ OrderType, Sortable }
import skinny.orm.feature.CRUDFeatureWithId

/**
  * Created by raru on 2017/07/02.
  */
case class Favorite(id: Option[Long],
                    userId: Long,
                    microPostId: Long,
                    createAt: ZonedDateTime,
                    updateAt: ZonedDateTime,
                    user: Option[User] = None,
                    microPost: Option[MicroPost] = None)

object Favorite extends SkinnyCRUDMapper[Favorite] {
  lazy val u = User.createAlias("u")

  lazy val userRef = belongsToWithAliasAndFkAndJoinCondition[User](
    right = User -> u,
    fk = "userId",
    on = sqls.eq(defaultAlias.userId, u.id),
    merge = (uf, f) => uf.copy(user = f)
  )

  lazy val mp = MicroPost.createAlias("mp")

  lazy val microPostRef = belongsToWithAliasAndFkAndJoinCondition[MicroPost](
    right = MicroPost -> mp,
    fk = "microPostId",
    on = sqls.eq(defaultAlias.microPostId, mp.id),
    merge = (uf, f) => uf.copy(microPost = f)
  )

  lazy val allAssociations: CRUDFeatureWithId[Long, Favorite] = joins(userRef, microPostRef)

  override def tableName = "favorites"

  override def defaultAlias: Alias[Favorite] = createAlias("f")

  override def extract(rs: WrappedResultSet, n: ResultName[Favorite]): Favorite =
    autoConstruct(rs, n, "user", "microPost")

  def create(favorite: Favorite)(implicit session: DBSession): Long =
    createWithAttributes(toNamedValues(favorite): _*)

  private def toNamedValues(record: Favorite): Seq[(Symbol, Any)] = Seq(
    'userId      -> record.userId,
    'microPostId -> record.microPostId,
    'createAt    -> record.createAt,
    'updateAt    -> record.updateAt
  )

  def update(favorite: Favorite)(implicit session: DBSession): Int =
    updateById(favorite.id.get).withAttributes(toNamedValues(favorite): _*)

  implicit object sortable extends Sortable[Favorite] {
    override val default: (String, OrderType) = ("id", OrderType.Descending)
    override val defaultPageSize: Int         = 10
    override val acceptableKeys: Set[String]  = Set("id")
  }
}
