package com.hfp.util.security;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtil {
	/**
	 * zip 压缩.
	 * 
	 * @param inputByte
	 *            需要解压缩的byte[]数组
	 * @return 压缩后的数据
	 * @throws IOException
	 */
	public static byte[] deflater(final byte[] inputByte) throws IOException {
		
		Deflater compresser = new Deflater();
		compresser.setInput(inputByte);
		compresser.finish();
		
		ByteArrayOutputStream o = new ByteArrayOutputStream(inputByte.length);
		
		byte[] buffer = new byte[1024];
		int compressedDataLength = 0;
		while (!compresser.finished()) {
			compressedDataLength = compresser.deflate(buffer);
			if (compressedDataLength == 0) {
				break;
			}
			o.write(buffer, 0, compressedDataLength);
		}
		o.close();
		compresser.end();
		
		return o.toByteArray();
	}
	
	/**
	 * zip 解压缩.
	 * 
	 * @param inputByte
	 *            byte[]数组类型的数据
	 * @return 解压缩后的数据
	 */
	public static byte[] inflater(final byte[] inputByte) {
		
		Inflater compresser = new Inflater(false);
		compresser.setInput(inputByte, 0, inputByte.length);

		ByteArrayOutputStream o = new ByteArrayOutputStream(inputByte.length);
		try {
			int compressedDataLength = 0;
			byte[] buffer = new byte[1024];
			while (!compresser.finished()) {
				compressedDataLength = compresser.inflate(buffer);
				if (compressedDataLength == 0) {
					break;
				}
				o.write(buffer, 0, compressedDataLength);
			}
		} catch (DataFormatException e) {
			e.printStackTrace();
		} finally {
			try {
				o.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		compresser.end();
		return o.toByteArray();
	}
	
	/**
	 * zip 压缩文件
	 * @param file
	 * @param zos
	 * @throws Exception
	 */
	private static boolean zipFile( File file, ZipOutputStream zos){
        
        FileInputStream fis;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
        System.out.println("file.toString() => " + file.toString()); // E:\tmp\RecvService.java
        System.out.println("file.getName() => " + file.getName()); // RecvService.java
        System.out.println("file.getPath() => " + file.getPath()); // E:\tmp\RecvService.java
        System.out.println("file.getAbsolutePath() => " + file.getAbsolutePath()); // E:\tmp\RecvService.java
        
        try{
	        zos.putNextEntry( new ZipEntry( file.toString() ) );
	        int readedBytes = 0;
	        byte[] buf = new byte[1024];
	        while( ( readedBytes = fis.read(buf) ) > 0 ){
	            zos.write( buf, 0, readedBytes);
	        }
        } catch(Exception e){
        	e.printStackTrace();
        	return false;
        } finally {
        	try {
				zos.closeEntry();
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
            
		}
        
        return true;
    }

	/**
	 * zip 压缩目录
	 * @param dir
	 * @param zos
	 * @throws Exception
	 */
	private static void zipDir( File dir, ZipOutputStream zos)throws Exception{
        if( dir.isDirectory() ) {
            File[] files = dir.listFiles();

            if( files.length == 0 ){         // 目录为空
                zos.putNextEntry( new ZipEntry(dir.toString() + "/") );
                zos.closeEntry();
            } else {
                for( File f: files){
                    if( f.isDirectory() ){
                        zipDir( f, zos);
                    } else{
                        zipFile( f, zos);
                    }
                }
            }
        } else {
        	zipFile( dir, zos);
        }
    }
	
	/**
	 * 把文件或者目录fileName做Zip压缩，压缩后的文件是zipFile
	 * @param fileName
	 * @param zipFile
	 */
	public static void zip(String fileName, String zipFile){
		FileOutputStream fileOutputStream;
		try {
			fileOutputStream = new FileOutputStream(zipFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(fileOutputStream));
		File file = new File(fileName);
	    try {
			zipDir( file, zos);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				zos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	    
	}

    private static boolean unzipFile( ZipInputStream zis) {
        ZipEntry zipEntry;
        FileOutputStream fos = null;

        try {
			while( ( zipEntry = zis.getNextEntry() ) != null){
				File file = new File( zipEntry.getName() );
			    if( zipEntry.isDirectory() ){
			        file.mkdirs();
			    }else{
			        //如果指定文件的目录不存在,则创建之.
			    	File parent = file.getParentFile();
			        if( parent != null && !parent.exists() ){
			            parent.mkdirs();
			        }

			        fos = new FileOutputStream(file);

			        // Scanner sc = new Scanner(zis);   sc.hasNextLine();
			        int readedBytes;
			        byte[] buf = new byte[512];
			        while( ( readedBytes = zis.read(buf) ) > 0){
			            fos.write( buf, 0, readedBytes );
			        }
			        fos.close();
			    }
			    
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(fos!=null) fos.close();
				if(zis!=null) zis.closeEntry();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
        return true;
    }
    
    public static boolean unzip(String zipFile){
    	FileInputStream fis;
		try {
			fis = new FileInputStream(zipFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
    	ZipInputStream zis = new ZipInputStream( new BufferedInputStream(fis) );
        unzipFile( zis);
        try {
			zis.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
        return true;
    }

	
	/**
	 * 读取Zip包中的某个文件
	 * >zip TestZip.java.zip TestZip.java 产生测试用Zip包
	 * @param path
	 * @param fileName
	 * @return
	 */
	public static String readZipFile(String path, String fileName){
        StringBuilder sb = new StringBuilder();
        ZipFile zipfile = null;
		try{
			zipfile = new ZipFile(path);
	        Enumeration<?> e = zipfile.entries();
	        while( e.hasMoreElements() ){
	            ZipEntry entry =  (ZipEntry)e.nextElement();
	            if(fileName.equals(entry.getName())){		            
		            InputStreamReader isr = new InputStreamReader( zipfile.getInputStream( entry ) );
		            BufferedReader br = new BufferedReader(isr);
		            String line = null;
		            while((line=br.readLine())!=null){
		            	sb.append(line);
		            }
		            br.close();
		            break;
	            }
	        }
		} catch (Exception e) {
			e.printStackTrace();
			if(null != zipfile){
				try {
					zipfile.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
        
        return sb.toString();
	}
	
	public static void main(String[] args) throws Exception{
		/*
		String inputString = "我们都是中国人";
        byte[] input = inputString.getBytes("UTF-8");
        System.out.println(HexUtil.toHex(input));
        // e68891e4bbace983bde698afe4b8ade59bbde4baba
        byte[] output = deflater(input);
        System.out.println(HexUtil.toHex(output));
        // 789c011500eaffe68891e4bbace983bde698afe4b8ade59bbde4babaa8cb0f7f
        System.out.println(new String(inflater(output)));
        */
		
		String dirname = "E:\\tmp\\RecvService.java";
        String zipfilename = dirname+".zip";
        // 压缩
        zip( dirname, zipfilename);
        // 解压缩
        unzip( zipfilename );
	}
}
