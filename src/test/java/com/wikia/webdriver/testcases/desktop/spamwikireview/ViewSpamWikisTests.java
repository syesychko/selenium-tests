package com.wikia.webdriver.testcases.desktop.spamwikireview;

import com.wikia.webdriver.common.core.Assertion;
import com.wikia.webdriver.common.core.annotations.Execute;
import com.wikia.webdriver.common.core.helpers.User;
import com.wikia.webdriver.common.templates.NewTestTemplate;
import com.wikia.webdriver.elements.communities.desktop.pages.SpamWikiReviewPage;

import org.apache.commons.lang3.EnumUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

@Test(groups = "SpamWikiReview")
public class ViewSpamWikisTests extends NewTestTemplate {

  /**
   * Tests if a normal user correctly doesn't have access to Spam Wiki Review
   */
  @Test
  @Execute(asUser = User.USER)
  public void authorizationFailSpamWikiReviewTest() {
    SpamWikiReviewPage spamWikiReviewPage = new SpamWikiReviewPage();
    spamWikiReviewPage.open();

    Assertion.assertTrue(spamWikiReviewPage.getRawVisibleBodyText()
                             .matches(".*(User not authorized|Unauthorized).*"),
                         "No 'unauthorized' type message was displayed to the user");

    Assertion.assertTrue(spamWikiReviewPage.getListDisplayedWikisTableRows().isEmpty(),
                         "It shouldn't be possible to see Wikis' list when unauthorized");
  }

  @Test
  @Execute(asUser = User.STAFF)
  public void filterJapaneseLanguageTest() {
    SpamWikiReviewPage spamWikiReviewPage = new SpamWikiReviewPage();
    spamWikiReviewPage.open();

    String spamWikiReviewListViewUrl = spamWikiReviewPage.getCurrentUrl();

    // select 'ja' language
    spamWikiReviewPage.selectLanguageOfWikis(SpamWikiReviewPage.LANGUAGE_CODE.ja);


    // assert current page contains query param selecting the language
    Assertion.assertEquals(spamWikiReviewPage.getCurrentUrl(),
                           spamWikiReviewListViewUrl + SpamWikiReviewPage.QUERY_PARAM_LANG
                           + SpamWikiReviewPage.LANGUAGE_CODE.ja);

    // check if all Wikis on a presumably filtered page have the 'ja' language
    for(WebElement wikiRow : spamWikiReviewPage.getListDisplayedWikisTableRows()) {
      Assertion.assertEquals(wikiRow.findElement(By.xpath(SpamWikiReviewPage.XPATH_LANGUAGE_COLUMN))
                                 .getText(), "ja");
    }

  }


  @Test
  @Execute(asUser = User.STAFF)
  public void filterAllLanguagesTest() {
    SpamWikiReviewPage spamWikiReviewPage = new SpamWikiReviewPage();
    spamWikiReviewPage.open();

    String spamWikiReviewListViewUrl = spamWikiReviewPage.getCurrentUrl();

    // select all languages
    spamWikiReviewPage.selectLanguageOfWikis(SpamWikiReviewPage.LANGUAGE_CODE.all);


    // assert current page contains query param selecting the language ALL
    Assertion.assertEquals(spamWikiReviewPage.getCurrentUrl(),
                           spamWikiReviewListViewUrl + SpamWikiReviewPage.QUERY_PARAM_LANG
                           + SpamWikiReviewPage.LANGUAGE_CODE.all);
  }


  @Test
  @Execute(asUser = User.STAFF)
  public void filterOtherLanguagesTest() {
    SpamWikiReviewPage spamWikiReviewPage = new SpamWikiReviewPage();
    spamWikiReviewPage.open();

    String spamWikiReviewListViewUrl = spamWikiReviewPage.getCurrentUrl();

    // select 'ja' language
    spamWikiReviewPage.selectLanguageOfWikis(SpamWikiReviewPage.LANGUAGE_CODE.other);


    // assert current page url contains query param selecting the language
    Assertion.assertEquals(spamWikiReviewPage.getCurrentUrl(),
                           spamWikiReviewListViewUrl + SpamWikiReviewPage.QUERY_PARAM_LANG
                           + SpamWikiReviewPage.LANGUAGE_CODE.other);

    // check if all Wikis on a presumably filtered page have languages other than those in LANGUAGE_CODE
    for(WebElement wikiRow : spamWikiReviewPage.getListDisplayedWikisTableRows()) {
      Assertion.assertFalse(EnumUtils.isValidEnum(SpamWikiReviewPage.LANGUAGE_CODE.class,
                                                 wikiRow.findElement(By.xpath(SpamWikiReviewPage.XPATH_LANGUAGE_COLUMN))
                                                     .getText()),
                           "Spam Wiki Review other languages view contained languages from"
                           + "LANGUAGE CODE - those that can be filtered individually, "
                           + "namely " + wikiRow.findElement(By.xpath(SpamWikiReviewPage.XPATH_LANGUAGE_COLUMN))
                               .getText());
    }
  }

  @Test
  @Execute(asUser = User.STAFF)
  public void ShowOnlyQuestionableWikisByNavigationTest() {
    SpamWikiReviewPage spamWikiReviewPage = new SpamWikiReviewPage();
    spamWikiReviewPage.open();

    // navigate to Questionable Wikis page
    spamWikiReviewPage.showQuestionableWikis();

    // assert current page url contains query param selecting the questionable Wikis
    Assertion.assertStringContains(spamWikiReviewPage.getCurrentUrl(), "status=questionable");

    // check if all Wikis on a presumably only questionable wikis page
    for(WebElement wikiRow : spamWikiReviewPage.getListDisplayedWikisTableRows()) {
      String visibleButtonsText = wikiRow.findElement(By.xpath(SpamWikiReviewPage.XPATH_STATUS_COLUMN)).getText();

      //have Spam and Not Spam buttons in that order without Questionable button

      Assertion.assertStringNotContains(visibleButtonsText,
                                        SpamWikiReviewPage.QUESTIONABLE_BUTTON_TEXT);

      Assertion.assertEquals(visibleButtonsText,SpamWikiReviewPage.SPAM_BUTTON_TEXT
                                                + "\n" + SpamWikiReviewPage.NOT_SPAM_BUTTON_TEXT);
    }



  }

  @Test
  @Execute(asUser = User.STAFF)
  public void SpamWikiReviewHeaderNavigatesToMainPageTest() {
    // TODO: Click on Spam Wiki Review in header and see if it redirects to main page
  }

  @Test
  @Execute(asUser = User.STAFF)
  public void NavigateToAddQuestionableWikiTest() {
    // TODO: Click on show questionable wikis button and see if all wikis displayed are questionable
  }


}