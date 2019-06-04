package com.hfp.util.io;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;


/**
 * 国际化
 * 创建时间：2019年4月12日 上午11:29:07
 * 文件名称：ResourceBundleUtil.java
 * @author 贺飞平
 * @version 1.0
 *
 */
public class ResourceBundleUtil {

	/**
	 * xml 文件格式
	 * @param baseName
	 * @return
	 */
	public static ResourceBundle getXMLInstance(String baseName){
		return ResourceBundle.getBundle(baseName, new XMLResourceBundleControl());
	}
	
	/**
	 * properties 文件
	 * @param baseName
	 * @return
	 */
	public static ResourceBundle getPropertiesInstance(String baseName){
		return ResourceBundle.getBundle(baseName,Locale.getDefault());
	}
	
	
	public static void main(String args[]) {

		
		System.out.println(Locale.getDefault().toString());  // zh_CN
		ResourceBundle bundle = ResourceBundleUtil.getXMLInstance("Strings");
        System.out.println("Key: " + bundle.getString("Key"));   // Key: zh_CN
        System.out.println("name: " + bundle.getString("name")); // name: 中文姓名

        Locale.setDefault(Locale.US);
		System.out.println(Locale.getDefault().toString());  // en_US
		ResourceBundle bundle2 = ResourceBundleUtil.getXMLInstance("Strings");
        System.out.println("Key: " + bundle2.getString("Key"));   // Key: en_US
        System.out.println("name: " + bundle2.getString("name")); // name: name

		Locale.setDefault(Locale.GERMAN);
		System.out.println(Locale.getDefault().toString());  // de
		ResourceBundle bundle3 = ResourceBundleUtil.getXMLInstance("Strings");
        System.out.println("Key: " + bundle3.getString("Key"));   // Key: defaultKey
        System.out.println("name: " + bundle3.getString("name")); // name: defaultName
        

        // export LANG=zh_CN.utf8  ==> Key: zh_CN
        // export LANG=en_US.utf8  ==> Key: en_US
        // export LANG=zh_TW.utf8  ==> Key: defaultKey
        
        
        /*
java.util.ResourceBundle(rt.jar) 国际化资源文件

读取资源属性文件（properties），根据.properties文件的名称信息（本地化信息），
匹配当前系统的国别语言信息（也可以程序指定），获取相应的properties文件的内容。

properties文件的名字是有规范的：
  一般的命名规范是： 自定义名_语言代码_国别代码.properties
     myres_en_US.properties
     myres_zh_CN.properties
  如果是默认的，直接写为：自定义名.properties
     myres.properties

中文操作系统：
  如果myres_zh_CN.properties、myres.properties两个文件都存在，则优先会使用myres_zh_CN.properties，
  当myres_zh_CN.properties不存在时候，会使用默认的myres.properties。


资源文件都必须是ISO-8859-1编码，对于所有非西方语系的处理，都必须先将之转换为Java Unicode Escape格式。
转换方法是通过JDK自带的工具native2ascii.
>native2ascii -encoding UTF8 myres_zh_CN.properties  tmp
>mv tmp myres_zh_CN.properties

资源文件，放到src的根目录下面（或者放到自己配置的classpath下面)
ResourceBundle.getBundle("myres", Locale.getDefault());
资源文件，放到com.hfp的包目录下面
ResourceBundle.getBundle("com.hfp.myres", Locale.getDefault());
*/

        Locale.setDefault(Locale.CHINA);
		System.out.println(Locale.getDefault().toString());  // zh_CN
		ResourceBundle bundle4 = ResourceBundleUtil.getPropertiesInstance("title");
        System.out.println("username: " + bundle4.getString("username"));   // username: 用户名
        System.out.println("password: " + bundle4.getString("password")); // password: 密码
        
        Locale.setDefault(Locale.US);
        System.out.println(Locale.getDefault().toString());  // zh_CN
		ResourceBundle bundle5 = ResourceBundleUtil.getPropertiesInstance("title");
        System.out.println("username: " + bundle5.getString("username"));   // username: username
        System.out.println("password: " + bundle5.getString("password"));   // password: password
        
		Locale.setDefault(Locale.GERMAN);
		System.out.println(Locale.getDefault().toString());  // zh_CN
		ResourceBundle bundle6 = ResourceBundleUtil.getPropertiesInstance("title");
        System.out.println("username: " + bundle6.getString("username")); // username: defaultUserName
        System.out.println("password: " + bundle6.getString("password")); // password: defalutPassword
    }
}



class XMLResourceBundle extends ResourceBundle {
    private Properties props;

    XMLResourceBundle(InputStream stream) throws IOException {
        this.props = new Properties();
        this.props.loadFromXML(stream);
    }

    protected Object handleGetObject(String key) {
        return props.getProperty(key);
    }

    public Enumeration<String> getKeys() {
        Set<String> handleKeys = props.stringPropertyNames();
        return Collections.enumeration(handleKeys);
    }
}

class XMLResourceBundleControl extends ResourceBundle.Control {
    private static String XML = "xml";

    public List<String> getFormats(String baseName) {
        return Collections.singletonList(XML);
    }

    public ResourceBundle newBundle(String baseName, 
    		Locale locale, 
    		String format, 
    		ClassLoader loader, 
    		boolean reload) 
    	throws IllegalAccessException, InstantiationException, IOException {

        if ( baseName == null || locale == null || format == null || loader == null ) {
            throw new NullPointerException();
        }
        ResourceBundle bundle = null;
        if (!format.equals(XML)) {
            return null;
        }

        String bundleName = toBundleName(baseName, locale);
        String resourceName = toResourceName(bundleName, format);
        System.out.println("resourceName="+resourceName);
        URL url = loader.getResource(resourceName);
        if (url == null) {
            return null;
        }
        URLConnection connection = url.openConnection();
        if (connection == null) {
            return null;
        }
        if (reload) {
            connection.setUseCaches(false);
        }
        InputStream stream = connection.getInputStream();
        if (stream == null) {
            return null;
        }
        BufferedInputStream bis = new BufferedInputStream(stream);
        bundle = new XMLResourceBundle(bis);
        bis.close();

        return bundle;
    }
}



