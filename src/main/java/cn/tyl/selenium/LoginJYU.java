package cn.tyl.selenium;

import cn.tyl.domain.User;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class LoginJYU {
    public  static WebDriver driver = new ChromeDriver();
    public static void main(String[] args) {

        driver.get("http://210.38.163.138/0.htm");

        WebElement username = driver.findElement(By.id("username"));
        WebElement password = driver.findElement(By.id("password"));
        User u = new User();
        write(username,u.getUsername());
        write(password,u.getPassword());

        driver.findElement(By.id("submit")).click();
        quit();




    }
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
    public static void quit() {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.quit();

    }

}
