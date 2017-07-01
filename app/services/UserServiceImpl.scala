package services

import javax.inject.Singleton
import jp.t2v.lab.play2.pager.scalikejdbc._ // 明示的にインポートする
import jp.t2v.lab.play2.pager.{ Pager, SearchResult }
import models.User
import scalikejdbc.DBSession

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

  override def findAll(pager: Pager[User])(implicit dbSession: DBSession): Try[SearchResult[User]] = Try {
    val size = User.countAllModels()
    SearchResult(pager, size) { pager =>
      User.findAllWithLimitOffset(
        pager.limit,
        pager.offset,
        pager.allSorters.map(_.toSQLSyntax(User.defaultAlias))
      )
    }
  }

  override def findById(id: Long)(implicit dbSession: DBSession): Try[Option[User]] = Try {
    User.findById(id)
  }
}
