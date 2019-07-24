package cn.tyl.utils;

import cn.tyl.domain.User;
import cn.tyl.utils.SeleniumUtil;
import org.apache.commons.lang3.RandomUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * selenium破解极验滑动验证码
 */
public class BiliLogin {

    public static Logger log4j =  Logger.getLogger(BiliLogin.class);


    private static String BASE_PATH = "";
    //开始遍历处距离左边的距离
    private static final int GEETEST_WIDTH_START_POSTION = 60;

    private static ChromeDriver driver = null;
    //文档截图后图片大小
    private static Point imageFullScreenSize = null;
    //html 大小
    private static Point htmlFullScreenSize = null;

    public static void login(WebDriver oldDriver) throws InterruptedException {


        driver = (ChromeDriver) oldDriver;

        loginNext();

    }

    //    static {
////        System.setProperty("webdriver.chrome.driver", "/Users/wangyang/Downloads/chromedriver");
//        driver = new ChromeDriver();
//    }
    public static void loginNext() throws InterruptedException {


        log4j.info("开始执行loginNext方法");
        try {
            for(int i = 0; i < 1; i++){
                log4j.info("设置窗口大小");
                driver.manage().window().setSize(new Dimension(1855, 937));
                log4j.info("打开登录页面");
                driver.get("https://passport.bilibili.com/login");
                User u = new User();
                WebElement username = driver.findElement(By.id("login-username"));
                WebElement passwd = driver.findElement(By.id("login-passwd"));
                SeleniumUtil.write(username,u.getUsername());
                SeleniumUtil.write(passwd,u.getPassword());
                log4j.info("输入密码，点击登录按钮");
                driver.findElement(By.xpath("//li[@class='btn-box']/a[1]")).click();//找到登录按钮并且点击


                Thread.sleep(3 * 1000);
                Actions actions = new Actions(driver);

                log4j.info("获取验证码图片slider.png");
                //图一
                BufferedImage image = getImageEle(driver.findElement(By.cssSelector("canvas[class='geetest_canvas_bg geetest_absolute']")));
                ImageIO.write(image, "png",  new File(BASE_PATH + "slider.png"));
                //设置原图可见
                driver.executeScript("document.getElementsByClassName(\"geetest_canvas_fullbg\")[0].setAttribute('style', 'display: block')\n");
                //图二
                log4j.info("获取验证码图片original.png");
                image = getImageEle(driver.findElement(By.cssSelector("canvas[class='geetest_canvas_fullbg geetest_fade geetest_absolute']")));
                ImageIO.write(image, "png",  new File(BASE_PATH + "original.png"));
                //隐藏原图
                driver.executeScript("document.getElementsByClassName(\"geetest_canvas_fullbg\")[0].setAttribute('style', 'display: none')\n");
                WebElement element = null;
                element = driver.findElement(By.className("geetest_slider_button"));
                actions.clickAndHold(element).perform();

                int moveDistance = calcMoveDistance();
                log4j.info("计算移动距离"+moveDistance+"，开始移动");
                int d = 0;

                List<MoveEntity> list = getMoveEntity(moveDistance+6);
                log4j.info(list.size());
                for(MoveEntity moveEntity : list){
                    actions.moveByOffset(moveEntity.getX(), moveEntity.getY()).perform();
                    log4j.debug("向右总共移动了:" + (d = d + moveEntity.getX()));
                    Thread.sleep(moveEntity.getSleepTime());
                }
                actions.release(element).perform();
                Thread.sleep(1 * 1000);
            }
            log4j.info("登录完毕");

        } catch (Exception e) {
            log4j.error("loginNext出错了，错误为-----"+e.getMessage());
        } finally {

            Thread.sleep(10000);

        }
    }

