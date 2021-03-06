package com.concur.dbcache.persist.service;

import com.concur.dbcache.cache.CacheUnit;
import com.concur.dbcache.CacheObject;
import com.concur.dbcache.IEntity;
import com.concur.dbcache.conf.impl.CacheConfig;
import com.concur.dbcache.dbaccess.DbAccessService;

import java.util.concurrent.ExecutorService;

/**
 * 实体入库服务接口
 * @author Jake
 * @date 2014年8月13日上午12:24:45
 */
public interface DbPersistService {

	/**
	 * 处理创建
	 * @param cacheObject 实体缓存对象
	 * @param dbAccessService 数据库存取服务
	 * @param cacheConfig TODO
	 */
	<T extends IEntity<?>> void handleSave(
            CacheObject<T> cacheObject,
            DbAccessService dbAccessService,
            CacheConfig<T> cacheConfig);

	/**
	 * 处理更新
	 * @param cacheObject 实体缓存对象
	 * @param dbAccessService 数据库存取服务
	 */
	<T extends IEntity<?>> void handleUpdate(
            CacheObject<T> cacheObject,
            DbAccessService dbAccessService,
            CacheConfig<T> cacheConfig);

	/**
	 * 处理删除
	 * @param cacheObject 实体缓存对象
	 * @param dbAccessService 数据库存取服务
	 * @param key 缓存key
	 * @param cacheUnit 缓存容器
	 */
	void handleDelete(
            CacheObject<?> cacheObject,
            DbAccessService dbAccessService,
            Object key,
            CacheUnit cacheUnit);

	/**
	 * 释放并且等待处理完毕
	 */
	void destroy();

	/**
	 * 打印出未入库对象
	 */
	void logHadNotPersistEntity();

	/**
	 * 获取入库线程池
	 * @return ExecutorService
	 */
	ExecutorService getThreadPool();

}
