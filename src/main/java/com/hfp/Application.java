package com.hfp;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;

import com.hfp.db.MongodbUtil;
import com.hfp.db.RedisUtil;
import com.hfp.util.io.IOUtil;

@EnableCaching          // 开启缓存功能
@SpringBootApplication
public class Application {
	
	public static void main(String[] args) throws IOException {
		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);

		//testMongoDB(context);
		//testRedis(context);
	}
	
	public static void testMongoDB(ConfigurableApplicationContext context){
		// 测试连接MongoDB存储数据  ==> MongodbUtil.java
		// 配置文件spring.data.mongodb...
		MongodbUtil mongodbUtil = context.getBean(MongodbUtil.class);
		//mongodbUtil.insert(new Department(new Integer(20),"hfp"));
		mongodbUtil.deleteById(new Integer(20));
		
		//mongo
		//>show dbs   // 显示数据库名词列表
		//>db         // 当前数据名
		//>use 数据库名   // 切换数据库,登陆mongo默认使用test数据库
		//>db.department.find()   // 查询
	}
	
	public static void testRedis(ConfigurableApplicationContext context){
		// 测试连接Redis存储数据  ==> RedisConfig.java RedisUtil.java
		// 配置文件spring.redis...
		//RedisUtil redisUtil = context.getBean(RedisUtil.class);
		//System.out.println(redisUtil.set("name",new String("hfp")));
		//System.out.println(redisUtil.hasKey("name"));
		//System.out.println(redisUtil.get("name"));
	}
}
