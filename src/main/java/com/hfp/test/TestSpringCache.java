package com.hfp.test;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;


@CacheConfig(cacheNames = "department")
@Component
public class TestSpringCache {
/*
@Cacheable   触发缓存入口
@CacheEvict  触发移除缓存
@CacahePut   更新缓存
@Caching     将多种缓存操作分组
@CacheConfig 类级别的缓存注解，允许共享缓存名称
*/
	
	@CachePut(key = "#department.id")
    public Department save(Department department) {
        System.out.println("保存 id=" + department.getId() + " 的数据");
        // ... 操作数据库的代码
        return department;  // 把返回的对象保存在缓存中, key是department.id值
    }
	
	@Cacheable(key = "#id")  // 缓存中没有数据才执行getDepartmentById方法
    public Department getDepartmentById(Integer id) {
        System.out.println("获取 id=" + id + " 的数据");
        Department department = new Department(id,"1234");  
        // ... 操作数据库的代码
        return department;   // 执行getDepartmentById之后缓存结果
    }
	
	@CacheEvict(key = "#id")  // 删除缓存
    public void deleteById(Integer id) {
        System.out.println("删除 id=" + id + " 的数据");
        // ... 操作数据库的代码
    }
}

