package com.wikia.webdriver.elements.communities.mobile.components.discussions.common;

import com.google.common.base.Function;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class DeleteDialog extends ConfirmationDialog {

  @FindBy(className = "post-detail")
  private List<WebElement> postList;

  public void confirmAndWait() {
    super.clickConfirm();
    new WebDriverWait(
        driver,
        DiscussionsConstants.TIMEOUT
    ).until((Function<WebDriver, Boolean>) input -> postList.stream()
        .allMatch(p -> p.getAttribute("class").contains("is-deleted")));
  }

  public void cancelAndWait() {
    super.clickCancel();
    new WebDriverWait(
        driver,
        DiscussionsConstants.TIMEOUT
    ).until((Function<WebDriver, Boolean>) input -> postList.stream()
        .anyMatch(p -> !p.getAttribute("class").contains("is-deleted")));
  }
}