    /**
     * 获取element的截图对应的BufferedImage对象
     * @param ele
     * @return
     */
    private static BufferedImage getImageEle(WebElement ele) {
        log4j.info("获取图片");
        try {
            byte[] fullPage = driver.getScreenshotAs(OutputType.BYTES);
            BufferedImage fullImg = ImageIO.read(new ByteArrayInputStream(fullPage));
            log4j.info("fullImage: width:" + fullImg.getWidth() + ", height:" + fullImg.getHeight());
            ImageIO.write(fullImg, "png",  new File(BASE_PATH + "full.png"));
            if (imageFullScreenSize == null){
                imageFullScreenSize = new Point(fullImg.getWidth(), fullImg.getHeight());
            }
            WebElement element = driver.findElement(By.cssSelector("div[id='login-app']"));

            log4j.info("html: width:" + element.getSize().width + ", height:" + element.getSize().height);
            if(htmlFullScreenSize == null){
                htmlFullScreenSize = new Point(1855, 937);
//                htmlFullScreenSize = new Point(element.getSize().getWidth(), element.getSize().getHeight());
            }
            Point point = ele.getLocation();
            int eleWidth = (int)(ele.getSize().getWidth() / (float)element.getSize().width * (float)fullImg.getWidth());
//            int eleWidth = 260;
            int eleHeight = (int) (ele.getSize().getHeight() / (float)element.getSize().height * (float)fullImg.getHeight());
//            int eleHeight = 160;






            //收集数据区域
            int pointx = point.getX();
            float elementWidth = (float)element.getSize().width;
            float fullImgWidth  = (float)fullImg.getWidth();

            log4j.debug("pointX--"+pointx + "--elementWidth--"+elementWidth+"--fullImgWidth--"
                    +fullImgWidth);
            log4j.debug("因此 x坐标为："+(int)point.getX()+"除以"+(float)element.getSize().width +"乘以"+ (float)fullImg.getWidth()+"="+(int)(point.getX() / (float)element.getSize().width * (float)fullImg.getWidth()));




























            log4j.debug("x--"+(int)(point.getX() / (float)element.getSize().width * (float)fullImg.getWidth())+"" +
                    "  y--"+(int)(point.getY() / (float)element.getSize().height * (float)fullImg.getHeight())+"" +
                    "要截取的宽："+eleWidth+" --要截取的高："+eleHeight);

            BufferedImage eleScreenShot = fullImg.getSubimage((int)(point.getX() / (float)element.getSize().width * (float)fullImg.getWidth()), (int)(point.getY() / (float)element.getSize().height * (float)fullImg.getHeight()), eleWidth, eleHeight);
//            BufferedImage eleScreenShot = fullImg.getSubimage(1045,point.getY(),260,160);
            return eleScreenShot;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static List<MoveEntity> getMoveEntity(int distance){
        List<MoveEntity> list = new ArrayList<MoveEntity>();
        int i = 0;
        do {
            MoveEntity moveEntity = new MoveEntity();
            int r = RandomUtils.nextInt(5, 8);
            moveEntity.setX(r);
            moveEntity.setY(RandomUtils.nextInt(0, 1)==1?RandomUtils.nextInt(0, 2):0-RandomUtils.nextInt(0, 2));
            int s = 0;
            if(i/Double.valueOf(distance)>0.05){
                if(i/Double.valueOf(distance)<0.85){
                    s = RandomUtils.nextInt(2, 5);
                }else {
                    s = RandomUtils.nextInt(10, 15);
                }
            }else{
                s = RandomUtils.nextInt(20, 30);
            }
            moveEntity.setSleepTime(s);
            list.add(moveEntity);
            i = i + r;
        } while (i <= distance+5);
        boolean cc= i>distance;
        for (int j = 0; j < Math.abs(distance-i); ) {
            int r = RandomUtils.nextInt(1, 3);
            MoveEntity moveEntity = new MoveEntity();
            moveEntity.setX(cc?-r:r);
            moveEntity.setY(0);
            moveEntity.setSleepTime(RandomUtils.nextInt(100, 200));
            list.add(moveEntity);
            j = j+r;
        }
        return list;
    }

    static class MoveEntity{
        private int x;
        private int y;
        private int sleepTime;//毫秒

        public MoveEntity(){

        }

        public MoveEntity(int x, int y, int sleepTime) {
            this.x = x;
            this.y = y;
            this.sleepTime = sleepTime;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getSleepTime() {
            return sleepTime;
        }

        public void setSleepTime(int sleepTime) {
            this.sleepTime = sleepTime;
        }
    }

    /**
     * 根据original.png和slider.png计算需要移动的距离
     * @return
     */
    private static int calcMoveDistance() {
        log4j.info("开始计算距离");
        //小方块距离左边界距离
        int START_DISTANCE = 6;
        int startWidth = (int)(GEETEST_WIDTH_START_POSTION * (imageFullScreenSize.x + 0.0f)/ htmlFullScreenSize.x);
        START_DISTANCE = (int)(START_DISTANCE * (imageFullScreenSize.x + 0.0f)/ htmlFullScreenSize.x);
        try {
            BufferedImage geetest1 = ImageIO.read(new File(BASE_PATH + "original.png"));
            BufferedImage geetest2 = ImageIO.read(new File(BASE_PATH + "slider.png"));
            for (int i = startWidth; i < geetest1.getWidth(); i++){
                for(int j = 0; j < geetest1.getHeight(); j++){
                    int[] fullRgb = new int[3];
                    fullRgb[0] = (geetest1.getRGB(i, j)  & 0xff0000) >> 16;
                    fullRgb[1] = (geetest1.getRGB(i, j)  & 0xff00) >> 8;
                    fullRgb[2] = (geetest1.getRGB(i, j)  & 0xff);

                    int[] bgRgb = new int[3];
                    bgRgb[0] = (geetest2.getRGB(i, j)  & 0xff0000) >> 16;
                    bgRgb[1] = (geetest2.getRGB(i, j)  & 0xff00) >> 8;
                    bgRgb[2] = (geetest2.getRGB(i, j)  & 0xff);
                    if(difference(fullRgb, bgRgb) > 255){
                        int moveDistance = (int)((i - START_DISTANCE) / ((imageFullScreenSize.x + 0.0f)/ htmlFullScreenSize.x));
                        log4j.debug("需要移动的距离:" + moveDistance);
                        return moveDistance;
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("计算移动距离失败");
    }
    private static int difference(int[] a, int[] b){
        return Math.abs(a[0] - b[0]) + Math.abs(a[1] - b[1]) + Math.abs(a[2] - b[2]);
    }
}
