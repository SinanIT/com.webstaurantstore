package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utils.WDriver;

import java.util.List;

public class HomePageSearch {

    public HomePageSearch(){
        PageFactory.initElements(WDriver.getDriver(), this);
    }

    @FindBy(id = "searchval")
    public WebElement searchBox;

    @FindBy(xpath = "//*[@value='Search']")
    public WebElement searchIcon;

    @FindBy(xpath = "//*[@class='rc-pagination-next']")
    public WebElement nextPage;

    @FindBy(xpath = "//a[contains(text(),'View Cart')]")
    public WebElement viewCart;

    @FindBy(xpath = "//*[name()='path' and contains(@d,'M247.244,1')]")
    public WebElement removeItem;

    @FindBy(xpath = "(//span/a)")
    public WebElement lastItemName;

    @FindBy(xpath = "//p[@class='header-1']")
    public WebElement cartHeader;

    @FindBy(xpath = "//*[contains(@data-testid,'itemDescription')]")
    public List<WebElement> resultList;


    @FindBy(xpath = "//li[@class='rc-pagination-item rc-pagination-item-9']")
    public WebElement lastPage;


    @FindBy(xpath = "//button[@aria-label='Submit Feedback' and text()='Add To Cart']")
    public WebElement addToCartFinal;
}
