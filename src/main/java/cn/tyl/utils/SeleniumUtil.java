package cn.tyl.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class SeleniumUtil {

//    public WebDriver driver = null;

    /**
     * 用于清空输入框，并且往里输入东西
     *
     */
    public static void write(WebElement element, String s){
        element.clear();
        element.sendKeys(s);


    }

    /**
     * 用于设置让浏览器等待10秒后再关闭
     * @throws InterruptedException
     */
    public static void quit(WebDriver driver) {

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.quit();

    }



}
