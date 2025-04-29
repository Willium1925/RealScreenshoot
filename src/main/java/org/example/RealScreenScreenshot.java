package org.example;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RealScreenScreenshot implements NativeKeyListener {

    private static final int SCREENSHOT_KEY = NativeKeyEvent.VC_9; // 對應數字鍵 9

    public static void main(String[] args) {
        // 停用 JNativeHook 的日誌輸出，避免控制台 OSD 過多
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.WARNING);
        logger.setUseParentHandlers(false);

        try {
            // 註冊全域熱鍵監聽器
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("註冊全域熱鍵時發生問題: " + ex.getMessage());
            System.exit(1);
        }

        // 創建並加入鍵盤事件監聽器
        GlobalScreen.addNativeKeyListener(new RealScreenScreenshot());

        System.out.println("全螢幕截圖程式已啟動，請按下數字鍵 '9' 進行截圖...");
        System.out.println("按下 Ctrl+C 終止程式。");
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        // 監聽數字鍵 '9' 的按下事件
        if (e.getKeyCode() == SCREENSHOT_KEY) {
            System.out.println("偵測到按下數字鍵 '9'，正在截圖...");
            captureScreenshot();
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        // 不做任何處理
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {
        // 不做任何處理
    }

    private void captureScreenshot() {
        try {
            // 獲取螢幕尺寸
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Rectangle screenRectangle = new Rectangle(screenSize);

            // 創建 Robot 物件用於截圖
            Robot robot = new Robot();
            BufferedImage image = robot.createScreenCapture(screenRectangle);

            // 生成帶有時間戳記的檔案名稱
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String filename = "screenshot_" + timestamp + ".png";
            File outputFile = new File(filename);

            // 儲存為 PNG 檔案 (無損格式)
            ImageIO.write(image, "PNG", outputFile);

            System.out.println("截圖已儲存為: " + outputFile.getAbsolutePath());

        } catch (AWTException | IOException ex) {
            System.err.println("截圖時發生錯誤: " + ex.getMessage());
        }
    }
}