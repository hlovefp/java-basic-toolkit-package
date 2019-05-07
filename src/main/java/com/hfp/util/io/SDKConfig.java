package com.hfp.util.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.hfp.util.common.LogUtil;
import com.hfp.util.common.StringUtil;

public class SDKConfig {
	public static final String FILE_NAME = "acp_sdk.properties";
	public static final String SDK_FRONT_URL = "acpsdk.frontTransUrl";
	public static final String SDK_BACK_URL = "acpsdk.backTransUrl";
	public static final String SDK_ROOTCERT_PATH = "acpsdk.rootCert.path";

	private static SDKConfig config = new SDKConfig();
	public static SDKConfig getConfig() {
		return config;
	}
	
	private String frontTransUrl;
	private String backTransUrl;
	private String rootCertPath;
	
	private SDKConfig() {
		loadPropertiesFromSrc();
	}

	public void loadPropertiesFromPath(String rootPath) {
		if (StringUtil.isNotBlank(rootPath)) {
			//LogUtil.writeLog("从路径读取配置文件: " + rootPath+File.separator+FILE_NAME);
			File file = new File(rootPath + File.separator + FILE_NAME);
			InputStream in = null;
			Properties properties = null;
			if (file.exists()) {
				try {
					in = new FileInputStream(file);
					properties = new Properties();
					properties.load(in);
					loadProperties(properties);
				} catch (FileNotFoundException e) {
					LogUtil.writeErrorLog(e.getMessage(), e);
				} catch (IOException e) {
					LogUtil.writeErrorLog(e.getMessage(), e);
				} finally {
					if (null != in) {
						try {
							in.close();
						} catch (IOException e) {
							LogUtil.writeErrorLog(e.getMessage(), e);
						}
					}
				}
			} else {
				// 由于此时可能还没有完成LOG的加载，因此采用标准输出来打印日志信息
				//LogUtil.writeErrorLog(rootPath + FILE_NAME + "不存在,加载参数失败");
			}
		} else {
			loadPropertiesFromSrc();
		}

	}

	/**
	 * 从classpath路径下加载配置参数
	 */
	public void loadPropertiesFromSrc() {
		InputStream in = null;
		Properties properties = null;
		try {
			//LogUtil.writeLog("从classpath: " +SDKConfig.class.getClassLoader().getResource("").getPath()+" 获取属性文件"+FILE_NAME);
			in = SDKConfig.class.getClassLoader().getResourceAsStream(FILE_NAME);
			if (null != in) {
				properties = new Properties();
				try {
					properties.load(in);
				} catch (IOException e) {
					throw e;
				}
			} else {
				//LogUtil.writeErrorLog(FILE_NAME + "属性文件未能在classpath指定的目录下 "+SDKConfig.class.getClassLoader().getResource("").getPath()+" 找到!");
				return;
			}
			loadProperties(properties);
		} catch (IOException e) {
			LogUtil.writeErrorLog(e.getMessage(), e);
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					LogUtil.writeErrorLog(e.getMessage(), e);
				}
			}
		}
	}

	/**
	 * 根据传入的 {@link #load(java.util.Properties)}对象设置配置参数
	 * 
	 * @param pro
	 */
	public void loadProperties(Properties pro) {
		LogUtil.writeLog("开始从属性文件中加载配置项");
		String value = null;
		
		value = pro.getProperty(SDK_ROOTCERT_PATH);
		if (!isEmpty(value)) {
			this.rootCertPath = value.trim();
		}
		
		value = pro.getProperty(SDK_BACK_URL);
		if (!isEmpty(value)) {
			this.backTransUrl = value.trim();
		}
		
		value = pro.getProperty(SDK_FRONT_URL);
		if (!isEmpty(value)) {
			this.frontTransUrl = value.trim();
		}
	}

	public String getFrontTransUrl() {
		return frontTransUrl;
	}

	public void setFrontTransUrl(String frontTransUrl) {
		this.frontTransUrl = frontTransUrl;
	}

	public String getBackTransUrl() {
		return backTransUrl;
	}

	public void setBackTransUrl(String backTransUrl) {
		this.backTransUrl = backTransUrl;
	}

	public String getRootCertPath() {
		return rootCertPath;
	}

	public void setRootCertPath(String rootCertPath) {
		this.rootCertPath = rootCertPath;
	}
	
	public static boolean isEmpty(String s) {
		return null == s || "".equals(s.trim());
	}
	
	public static void main(String[] args){
		System.out.println(SDKConfig.getConfig().getRootCertPath());
		System.out.println(SDKConfig.getConfig().getFrontTransUrl());
		System.out.println(SDKConfig.getConfig().getBackTransUrl());
	}
}
