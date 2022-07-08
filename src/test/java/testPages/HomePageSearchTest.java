package testPages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.HomePageSearch;
import utils.ConfigReader;
import utils.GenMethods;
import utils.WDriver;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class HomePageSearchTest extends HomePageSearch {
    List<WebElement> results;
    List<String> searchResult;

    // This method will execute before every single test
    @BeforeTest
    public void setUp() {
        WDriver.getDriver().get(ConfigReader.getProperty("url"));
        searchBox.sendKeys("stainless work table");
        searchIcon.click();
    }

    @Test
    public void searchProductsTest() {
        results = resultList;
        //adding first page search results page to searchResult list
        searchResult = new LinkedList<>(); // Since I will update searchResult data, it is better use LinkedList collection here.
        resultList.stream().forEach(t -> searchResult.add(t.getText()));

        //updating searchResult list adding by other pages search results
        for (int i = 0; i < 8; i++) {
            GenMethods.waitForClickablility(nextPage, 3);
            nextPage.click();
            //I put web element here to avoid StaleElementReferenceException by refreshing it.
            results = resultList;
            resultList.stream().forEach(t -> searchResult.add(t.getText()));
        }

        //Size of test result pages (There are 9 result pages after search, every page has 60 results. My expected searchResult size is 540 )
        int actualResultSize = searchResult.size();
        int expectedResultSize = 540;
        Assert.assertEquals(expectedResultSize, actualResultSize);

        // Check the search result ensuring every product has the word 'Table' in its title.
        // List of products those contain "Table" word in their titles
        List<String> listWithTableWord = searchResult.stream().filter(t -> t.contains("Table")).collect(Collectors.toList());
        if (listWithTableWord.size() == expectedResultSize) {
            Assert.assertTrue(true, "Every product title has 'Table' sequence in their title");
        } else {
            Assert.assertFalse(false, "Some products do not have 'Table' in their title ");

            // List of products those NOT contain "Table" word in their titles
            List<String> listWithoutTableWord = searchResult.stream().filter(t -> !(t.contains("Table"))).collect(Collectors.toList());
            System.out.println(listWithTableWord.size() + " products has 'Table' sequence in their title.\n" +
                    listWithoutTableWord.size() + " products do not have 'Table' sequence in their title.");

            // print out no 'Table' word products list
            for (String a : listWithoutTableWord) {
                System.out.print("Product list without 'Table' word : " + a.toString() + "\n");
            }
        }
        // print out contains 'Table' word products list
        for (String a : searchResult) {
            System.out.print("Product list with 'Table' word : " + a.toString() + "\n");
        }
    }

    @Test
    public void addProductToCart() {
        results = resultList;
        List<String> lastPageResults = new ArrayList<>();// Since I will retrieve the data it is better use here ArrayList collection.
        GenMethods.scrollDown(5000);
        GenMethods.waitForClickablility(lastPage, 4);
        lastPage.click();
        WDriver.getDriver().navigate().refresh();

        //I put web element here to avoid StaleElementReferenceException by refreshing it.
        results.stream().forEach(t -> lastPageResults.add(t.getText()));

        //Get last product to name
        WebElement lastElement = WDriver.getDriver().findElement(By.xpath("(//*[contains(@data-testid,'itemDescription')])[" + (lastPageResults.size() + "]")));
        String lastElementName = lastElement.getText();
        System.out.println("Last product name of search results: " + lastElementName);

        // Add last product to cart
        WebElement addCart = WDriver.getDriver().findElement(By.xpath("(//input[@data-action='addToCart'])[" + (lastPageResults.size() + "]")));
        addCart.click();
        addToCartFinal.click();

        //View cart
        viewCart.click();

        //Verifying Product added
        Assert.assertEquals(lastItemName.getAttribute("title"), lastElementName);

        //Remove product from Cart
        removeItem.click();

        //Verifying cart is empty after remove the item
        Assert.assertEquals(cartHeader.getText(), "Your cart is empty.", "No product in cart");
    }

    //close driver after executed every single test
    @AfterTest
    public void closeBrowser() {
        WDriver.closeDriver();
    }
}