package cn.tyl.utils;

import cn.tyl.dao.FavlistDAO;
import cn.tyl.dao.impl.FavlistDAOImpl;
import cn.tyl.domain.Favlist;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BiliUtils {

    public static Logger log4j =  Logger.getLogger(BiliUtils.class);


/**
     * 用于判断是否登录bilibili
     * 原理是：如果已经登录bilibili，再打开https://passport.bilibili.com/login 会自动跳转到 https://www.bilibili.com/
     * 只要打开先打开登录页面，稍等3秒，若页面链接变成首页，则判断为登录成功
     *driver.getCurrentUrl()获取当前链接
     *
     * @param driver
     * @return
     */

    //因为需要调用自己，所以留下个变量控制重试次数
    public static int reCount = 0 ;
    public static void login(WebDriver driver){

        log4j.info("准备进行登录操作");
        driver.get("https://passport.bilibili.com/login");
        String currentUrl ="";
        try {
            Thread.sleep(3000);
            currentUrl = driver.getCurrentUrl();




        } catch (InterruptedException e) {
            log4j.error("出错了，错误为-----"+e.getMessage());
        }

        boolean result= "https://www.bilibili.com/".equals(currentUrl);

        if (!result){
            //未登录
            try {
                log4j.info("未登录，调用 BiliLogin.login进行登录");
                BiliLogin.login(driver);
                Thread.sleep(5000);
                if(reCount<3){
                    reCount++;
                    log4j.info("重试登录，第"+reCount+"次重试");
                    BiliUtils.login(driver);



                }else
                    log4j.info("重试次数已经达到3次，仍然未登录成功");
                    return;


            } catch (InterruptedException e) {
                log4j.error("登录失败，错误为-----"+e.getMessage());
            }

        }else {
            log4j.info("已登录");
        }
    }
}