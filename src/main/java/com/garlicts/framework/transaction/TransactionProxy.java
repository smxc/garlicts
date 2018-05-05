package com.garlicts.framework.transaction;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.garlicts.framework.aop.proxy.Proxy;
import com.garlicts.framework.aop.proxy.ProxyChain;
import com.garlicts.framework.dao.DatabaseComponent;
import com.garlicts.framework.transaction.annotation.Transaction;

/**
 * 事务代理
 *
 * @author 水木星辰
 * @since 1.0
 */
public class TransactionProxy implements Proxy {

    private static final Logger logger = LoggerFactory.getLogger(TransactionProxy.class);

    /**
     * 定义一个线程局部变量，用于保存当前线程中是否进行了事务处理，默认为 false（未处理）
     */
    private static final ThreadLocal<Boolean> threadLocal = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };

    @Override
    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result;
        // 判断当前线程是否进行了事务处理
        boolean flag = threadLocal.get();
        // 获取目标方法
        Method method = proxyChain.getTargetMethod();
        // 若当前线程未进行事务处理，且在目标方法上定义了 Transaction 注解，则说明该方法需要进行事务处理
        if (!flag && method.isAnnotationPresent(Transaction.class)) {
            // 设置当前线程已进行事务处理
        	threadLocal.set(true);
            try {
                // 开启事务
                DatabaseComponent.beginTransaction();
                logger.info("开启事务");
                // 执行目标方法
                result = proxyChain.doProxyChain();
                // 提交事务
                DatabaseComponent.commitTransaction();
                logger.info("提交事务");
            } catch (Exception e) {
                // 回滚事务
                DatabaseComponent.rollbackTransaction();
                logger.info("回滚事务");
                throw e;
            } finally {
                // 移除线程局部变量
            	threadLocal.remove();
            }
        } else {
            // 执行目标方法
            result = proxyChain.doProxyChain();
        }
        return result;
    }
}
