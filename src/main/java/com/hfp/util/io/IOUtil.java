package com.hfp.util.io;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;


public class IOUtil {
	public static String toString(InputStream input){
		try {
			return IOUtils.toString(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) throws IOException {

	}
}
