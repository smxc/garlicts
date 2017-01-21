package com.garlicts.datasource.impl;

import javax.sql.DataSource;

import com.garlicts.FrameworkConstant;
import com.garlicts.config.PropertiesProvider;
import com.garlicts.datasource.DataSourceFactory;

/**
 * 抽象数据源工厂
 *
 * @author 水木星辰
 * @since 1.0
 */
public abstract class AbstractDataSourceFactory<T extends DataSource> implements DataSourceFactory {

    protected final String driver = PropertiesProvider.getString(FrameworkConstant.JDBC_DRIVER);
    protected final String url = PropertiesProvider.getString(FrameworkConstant.JDBC_URL);
    protected final String username = PropertiesProvider.getString(FrameworkConstant.JDBC_USERNAME);
    protected final String password = PropertiesProvider.getString(FrameworkConstant.JDBC_PASSWORD);

    @Override
    public final T getDataSource() {
        // 创建数据源对象
        T ds = createDataSource();
        // 设置基础属性
        setDriver(ds, driver);
        setUrl(ds, url);
        setUsername(ds, username);
        setPassword(ds, password);
        // 设置高级属性
        setAdvancedConfig(ds);
        return ds;
    }

    public abstract T createDataSource();

    public abstract void setDriver(T ds, String driver);

    public abstract void setUrl(T ds, String url);

    public abstract void setUsername(T ds, String username);

    public abstract void setPassword(T ds, String password);

    public abstract void setAdvancedConfig(T ds);
    
}
