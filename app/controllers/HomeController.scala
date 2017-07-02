package controllers

import javax.inject._

import jp.t2v.lab.play2.auth.OptionalAuthElement
import jp.t2v.lab.play2.pager.{ Pager, SearchResult }
import models.{ Favorite, MicroPost }
import play.api.Logger
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{ I18nSupport, Messages, MessagesApi }
import play.api.mvc._
import services.{ FavoriteService, MicroPostService, UserService }

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject()(val userService: UserService,
                               val microPostService: MicroPostService,
                               val favoriteService: FavoriteService,
                               val messagesApi: MessagesApi)
    extends Controller
    with I18nSupport
    with AuthConfigSupport
    with OptionalAuthElement {

  private val postForm = Form {
    "content" -> nonEmptyText
  }

  def index(pager: Pager[MicroPost]): Action[AnyContent] = StackAction { implicit request =>
    val userOpt = loggedIn
    userOpt
      .map { user =>
        val favorites = favoriteService.findByUserId(user.id.get).getOrElse(List.empty[Favorite])
        microPostService
          .findAllByWithLimitOffset(pager, user.id.get)
          .map { searchResult =>
            Ok(views.html.index(userOpt, postForm, searchResult, favorites))
          }
          .recover {
            case e: Exception =>
              Logger.error(s"occurred error", e)
              Redirect(routes.HomeController.index(Pager.default))
                .flashing("failure" -> Messages("InternalError"))
          }
          .getOrElse(InternalServerError(Messages("InternalError")))
      }
      .getOrElse(
        Ok(
          views.html.index(userOpt, postForm, SearchResult(pager, 0)(_ => Seq.empty[MicroPost]), List.empty[Favorite])
        )
      )
  }

}
