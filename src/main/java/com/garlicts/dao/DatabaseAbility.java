package com.garlicts.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.garlicts.InstanceFactory;
import com.garlicts.config.PropertiesProvider;
import com.garlicts.datasource.DataSourceFactory;

/**
 * 数据库相关操作的帮助类
 *
 * @author 水木星辰
 * @since 1.0
 */
public class DatabaseAbility {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseAbility.class);

    /**
     * 定义一个局部线程变量（使每个线程都拥有自己的连接）
     */
    private static final ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>();

    /**
     * 获取数据源工厂
     */
    private static final DataSourceFactory dataSourceFactory = InstanceFactory.getDataSourceFactory();

    /**
     * 获取数据访问器
     */
    private static final JdbcTemplate jdbcTemplate = InstanceFactory.getJdbcTemplate();

    /**
     * 数据库类型
     */
    private static final String databaseType = PropertiesProvider.getString("smart.framework.jdbc.type");

    /**
     * 获取数据库类型
     */
    public static String getDatabaseType() {
        return databaseType;
    }

    /**
     * 获取数据源
     */
    public static DataSource getDataSource() {
        return dataSourceFactory.getDataSource();
    }

    /**
     * 获取数据库连接
     */
    public static Connection getConnection() {
        Connection conn;
        try {
            // 先从 ThreadLocal 中获取 Connection
            conn = threadLocal.get();
            if (conn == null) {
                // 若不存在，则从 DataSource 中获取 Connection
                conn = getDataSource().getConnection();
                // 将 Connection 放入 ThreadLocal 中
                if (conn != null) {
                    threadLocal.set(conn);
                }
            }
        } catch (SQLException e) {
            logger.error("获取数据库连接出错！");
            throw new RuntimeException(e);
        }
        return conn;
    }

    /**
     * 开启事务
     */
    public static void beginTransaction() {
        Connection conn = getConnection();
        if (conn != null) {
            try {
                conn.setAutoCommit(false);
            } catch (SQLException e) {
                logger.error("开启事务出错！");
                throw new RuntimeException(e);
            } finally {
                threadLocal.set(conn);
            }
        }
    }

    /**
     * 提交事务
     */
    public static void commitTransaction() {
        Connection conn = getConnection();
        if (conn != null) {
            try {
                conn.commit();
                conn.close();
            } catch (SQLException e) {
                logger.error("提交事务出错！");
                throw new RuntimeException(e);
            } finally {
                threadLocal.remove();
            }
        }
    }

    /**
     * 回滚事务
     */
    public static void rollbackTransaction() {
        Connection conn = getConnection();
        if (conn != null) {
            try {
                conn.rollback();
                conn.close();
            } catch (SQLException e) {
                logger.error("回滚事务出错！");
                throw new RuntimeException(e);
            } finally {
                threadLocal.remove();
            }
        }
    }

    /**
     * 初始化 SQL 脚本
     */
//    public static void initSQL(String sqlPath) {
//        try {
//            File sqlFile = new File(ClassUtil.getClassPath() + sqlPath);
//            List<String> sqlList = FileUtils.readLines(sqlFile);
//            for (String sql : sqlList) {
//                update(sql);
//            }
//        } catch (Exception e) {
//            logger.error("初始化 SQL 脚本出错！");
//            throw new RuntimeException(e);
//        }
//    }

    /**
     * 根据 SQL 语句查询 Entity
     */
//    public static <T> T queryEntity(Class<T> entityClass, String sql, Object... params) {
//        return jdbcTemplate.queryEntity(entityClass, sql, params);
//    }

    /**
     * 根据 SQL 语句查询 Entity 列表
     */
//    public static <T> List<T> queryEntityList(Class<T> entityClass, String sql, Object... params) {
//        return jdbcTemplate.queryEntityList(entityClass, sql, params);
//    }

    /**
     * 根据 SQL 语句查询 Entity 映射（Field Name => Field Value）
     */
    public static <K, V> Map<K, V> queryEntityMap(Class<V> entityClass, String sql, Object... params) {
        return jdbcTemplate.queryEntityMap(entityClass, sql, params);
    }

    /**
     * 根据 SQL 语句查询 Array 格式的字段（单条记录）
     */
    public static Object[] queryArray(String sql, Object... params) {
        return jdbcTemplate.queryArray(sql, params);
    }

    /**
     * 根据 SQL 语句查询 Array 格式的字段列表（多条记录）
     */
    public static List<Object[]> queryArrayList(String sql, Object... params) {
        return jdbcTemplate.queryArrayList(sql, params);
    }

    /**
     * 根据 SQL 语句查询 Map 格式的字段（单条记录）
     */
    public static Map<String, Object> queryMap(String sql, Object... params) {
        return jdbcTemplate.queryMap(sql, params);
    }

    /**
     * 根据 SQL 语句查询 Map 格式的字段列表（多条记录）
     */
    public static List<Map<String, Object>> queryMapList(String sql, Object... params) {
        return jdbcTemplate.queryMapList(sql, params);
    }

    /**
     * 根据 SQL 语句查询指定字段（单条记录）
     */
    public static <T> T queryColumn(String sql, Object... params) {
        return jdbcTemplate.queryColumn(sql, params);
    }

    /**
     * 根据 SQL 语句查询指定字段列表（多条记录）
     */
    public static <T> List<T> queryColumnList(String sql, Object... params) {
        return jdbcTemplate.queryColumnList(sql, params);
    }

    /**
     * 根据 SQL 语句查询指定字段映射（多条记录）
     */
    public static <T> Map<T, Map<String, Object>> queryColumnMap(String column, String sql, Object... params) {
        return jdbcTemplate.queryColumnMap(column, sql, params);
    }

    /**
     * 根据 SQL 语句查询记录条数
     */
    public static long queryCount(String sql, Object... params) {
        return jdbcTemplate.queryCount(sql, params);
    }

    /**
     * 执行更新语句（包括：update、insert、delete）
     */
    public static int update(String sql, Object... params) {
        return jdbcTemplate.update(sql, params);
    }

    /**
     * 执行插入语句，返回插入后的主键
     */
    public static Serializable insertReturnPK(String sql, Object... params) {
        return jdbcTemplate.insertReturnPK(sql, params);
    }
}
