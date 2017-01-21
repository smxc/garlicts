package com.garlicts.dao.bean;

import java.util.List;

import com.garlicts.core.bean.BaseBean;

/**
 * 分页对象
 *
 * @author 水木星辰
 * @since 1.0
 */
public class Pager<T> extends BaseBean {

	private static final long serialVersionUID = 2348138641614817049L;
	
	private int pageNumber;     // 页面编号
    private int pageSize;       // 每页条数
    private long totalRecord;   // 总记录数
    private List<T> recordList; // 数据列表

    public Pager() {
    }

    public Pager(int pageNumber, int pageSize, long totalRecord, List<T> recordList) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalRecord = totalRecord;
        this.recordList = recordList;
    }

    public long getTotalPage() {
        return totalRecord % pageSize == 0 ? totalRecord / pageSize : totalRecord / pageSize + 1;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(long totalRecord) {
        this.totalRecord = totalRecord;
    }

    public List<T> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<T> recordList) {
        this.recordList = recordList;
    }
}
