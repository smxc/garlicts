package com.garlicts.framework.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.BeanMapHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.KeyedHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 默认数据访问器
 * <br/>
 * 基于 Apache Commons DbUtils 实现
 *
 * @author 水木星辰
 * @since 1.0
 * 
 * common-dbutils
 * http://wallimn.iteye.com/blog/1606930
 */
public class JdbcTemplate {

    private static final Logger logger = LoggerFactory.getLogger(JdbcTemplate.class);

    private final QueryRunner queryRunner;

    public JdbcTemplate() {
        DataSource dataSource = DatabaseComponent.getDataSource();
        queryRunner = new QueryRunner(dataSource);
    }

    public <T> T queryOne(String sql, Class<T> resultClass){
    	T result = null;
    	try {
    		result = queryRunner.query(sql, new BeanHandler<T>(resultClass));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
    	printSQL(sql);
    	return result;
    }
    
    /**
     * 查询对应的实体，返回单条记录
     */
    public <T> T queryOne(String sql, Object[] params, Class<T> resultClass) {
        T result;
        try {
        	result = queryRunner.query(sql, new BeanHandler<T>(resultClass), params);
        } catch (SQLException e) {
            logger.error("查询出错！");
            throw new RuntimeException(e);
        }
        printSQL(sql);
        return result;
    }

    /**
     * 查询对应的实体列表，返回多条记录
     */
    public <T> List<T> queryList(String sql, Object[] params, Class<T> resultClass) {
        List<T> result;
        try {
        	result = queryRunner.query(sql, new BeanListHandler<T>(resultClass), params);
        } catch (SQLException e) {
            logger.error("查询出错！");
            throw new RuntimeException(e);
        }
        printSQL(sql);
        return result;
    }

    public Object[] queryArray(String sql, Object[] params) {
        Object[] array;
        try {
            array = queryRunner.query(sql, new ArrayHandler(), params);
        } catch (SQLException e) {
            logger.error("查询出错！");
            throw new RuntimeException(e);
        }
        printSQL(sql);
        return array;
    }

    public List<Object[]> queryArrayList(String sql, Object[] params) {
        List<Object[]> arrayList;
        try {
            arrayList = queryRunner.query(sql, new ArrayListHandler(), params);
        } catch (SQLException e) {
            logger.error("查询出错！");
            throw new RuntimeException(e);
        }
        printSQL(sql);
        return arrayList;
    }

    public <K, V> Map<K, V> queryMap(String sql, Object[] params, Class<V> resultClass) {
        Map<K, V> result;
        try {
        	result = queryRunner.query(sql, new BeanMapHandler<K, V>(resultClass), params);
        } catch (SQLException e) {
            logger.error("查询出错！");
            throw new RuntimeException(e);
        }
        printSQL(sql);
        return result;
    }
    
    public Map<String, Object> queryMap(String sql, Object[] params) {
        Map<String, Object> map;
        try {
            map = queryRunner.query(sql, new MapHandler(), params);
        } catch (SQLException e) {
            logger.error("查询出错！");
            throw new RuntimeException(e);
        }
        printSQL(sql);
        return map;
    }    
    
    /**
     * 查询对应的数据，返回多条记录（列名 => 数据）
     */
    public List<Map<String, Object>> queryMapList(String sql, Object... params) {
        List<Map<String, Object>> result;
        try {
        	result = queryRunner.query(sql, new MapListHandler(), params);
        } catch (SQLException e) {
            logger.error("查询出错！");
            throw new RuntimeException(e);
        }
        printSQL(sql);
        return result;
    }

    /**
     * 查询对应的数据，返回单条数据（列名 => 数据）
     */
    public <T> T queryColumn(String sql, Object[] params) {
        T result;
        try {
        	result = queryRunner.query(sql, new ScalarHandler<T>(), params);
        } catch (SQLException e) {
            logger.error("查询出错！");
            throw new RuntimeException(e);
        }
        printSQL(sql);
        return result;
    }

    /**
     * 查询对应的数据，返回多条数据（列名 => 数据）
     */
    public <T> List<T> queryColumnList(String sql, Object... params) {
        List<T> list;
        try {
            list = queryRunner.query(sql, new ColumnListHandler<T>(), params);
        } catch (SQLException e) {
            logger.error("查询出错！");
            throw new RuntimeException(e);
        }
        printSQL(sql);
        return list;
    }

    /**
     * 查询指定列名对应的数据，返回多条数据（列名对应的数据 => 列名与数据的映射关系）
     */
    public <T> Map<T, Map<String, Object>> queryColumnMap(String column, String sql, Object... params) {
        Map<T, Map<String, Object>> map;
        try {
            map = queryRunner.query(sql, new KeyedHandler<T>(column), params);
        } catch (SQLException e) {
            logger.error("查询出错！");
            throw new RuntimeException(e);
        }
        printSQL(sql);
        return map;
    }

    /**
     * 查询记录条数，返回总记录数
     */
    public long queryCount(String sql, Object[] params) {
        long result;
        try {
            result = queryRunner.query(sql, new ScalarHandler<Long>("count(*)"), params);
        } catch (SQLException e) {
            logger.error("查询出错！");
            throw new RuntimeException(e);
        }
        printSQL(sql);
        return result;
    }

    /**
     * 执行更新操作（包括：update、insert、delete），返回所更新的记录数
     */
    public int update(String sql, Object[] params) {
        int result;
        try {
            result = queryRunner.update(sql, params);
        } catch (SQLException e) {
            logger.error("更新出错！");
            throw new RuntimeException(e);
        }
        printSQL(sql);
        return result;
    }

    /**
     * 插入 
     */
    public int insert(String sql, Object[] params){
    	
    	int affectedRows = 0;
    	
    	try {
			affectedRows = queryRunner.update(sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	return affectedRows;
    	
    }
    
    /**
     * 批量插入或更新
     */
    public int[] batchInsertOrUpdate(String sql, Object[][] params){
    	
    	int[] affectedRows = new int[0];
    	
    	try {
    		affectedRows = queryRunner.batch(sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	return affectedRows;
    	
    }
    
    private static void printSQL(String sql) {
        logger.info(sql);
    }
    
}



















