package com.concur.dbcache;

import com.concur.dbcache.conf.impl.CacheConfig;
import com.concur.dbcache.index.IndexObject;
import com.concur.dbcache.persist.PersistStatus;
import com.concur.unity.typesafe.SafeType;
import org.apache.commons.lang.builder.EqualsBuilder;

import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * 单个实体缓存数据结构
 *
 * @author jake
 * @date 2014-7-31-下午8:18:03
 */
public class CacheObject<T extends IEntity<?>> extends SafeType {

	/**
	 * 缓存对象
	 */
	protected final T entity;

	/**
	 * 代理缓存对象
	 */
	protected final T proxyEntity;

	/**
	 * 修改过的属性
	 */
	private volatile AtomicIntegerArray modifiedFields;


	// for persist
	/**
	 * 持久化状态
	 */
	private volatile PersistStatus persistStatus;

	/**
	 * 索引对象引用持有
	 */
	private final Set<IndexObject<?>> indexObjects = new TreeSet<IndexObject<?>>();

	/**
	 * WAL日志
	 */
	private volatile WALLog walLog = new WALLog();

	/**
	 * WCL日志
	 */
	private volatile WCLLog wclLog = new WCLLog();

	/**
	 * 构造方法
	 *
	 * @param entity
	 *            实体
	 * @param clazz
	 *            类型
	 */
	public CacheObject(T entity, Class<T> clazz, T proxyEntity, AtomicIntegerArray modifiedFields) {
		this.entity = entity;
		this.proxyEntity = proxyEntity;
		this.persistStatus = PersistStatus.TRANSIENT;
		this.modifiedFields = modifiedFields;
	}

	/**
	 * 初始化
	 */
	public void doInit(CacheConfig<T> cacheConfig) {
		// 加载回调
		this.doAfterLoad();
	}
	
	/**
	 * 调用加载回调
	 */
	public void doAfterLoad() {
		// 调用初始化
		if (entity instanceof EntityInitializer){
			EntityInitializer entityInitializer = (EntityInitializer) entity;
			entityInitializer.doAfterLoad();
		}
	}

	/**
	 * 持久化之前的操作
	 */
	public void doBeforePersist(CacheConfig<T> cacheConfig) {
		// 持久化前操作
		if (entity instanceof EntityInitializer) {
			EntityInitializer entityInitializer = (EntityInitializer) entity;
			entityInitializer.doBeforePersist();
		}
	}
	

	@Override
	public int hashCode() {
		if (this.entity.getId() == null) {
			return this.entity.getClass().hashCode() * 31;
		}
		return this.entity.getClass().hashCode() * 31 + this.entity.getId().hashCode();
	}

	
	@Override
	@SuppressWarnings("unchecked")
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof CacheObject)) {
			return false;
		}
		
		CacheObject<T> target = (CacheObject<T>) obj;
		
		return new EqualsBuilder()
				.append(this.entity.getClass(), target.entity.getClass())
				.append(this.entity.getId(), target.entity.getId()).isEquals();
	}

	public T getEntity() {
		return entity;
	}

	public T getProxyEntity() {
		return proxyEntity;
	}

	public PersistStatus getPersistStatus() {
		return persistStatus;
	}

	public void setPersistStatus(PersistStatus persistStatus) {
		this.persistStatus = persistStatus;
	}

	public AtomicIntegerArray getModifiedFields() {
		return modifiedFields;
	}

	/**
	 * 移除索引对象引用
	 * @param indexObject IndexObject<?>
	 */
	public void removeIndexObject(IndexObject<?> indexObject) {
		this.indexObjects.remove(indexObject);
	}

	/**
	 * 添加索引对象引用
	 * @param indexObject IndexObject<?>
	 */
	public void addIndexObject(IndexObject<?> indexObject) {
		this.indexObjects.add(indexObject);
	}

	public WALLog getWalLog() {
		return walLog;
	}

	public WCLLog getWclLog() {
		return wclLog;
	}
}