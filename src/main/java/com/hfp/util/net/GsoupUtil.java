package com.hfp.util.net;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

/**
 * xss非法标签过滤
 * {@link http://www.jianshu.com/p/32abc12a175a?nomobile=yes}
 */
public class GsoupUtil {
	
	
	/**
	 * 使用自带的basicWithImages 白名单
	 * 允许的便签有a,b,blockquote,br,cite,code,dd,dl,dt,em,i,li,ol,p,pre,q,
	 *         small,span,strike,strong,sub,sup,u,ul,img
	 * 以及a标签的href,img标签的src,align,alt,height,width,title属性
	 */
	private static final Whitelist whitelist = Whitelist.basicWithImages();
	//Whitelist.none();  ==> new Whitelist();
	//Whitelist.simpleText();
	//Whitelist.basic();         // 没有图片上传的需求, 使用 basic
	//Whitelist.relaxed();
	
	/** 配置过滤化参数,不对代码进行格式化 (默认在标签及标签内容之间添加了 \n 回车符)*/
	private static final Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);
	static {
		// 富文本编辑时一些样式是使用style来进行实现的
		// 比如红色字体 style="color:red;"
		// 所以需要给所有标签添加style属性
		whitelist.addAttributes(":all", "style");
	}

	/**
	 * xss非法标签过滤  ==> XSS攻击
	 * @param content
	 * @return
	 */
	public static String clean(String content) {
		
		return Jsoup.clean(content, "", whitelist, outputSettings);
	}
	
	public static void main(String[] args){
		String text = "<a href=\"http://www.baidu.com/a\" onclick=\"alert(1);\">sss</a><script>alert(0);</script>sss";
		System.out.println(clean(text));
		
		/*
		String test = "<div class='div'>div标签 </div><span>span标签</span><p class='div' style='width:50px;'>p标签 </p>";
		Whitelist whitelist = new Whitelist();  // 创建一个白名单对象
		whitelist.addTags("p","span");                 // 添加允许使用的标签, 此时只允许 p,span标签存在
		whitelist.addAttributes("p","class","style");   // 将 p 标签中的 class,style 属性加入白名单中
		//whitelist.addAttributes(":all","class","style"); // 将 所有标签中的 class,style 属性加入白名单中
		System.out.println(Jsoup.clean(test, whitelist));                      // 返回内容有换行
		System.out.println(Jsoup.clean(test, "", whitelist, outputSettings));  // 返回内容没有换行
		*/
		
	}
	
}
