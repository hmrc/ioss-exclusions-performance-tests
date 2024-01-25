/*
 * Copyright 2024 HM Revenue & Customs
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

import uk.gov.hmrc.performance.simulation.PerformanceTestRunner
import uk.gov.hmrc.perftests.exclusions.ExclusionsRequests._

class ExclusionsSimulation extends PerformanceTestRunner {

  setup("exclusionsMoveCountry", "Exclusions - Move Country Journey") withRequests
    (
      getAuthorityWizard,
      postAuthorityWizard,
      getMoveCountry,
      postMoveCountry(true),
      getEuCountry,
      postEuCountry,
      getMoveDate,
      postMoveDate,
      getTaxNumber,
      postTaxNumber,
      getCheckYourAnswers,
      postCheckYourAnswers,
      getSuccessful
    )

  setup("exclusionsStoppedSellingGoods", "Exclusions - Stopped Selling Eligible Goods Journey") withRequests
    (
      getAuthorityWizard,
      postAuthorityWizard,
      getMoveCountry,
      postMoveCountry(false),
      getStoppedSellingGoods,
      postStoppedSellingGoods(true),
      getStoppedSellingGoodsDate,
      postStoppedSellingGoodsDate,
      getSuccessful
    )

  setup("exclusionsVoluntary", "Exclusions - Voluntary Journey") withRequests
    (
      getAuthorityWizard,
      postAuthorityWizard,
      getMoveCountry,
      postMoveCountry(false),
      getStoppedSellingGoods,
      postStoppedSellingGoods(false),
      getLeaveScheme,
      postLeaveScheme,
      getStoppedUsingServiceDate,
      postStoppedUsingServiceDate,
      getSuccessful
    )

  runSimulation()
}
