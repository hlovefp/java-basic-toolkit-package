package com.hfp.util.image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import javax.imageio.ImageIO;
import java.util.Hashtable;
import com.google.zxing.common.BitMatrix;
import com.hfp.util.security.BASE64Util;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;

/*
 * 生成二维码图片
 */
public class QrcodeUtil {

    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;
    
    private int width     = 90;
    private int height    = 90;
    private String text;
    private BitMatrix bitMatrix = null;
    
    private BitMatrix toBitMatrix() throws WriterException{
        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");      // 内容所使用字符集编码
        return new MultiFormatWriter().encode(
					this.text, BarcodeFormat.QR_CODE, this.width, this.height, hints);
    }
    
	/**
	 *
	 * @param
	 *  text   二维码内容
	 *	width  生成二维码图片宽度
	 *	height 生成二维码图片高度
	 *	format 生成二维码图片格式,jpg,png
	 * @return
	 * @throws WriterException 
	 */
	private void setQrcode(String text, int width, int height) throws WriterException{
		this.text   = text;
		this.width  = width;
		this.height = height;
		this.bitMatrix = toBitMatrix();
	}

    private BufferedImage toBufferedImage() {
        int width  = this.bitMatrix.getWidth();
        int height = this.bitMatrix.getHeight();
        BufferedImage image = new BufferedImage( width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB( x, y, this.bitMatrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }

    private void writeToFile(File file, String format) throws IOException {
        BufferedImage image = toBufferedImage();
        if (!ImageIO.write(image, format, file)) {
            throw new IOException("Could not write an image of format " + format + " to " + file);
        }
    }

    private void writeToStream(OutputStream stream, String format) throws IOException {
        BufferedImage image = toBufferedImage();
        if (!ImageIO.write(image, format, stream)) {
            throw new IOException("Could not write an image of format " + format);
        }
    }
    
    private static QrcodeUtil newInstance(){
    	return new QrcodeUtil();
    }
    
    public static BufferedImage toQrcodeBufferedImage(String text, int width, int height) throws WriterException, IOException {
    	QrcodeUtil qrcode = QrcodeUtil.newInstance();
    	qrcode.setQrcode(text, width, height);
    	return qrcode.toBufferedImage();
    }
    
    public static String toQrcodeString(String text, int width, int height, String format) throws WriterException, IOException {
    	QrcodeUtil qrcode = QrcodeUtil.newInstance();
    	qrcode.setQrcode(text, width, height);
    	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    	qrcode.writeToStream(outputStream, format);
    	return new String(BASE64Util.encode(outputStream.toByteArray()));
    }

    public static void toQrcodeFile(String text, File outputFile, int width, int height,String format) throws IOException, WriterException{
    	QrcodeUtil qrcode = QrcodeUtil.newInstance();
    	qrcode.setQrcode(text, width, height);
        qrcode.writeToFile(outputFile, format);   // 生成二维码图片文件
    }

    public static void main(String[] args) throws Exception {
        //String text = "http://www.baidu.com";         // 二维码内容
        
        //File outputFile = new File("d:" + File.separator + "new.jpg");
        //QrcodeUtil.toQrcodeFile(text, outputFile, 90, 90, "jpg");

        //String img = QrcodeUtil.toQrcodeString(text, 90, 90, "jpg");
    	//System.out.println("<img src=\"date:image/jpg;base64,"+img+"\">");
    	
    	// 合成码牌
    	BufferedImage img = QrcodeUtil.toQrcodeBufferedImage("http://www.baidu.com", 175, 175);
    	BufferedImage bg = ImageUtil.loadImageLocal("E:\\tmp\\java-basic-toolkit-package\\src\\main\\resources\\bg.png");
    	BufferedImage ret = ImageUtil.modifyImagetogeter(img, bg, 120, 180);
    	ImageUtil.writeToFile(ret, "E:\\cg.jpg", "jpg");
    }

}
