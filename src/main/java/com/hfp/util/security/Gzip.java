package com.hfp.util.security;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;



public class Gzip{
	
	/**
	 * gzip 压缩
	 * @param datas
	 * @return
	 */
	public static byte[] gzip(byte[] datas) {
		if(datas==null)
			return null;

		byte[] gzipByte = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();  
        GZIPOutputStream gzip=null;  
        try {  
            gzip = new GZIPOutputStream(bos); 
            gzip.write(datas);
            gzip.close();
            gzipByte = bos.toByteArray();  
        } catch (Exception ex) {  
            ex.printStackTrace();  
        } finally{
            try {
            	if(gzip != null) gzip.close();
            } catch (IOException e) {
            	e.printStackTrace();
            }
        }
        return gzipByte;  
    }

    public static byte[] ungzip(byte[] datas){
    	byte[] ret = null;
        ByteArrayInputStream bis = new ByteArrayInputStream(datas);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();  

        GZIPInputStream ungzip = null;
        try{
        	ungzip = new GZIPInputStream(bis);
	        byte[] buf = new byte[1024];  
	        int num = 0;
	        while ((num = ungzip.read(buf)) > -1) {
	            bos.write(buf, 0, num);    
	        }
	        ungzip.close();
	        ret = bos.toByteArray();
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	try{
        		if( ungzip!=null) ungzip.close();
        	}catch(Exception e){
        		e.printStackTrace();
        	}
        }
        
        return ret;
    }
    
    public static boolean gzipFile(String file, String gzipfile){
    	FileOutputStream fos=null;
		try {
			fos = new FileOutputStream( gzipfile );
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		
    	GZIPOutputStream gzip=null;
		try {
			gzip = new GZIPOutputStream(new BufferedOutputStream(fos));
		} catch (IOException e) {
			e.printStackTrace();
			try {
				if(fos!=null) fos.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return false;
		}    	
    	
		FileInputStream fis;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			try {
				gzip.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return false;
		}

        BufferedInputStream bis = new BufferedInputStream(fis);

        try{
            byte[] buffer = new byte[1024];
            int n;
	        while( (n=bis.read(buffer)) > -1 ){
	            gzip.write( buffer, 0, n);
	        }
        }catch(Exception e){
        	e.printStackTrace();
        	
        	return false;
        }finally {
        	try {
				gzip.close();
				bis.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

    	return true;
    }
    
    public static boolean ungzipFile(String gzipfile, String ungzipfile){
    	
    	GZIPInputStream ungzip=null;
    	FileInputStream fis=null;
		try {
			fis = new FileInputStream( gzipfile );
			ungzip = new GZIPInputStream( new BufferedInputStream( fis ) );
		} catch (Exception e) {
			e.printStackTrace();
			try{
				if(fis!=null) fis.close();
				if(ungzip!=null)ungzip.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return false;
		}

		FileOutputStream fos;
		try {
			fos = new FileOutputStream(ungzipfile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			try{
				if(ungzip!=null)ungzip.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return false;
		}
    	BufferedOutputStream bos = new BufferedOutputStream(fos);
		byte[] buffer = new byte[256];
		int n;
		try{
		while( (n = ungzip.read(buffer)) > -1 ) {
		    bos.write( buffer, 0, n);
		}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}finally {
			try{
				bos.close();
				ungzip.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return true;
    }


    public static void main(String[] args)throws Exception{
    	/*
        // 或者是从文件中读取数据
        String str = "%5B%7B%22lastUpdateTime%22%3A%222011-10-28+9%3A39%3A41%22%2C%22smsList%22%3A%5B%7B%22liveState%22%3A%221";
        System.out.println("Source: \n" + HexUtil.toHex(str.getBytes()));
        byte[] compressBytes = gzip(str.getBytes());
        System.out.println("Compress: \n" + HexUtil.toHex(compressBytes));
        // 1f8b0800000000000000533575523577523532ca492c2e092d48492c490dc9cc4d050aa81a3b0249230343435d43035d230b6d4ba0883188303104491b3903c9e2dc629fcce212a872b8599965a9c12540a3e0c6180200f8f0cc1c68000000
        byte[] uncompressBytes = ungzip(compressBytes);
        System.out.println("Uncompress: \n" +  new String(uncompressBytes));
        */
    	
    	String file = "E:\\tmp\\RecvService.java";
        String gzipfile = file+".gz";
        String ungzipfile = gzipfile+".ungz";
        gzipFile(file, gzipfile);
        ungzipFile(gzipfile, ungzipfile);
    }
}
