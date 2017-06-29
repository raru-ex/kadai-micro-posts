package controllers

import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.test.FakeRequest
import play.api.test.Helpers._

/**
  * Created by raru on 2017/06/29.
  */
class HomeControllerSpec extends PlayFunSpec with GuiceOneAppPerSuite {

  describe("HomeController") {
    describe("route of HomeController#index") {
      it("should be valid") {
        val result = route(app, addCsrfToken(FakeRequest(GET, routes.HomeController.index().toString)))
      }
    }
  }
}
