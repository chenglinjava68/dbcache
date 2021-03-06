package com.concur.dbcache.anno;

import com.concur.dbcache.conf.PersistType;
import com.concur.dbcache.conf.CacheType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h5>实体缓存配置注解</h5>
 * <br/>没有标注此注解的实体类则使用默认配置
 * <br/>实体需要持久化的属性需使用基本类型或String,byte,byte[]等
 * <br/>使用DynamicUpdate后:
 * <br/>由于采用字节码增强实现监听属性变化的方式,外部put/add瞬时的如Map,List等结构时的属性,需要在update之前再次set一遍
 * @see Index
 * @see DynamicUpdate
 * @see JsonType
 * @author Jake
 * @date 2014年9月13日下午1:38:22
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cached {

	/**
	 * 缓存类型,默认CacheType.LRU
	 * @return
	 */
	CacheType cacheType() default CacheType.LRU;

	/**
	 * 持久化处理方式,默认PersistType.INTIME
	 * @return
	 */
	PersistType persistType() default PersistType.INTIME;

	/**
	 * 实体缓存大小上限,默认值10000
	 * <p>分类缓存的概念在于:</p>
	 * 1,提高hash命中率
	 * 2,防止极端情况的LRU出队
	 * 3,如果超过阀值,则进行立即回收到entitySize的大小
	 * 4,实际运行的大小限制收全局实体大小设置影响
	 * @return
	 */
	int entitySize() default 10000;

	/**
	 * 实体数量阀值,默认20000
	 * 如果超过阀值,则进行立即回收到entitySize的大小
	 * @return
	 */
	int threshold() default 20000;

	/**
	 * 索引缓存大小,不设置则共用entityCache缓存(大小为entitySize)
	 * @return
	 */
	int indexSize() default 0;

	/**
	 * 并发线程数,默认为运行时CPU核数(Runtime.getRuntime().availableProcessors())
	 * @return
	 */
	int concurrencyLevel() default 16;

	/**
	 * 是否启用索引服务
	 * <br/>未开启索引服务时,dbcache.anno.Index将不会生效
	 * @return
	 */
	boolean enableIndex() default false;

	/**
	 * 删除后是否从缓存移除
	 * <br/>比如当一次性使用的实体需要删除时,使用次选项
	 * @return
	 */
	boolean evictWhenDelete() default false;

}
