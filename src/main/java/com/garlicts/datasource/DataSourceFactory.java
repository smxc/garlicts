package com.garlicts.datasource;

import javax.sql.DataSource;

/**
 * 数据源工厂
 *
 * @author 水木星辰
 * @since 1.0
 */
public interface DataSourceFactory {

    /**
     * 获取数据源
     *
     * @return 数据源
     */
    DataSource getDataSource();
}
