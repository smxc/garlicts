package com.garlicts.framework.dao;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.garlicts.framework.InstanceFactory;
import com.garlicts.framework.config.PropertiesProvider;
import com.garlicts.framework.datasource.DataSourceFactory;

/**
 * 数据库相关操作的帮助类
 *
 * @author 水木星辰
 * @since 1.0
 */
public class DatabaseComponent {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseComponent.class);

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
//    private static final JdbcTemplate jdbcTemplate = InstanceFactory.getJdbcTemplate();

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
    
}
