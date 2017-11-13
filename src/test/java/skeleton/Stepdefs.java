package skeleton;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.RestAssured;
import io.restassured.matcher.ResponseAwareMatcher;
import io.restassured.response.Response;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import org.openqa.selenium.support.ui.WebDriverWait;


import java.util.Arrays;
import java.util.List;


public class Stepdefs {
    WebDriver webDriver;
    String origin;
    String destination;

    @Given("^I have a \"([^\"]*)\" as my url$")
    public void i_have_a_as_my_url(String url) throws Exception {

//        System.setProperty("webdriver.chrome.driver", "chromedriver");

        this.webDriver = new ChromeDriver();
        this.webDriver.manage().window().maximize();
        this.webDriver.get(url);


        WebDriverWait wait = new WebDriverWait(webDriver, 10);
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.className("tactile-searchbox-input")));

    }
    @When("^I invoke it with \"([^\"]*)\" and \"([^\"]*)\" as my parameters$")
    public void i_invoke_it_with_and_as_my_parameters(String origin, String destination) throws Exception {
        this.origin = origin;
        this.destination = destination;
        List<WebElement> inputElements = this.webDriver.findElements(By.className("tactile-searchbox-input"));
        inputElements.get(0).sendKeys(origin);
        inputElements.get(1).sendKeys(destination);
        inputElements.get(1).sendKeys(Keys.RETURN);


    }

    @Then("^the distance and duration are validated against the API$")

    public void the_distance_is() throws Exception {

        WebDriverWait wait1 = new WebDriverWait(webDriver, 10);
        WebElement element1 = wait1.until(ExpectedConditions.elementToBeClickable(By.className("section-directions-trip-numbers")));

        List<WebElement> ele = this.webDriver.findElements(By.className("section-directions-trip-numbers"));

        String goog_dur_dist[] = ele.get(0).getText().split("\n");
        String goog_dur = goog_dur_dist[0].toString();
        String goog_dist = goog_dur_dist[1].toString().replace("miles","mi");
        System.out.println(goog_dur_dist[0]);
        System.out.println(goog_dur_dist[1]);



        String restAPIUrl = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins="+origin+"&destinations="+destination+"&key=AIzaSyD-doMVCCV2UAWKYtJ8JWnA-eAUb-dK7QY";
        RestAssured.urlEncodingEnabled = true;
        Response response =  RestAssured.get(restAPIUrl);
        System.out.println(response.getBody().prettyPrint());
        response.then().body("rows.elements.distance.text", Matchers.hasItem(Arrays.asList(goog_dist)));
        this.webDriver.close();

    }

}
