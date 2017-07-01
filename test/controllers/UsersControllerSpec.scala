package controllers

import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.test.FakeRequest
import play.api.test.Helpers._
import services.UserService
import jp.t2v.lab.play2.auth.test.Helpers._

/**
  * Created by raru on 2017/07/01.
  */
class UsersControllerSpec extends PlayFunSpec with GuiceOneAppPerSuite {

  object Config extends AuthConfigSupport {
    override val userService: UserService = app.injector.instanceOf[UserService]
  }

  describe("UsersController") {
    describe("route of UsersController#index") {
      it("should be valid") {
        val result = route(app,
                           addCsrfToken(FakeRequest(GET, routes.UsersController.index().toString))
                             .withLoggedIn(Config)("test@test.com")).get
        status(result) mustBe OK
      }
    }

    describe("route of UsersController#show") {
      it("should be valid") {
        val userId = 1L
        val result = route(app,
                           addCsrfToken(FakeRequest(GET, routes.UsersController.show(userId).toString))
                             .withLoggedIn(Config)("test@test.com")).get
        status(result) mustBe OK
      }
    }
  }

}
