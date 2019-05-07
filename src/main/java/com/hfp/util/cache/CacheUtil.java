package com.hfp.util.cache;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 本地进程内缓存工具类
 */
public class CacheUtil {
	
	private static CacheUtil cacheUtil = null;

    //缓存map  Concurrenthashmap 线程安全
    private  Map<String, CacheEntity> cacheMap = new ConcurrentHashMap<String, CacheEntity>();
    //启动一个线程,定时扫描map,清除过期数据
    private Thread cleanMapThread = new Thread(new CleanMapTask());
    
	//缓存类
    class CacheEntity {
        private long expireTime;  // 截止时间
        private Object object;
        public CacheEntity(Object object, long expireTime) {
            this.object = object;
            this.expireTime = expireTime;
        }
        public long getExpireTime() { return this.expireTime; }
        public Object getObject() { return this.object; }
    }
    
	//清理缓存任务
    class CleanMapTask implements Runnable {
    	private long circleTime = 10 * 60 * 1000; // 循环间隔时间，单位毫秒，默认10分钟
    	public CleanMapTask(){ }
    	public CleanMapTask(long circleTime){
    		this.circleTime = circleTime;
    	}
        public void run() {
            while (true) {

                // 遍历缓存中的所有对象，去掉到期的对象
                for (Map.Entry<String, CacheEntity> entry : cacheMap.entrySet()) {
                    CacheEntity entity = entry.getValue();
                    if ( System.currentTimeMillis() > entity.getExpireTime()) {
                        cacheMap.remove(entry.getKey());
                    }
                }
                
                try {
                    Thread.sleep(circleTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private CacheUtil(){  }  // 使不能被外部构建
    
    public static CacheUtil getInstance() {
    	if( cacheUtil == null )
    		cacheUtil = new CacheUtil();
    	return cacheUtil;
    }

    /**
     * 放入缓存
     *
     * @param key        缓存键
     * @param value      缓存对象
     * @param expireTime 缓存时间 单位秒
     */
    public void put(String key, Object value, long expireTime) {
        cacheMap.put(key, new CacheEntity(value, System.currentTimeMillis() + expireTime*1000));
        if (!cleanMapThread.isAlive()) {
            cleanMapThread.start();
        }
    }

    /**
     * 获取缓存对象
     *
     * @param key 缓存键
     * @return 缓存对象
     */
    public Object get(String key) {
        CacheEntity entity = cacheMap.get(key);
        if (entity != null) {
            // 缺点：采用间隔相同的时间清除缓存，导致有些缓存生存时间比设定的久, 故需要判断
            if ( System.currentTimeMillis() <= entity.getExpireTime()) { // 没过期
                return entity.getObject();
            } else {
            	cacheMap.remove(key);
            }
        }
        return null;
    }
    
    public static void main(String[] args) throws InterruptedException{
    	CacheUtil.getInstance().put("name", "tom", 10);
    	System.out.println(new Date()+" : "+String.valueOf(CacheUtil.getInstance().get("name")));
    	while(true){
    		Thread.sleep(5*1000);
    		System.out.println(new Date()+" : "+String.valueOf(CacheUtil.getInstance().get("name")));
    	}
    }
}