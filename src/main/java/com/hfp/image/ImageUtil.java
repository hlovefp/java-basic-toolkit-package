package com.hfp.image;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
    
    public static void main(String[] args) throws Exception{
    	FileInputStream fileInputStream = new FileInputStream("G:\\1.jpg");
    	FileOutputStream fileOutputStream = new FileOutputStream("G:\\2.jpg");
    	
    	resizeImage(fileInputStream,fileOutputStream,2,"jpg");
    	
    	fileInputStream.close();
    	fileOutputStream.close();
    }
}
