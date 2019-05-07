package com.hfp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;

import com.hfp.db.MongodbUtil;
import com.hfp.db.RedisUtil;
import com.hfp.test.Department;
import com.hfp.test.TestSpringCache;

@EnableCaching          // 开启缓存功能
@SpringBootApplication
public class Application {
	
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
		testMongoDB(context);
		//testRedis(context);
		//testRedisCache(context);
		//testEhcache(context);
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
	
	public static void testRedisCache(ConfigurableApplicationContext context){
		// 测试使用Redis做缓存  ==> @EnableCaching RedisConfig.java TestSpringCache.java
		// 配置文件spring.cache.type=redis,spring.cache.redis...
		//TestSpringCache redisCache = context.getBean(TestSpringCache.class);
		//redisCache.save(new Department(new Integer(20),"hfp"));
		//System.out.println(redisCache.getDepartmentById(new Integer(20)));
		//redisCache.deleteById(new Integer(10));
		
		//redis命令登陆查看
		//redis-cli
		//>auth password
		//>keys *
		//2) "department::10"
		//>get department::10
	}
	
	public static void testEhcache(ConfigurableApplicationContext context){
		// Ehcache(缓存): 纯Java的进程内缓存框架
		// 配置文件spring.cache.type=ehcache,spring.cache.ehcache...
		TestSpringCache redisCache = context.getBean(TestSpringCache.class);
		redisCache.save(new Department(new Integer(20),"hfp"));
		System.out.println(redisCache.getDepartmentById(new Integer(20)));
	}
}
