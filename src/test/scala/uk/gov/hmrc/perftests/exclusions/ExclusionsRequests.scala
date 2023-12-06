/*
 * Copyright 2023 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.perftests.exclusions

import io.gatling.core.Predef._
import io.gatling.core.session.Expression
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder
import uk.gov.hmrc.performance.conf.ServicesConfiguration

import java.time.LocalDate

object ExclusionsRequests extends ServicesConfiguration {

  val baseUrl: String = baseUrlFor("ioss-exclusions-frontend")
  val route: String   = "/pay-vat-on-goods-sold-to-eu/leave-import-one-stop-shop"
  val loginUrl        = baseUrlFor("auth-login-stub")

  def inputSelectorByName(name: String): Expression[String] = s"input[name='$name']"

  def getAuthorityWizard =
    http("Get Authority Wizard page")
      .get(loginUrl + s"/auth-login-stub/gg-sign-in")
      .check(status.in(200, 303))

  def postAuthorityWizard =
    http("Enter Auth login credentials ")
      .post(loginUrl + s"/auth-login-stub/gg-sign-in")
      .formParam("authorityId", "")
      .formParam("gatewayToken", "")
      .formParam("credentialStrength", "strong")
      .formParam("confidenceLevel", "50")
      .formParam("affinityGroup", "Organisation")
      .formParam("email", "user@test.com")
      .formParam("credentialRole", "User")
      .formParam("redirectionUrl", baseUrl + route)
      .formParam("enrolment[0].name", "HMRC-MTD-VAT")
      .formParam("enrolment[0].taxIdentifier[0].name", "VRN")
      .formParam("enrolment[0].taxIdentifier[0].value", "${vrn}")
      .formParam("enrolment[0].state", "Activated")
      .formParam("enrolment[1].name", "HMRC-IOSS-ORG")
      .formParam("enrolment[1].taxIdentifier[0].name", "IOSSNumber")
      .formParam("enrolment[1].taxIdentifier[0].value", "IM9001234567")
      .formParam("enrolment[1].state", "Activated")
      .check(status.in(200, 303))
      .check(headerRegex("Set-Cookie", """mdtp=(.*)""").saveAs("mdtpCookie"))

  def getMoveCountry =
    http("Get Move Country page")
      .get(s"$baseUrl$route/move-country")
      .header("Cookie", "mdtp=${mdtpCookie}")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def postMoveCountry =
    http("Post Move Country")
      .post(s"$baseUrl$route/move-country")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("value", "true")
      .check(status.in(200, 303))
      .check(header("Location").is(s"$route/eu-country"))

  def getEuCountry =
    http("Get EU Country page")
      .get(s"$baseUrl$route/eu-country")
      .header("Cookie", "mdtp=${mdtpCookie}")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def postEuCountry =
    http("Post EU Country")
      .post(s"$baseUrl$route/eu-country")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("value", "HR")
      .check(status.in(200, 303))
      .check(header("Location").is(s"$route/move-date"))

  def getMoveDate =
    http("Get Move Date page")
      .get(s"$baseUrl$route/move-date")
      .header("Cookie", "mdtp=${mdtpCookie}")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def postMoveDate =
    http("Post Move Date")
      .post(s"$baseUrl$route/move-date")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("value.day", s"${LocalDate.now().getDayOfMonth}")
      .formParam("value.month", s"${LocalDate.now().getMonthValue}")
      .formParam("value.year", s"${LocalDate.now().getYear}")
      .check(status.in(200, 303))
      .check(header("Location").is(s"$route/tax-number"))

  def getTaxNumber =
    http("Get Tax Number page")
      .get(s"$baseUrl$route/tax-number")
      .header("Cookie", "mdtp=${mdtpCookie}")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def postTaxNumber =
    http("Post Tax Number")
      .post(s"$baseUrl$route/tax-number")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("value", "HR01234567888")
      .check(status.in(200, 303))
      .check(header("Location").is(s"$route/check-your-answers"))

  def getCheckYourAnswers =
    http("Get Check Your Answers page")
      .get(s"$baseUrl$route/check-your-answers")
      .header("Cookie", "mdtp=${mdtpCookie}")
//      Page still being developed
//      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

}
