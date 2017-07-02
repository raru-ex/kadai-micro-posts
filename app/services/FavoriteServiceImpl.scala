package services

import javax.inject._

import jp.t2v.lab.play2.pager.{ OrderType, Pager, SearchResult, Sorter }
import models.{ Favorite, MicroPost, User, UserFollow }
import scalikejdbc._

import scala.util.Try

/**
  * Created by raru on 2017/07/02.
  */
@Singleton
class FavoriteServiceImpl extends FavoriteService {

  // post用ソート
  implicit def sortersToSQLSyntaxMicroPost(sorters: Seq[Sorter[MicroPost]]): Seq[SQLSyntax] = {
    sorters.map { sorter =>
      if (sorter.dir == OrderType.Descending)
        MicroPost.defaultAlias.id.desc
      else
        MicroPost.defaultAlias.id.asc
    }
  }

  // user用ソート
  implicit def sortersToSQLSyntaxUser(sorters: Seq[Sorter[User]]): Seq[SQLSyntax] = {
    sorters.map { sorter =>
      if (sorter.dir == OrderType.Descending)
        User.defaultAlias.id.desc
      else
        User.defaultAlias.id.asc
    }
  }

  override def create(favorite: Favorite)(implicit dbSession: DBSession): Try[Long] = Try {
    Favorite.create(favorite)
  }

  override def findById(favoriteId: Long)(implicit dbSession: DBSession): Try[Option[Favorite]] = Try {
    Favorite.findById(favoriteId)
  }

  override def findByUserId(userId: Long)(implicit dbSession: DBSession): Try[List[Favorite]] = Try {
    Favorite.where('userId -> userId).apply()
  }

  override def findPagedMicroPostsByUserId(pager: Pager[MicroPost], userId: Long)(
      implicit dbSession: DBSession
  ): Try[SearchResult[MicroPost]] = {
    countByUserId(userId).map { size =>
      SearchResult(pager, size) { pager =>
        Favorite
          .includes(Favorite.microPostRef)
          .findAllByWithLimitOffset(
            sqls.eq(Favorite.defaultAlias.userId, userId),
            pager.limit,
            pager.offset,
            pager.allSorters
          )
          .map(_.microPost.get)
      }
    }
  }

  override def findPagedFavoriteUsersByMicroPostId(pager: Pager[User], microPostId: Long)(
      implicit dbSession: DBSession
  ): Try[SearchResult[User]] = {
    countByMicroPostId(microPostId).map { size =>
      SearchResult(pager, size) { pager =>
        Favorite
          .includes(Favorite.userRef)
          .findAllByWithLimitOffset(
            sqls.eq(Favorite.defaultAlias.microPostId, microPostId),
            pager.limit,
            pager.offset,
            pager.allSorters
          )
          .map(_.user.get)
      }
    }
  }

  override def countByUserId(userId: Long)(implicit dbSession: DBSession): Try[Long] = Try {
    Favorite.allAssociations.countBy(sqls.eq(Favorite.defaultAlias.userId, userId))
  }

  override def countByMicroPostId(microPostId: Long)(implicit dbSession: DBSession): Try[Long] = Try {
    Favorite.allAssociations.countBy(sqls.eq(Favorite.defaultAlias.microPostId, microPostId))
  }

  override def deleteBy(userId: Long, microPostId: Long)(implicit dbSession: DBSession): Try[Int] = Try {
    val c     = Favorite.column
    val count = Favorite.countBy(sqls.eq(c.microPostId, microPostId).and.eq(c.userId, userId))
    if (count == 1) {
      Favorite.deleteBy(
        sqls
          .eq(Favorite.column.userId, userId)
          .and(sqls.eq(Favorite.column.microPostId, microPostId))
      )
    } else 0
  }
}
