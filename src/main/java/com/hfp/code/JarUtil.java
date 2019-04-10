package com.hfp.code;

import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.JarURLConnection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarUtil {
	/*
java.net.JarURLConnection 连接到本地或远程的JAR文件或JAR文件中条目
JAR URL 的语法为： 
 jar:<url>!/{entry}  !/ 被视为分隔符
 Jar 条目 
   jar:http://www.foo.com/bar/baz.jar!/com/foo/Quux.class 
 Jar 文件 
   jar:http://www.foo.com/bar/baz.jar!/
   jar:file:/home/duke/duke.jar!/
 Jar 目录 
   jar:http://www.foo.com/bar/baz.jar!/com/foo/ 
	 */
	public static String read(String path, String fileName){
		StringBuilder sb = new StringBuilder();
		try{
			URL url = new URL(path);
	        JarURLConnection jarConnection = (JarURLConnection)url.openConnection();
	        
	        /*
	        Attributes att = jarConnection.getMainAttributes();
	        System.out.println("MANIFEST_VERSION: "+att.getValue(Attributes.Name.MANIFEST_VERSION));
	
	        Manifest manifest = jarConnection.getManifest();
	        Attributes attr = manifest.getMainAttributes();
	        System.out.println("MANIFEST_VERSION: "+attr.getValue(Attributes.Name.MANIFEST_VERSION));
	        */
	        
	        JarFile jarFile =  jarConnection.getJarFile();
	        Enumeration<JarEntry> entries = jarFile.entries();
	        while(entries.hasMoreElements()){
	        	JarEntry entry = entries.nextElement();
	        	//System.out.println(entry.getName());
	        	if(entry.getName().equals(fileName)){
	        		InputStreamReader isr = new InputStreamReader( jarFile.getInputStream( entry ) );
		            BufferedReader br = new BufferedReader(isr);
		            String line = null;
		            while((line=br.readLine())!=null){
		            	sb.append(line).append("\n");
		            }
		            
		            br.close();
		            break;
	        	}
	        }
	        jarFile.close();
	        
	        //Attributes getAttributes() 如果此连接的 URL 指向 JAR 文件条目，则返回其 Attribute 对象；否则返回 null。 
	        //Certificate[] getCertificates()  如果此连接的 URL 指向 JAR 文件条目，则返回其 Certificate 对象；否则返回 null。 
	        //String getEntryName()  返回此连接的条目名称。 
	        //JarEntry getJarEntry()  返回此连接的 JAR 条目对象（如果有）。 
		}catch(Exception e){
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	public static void main(String[] args){
		System.out.println(read("jar:file:/home/dt.jar!/", "META-INF/MANIFEST.MF"));
	}
}
