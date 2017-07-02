package controllers

import java.time.ZonedDateTime
import javax.inject._

import jp.t2v.lab.play2.auth.AuthenticationElement
import jp.t2v.lab.play2.pager.Pager
import models.Favorite
import play.api.Logger
import play.api.i18n.{ I18nSupport, Messages, MessagesApi }
import play.api.mvc.{ Action, AnyContent, Controller }
import services.{ FavoriteService, UserFollowService, UserService }

/**
  * Created by raru on 2017/07/02.
  */
@Singleton
class FavoriteController @Inject()(val userFollowService: UserFollowService,
                                   val favoriteService: FavoriteService,
                                   val userService: UserService,
                                   val messagesApi: MessagesApi)
    extends Controller
    with I18nSupport
    with AuthConfigSupport
    with AuthenticationElement {

  def favorite(microPostId: Long): Action[AnyContent] = StackAction { implicit request =>
    val currentUser = loggedIn
    val now         = ZonedDateTime.now()
    val favorite    = Favorite(None, currentUser.id.get, microPostId, now, now)
    favoriteService
      .create(favorite)
      .map { _ =>
        Redirect(routes.HomeController.index(Pager.default))
      }
      .recover {
        case e: Exception =>
          Logger.error("occurred error", e)
          Redirect(routes.HomeController.index(Pager.default))
            .flashing("failure" -> Messages("InternalError"))
      }
      .getOrElse(InternalServerError(Messages("InternalError")))
  }

  def remove(microPostId: Long): Action[AnyContent] = StackAction { implicit request =>
    val currentUser = loggedIn
    favoriteService
      .deleteBy(currentUser.id.get, microPostId)
      .map { _ =>
        Redirect(routes.HomeController.index(Pager.default))
      }
      .recover {
        case e: Exception =>
          Logger.error("occurred error", e)
          Redirect(routes.HomeController.index(Pager.default))
            .flashing("failure" -> Messages("InternalError"))
      }
      .getOrElse(InternalServerError(Messages("InternalError")))
  }
}
