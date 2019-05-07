package com.hfp.util.image;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.imageio.ImageIO;

public class ImageUtil {
    /**
     * 改变图片大小、格式
     * @param scale   相对原图的缩放比例
     * @param format    "jpg"
     * @return
     * @throws IOException
     */
    public static OutputStream resizeImage(InputStream inputStream, OutputStream outputStream, int scale, String format) throws IOException {
        BufferedImage prevImage = ImageIO.read(inputStream);
        int newWidth  = (int)(prevImage.getWidth()  * scale);
        int newHeight = (int)(prevImage.getHeight() * scale);
        
        BufferedImage image = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_BGR);
        Graphics graphics = image.createGraphics();
        graphics.drawImage(prevImage, 0, 0, newWidth, newHeight, null);
        ImageIO.write(image, format, outputStream);
        
        outputStream.flush();
        inputStream.close();
        outputStream.close();
        
        return outputStream;
    }
    
    /**
     * 把图片b合成到图片d上,b在d的起始坐标是(x,y)
     * @param b
     * @param d
     * @return
     */
    public static BufferedImage modifyImagetogeter(BufferedImage b, BufferedImage d,int x, int y) {
    	 
        try {
            Graphics2D g = d.createGraphics();
            g.drawImage(b, x, y, b.getWidth(), b.getHeight(), null);
            g.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        return d;
    }
    
    /** 
     * 导入本地图片到缓冲区 
     */
    public static BufferedImage loadImageLocal(String imgName) {
        try {
            return ImageIO.read(new File(imgName));
        } catch (IOException e) {
        	e.printStackTrace();
        }
        return null;
    }
    
    /** 
     * 导入网络图片到缓冲区 
     */
    public static BufferedImage loadImageUrl(String imgName) {
        try {
            URL url = new URL(imgName);
            return ImageIO.read(url);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    public static void writeToFile(BufferedImage image, String file, String format) throws IOException {
        if (!ImageIO.write(image, format, new File(file))) {
            throw new IOException("Could not write an image of format " + format + " to " + file);
        }
    }


    
    public static void main(String[] args) throws Exception{
    	/*
    	FileInputStream fileInputStream = new FileInputStream("G:\\1.jpg");
    	FileOutputStream fileOutputStream = new FileOutputStream("G:\\2.jpg");
    	resizeImage(fileInputStream,fileOutputStream,2,"jpg");
    	fileInputStream.close();
    	fileOutputStream.close();
    	*/
    	
    	// 合成码牌
    	BufferedImage img = QrcodeUtil.toQrcodeBufferedImage("http://www.baidu.com", 175, 175);
    	BufferedImage bg = ImageUtil.loadImageLocal("E:\\tmp\\java-basic-toolkit-package\\src\\main\\resources\\bg.png");
    	BufferedImage ret = ImageUtil.modifyImagetogeter(img, bg, 120, 180);
    	ImageUtil.writeToFile(ret, "E:\\cg.jpg", "jpg");
    }
}
