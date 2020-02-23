package com.changgou.canal.listener;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.xpand.starter.canal.annotation.*;

/**
 * @author 矢量
 * @date 2020/2/19-10:39
 * 实现mysql数据监听
 */
@CanalEventListener
public class CanalDataEventListener {
    /**
     * 监听数据的增加
     * @param evenType 当前操作的类型
     * @param rowData 发生变更的一行数据
     */
    @InsertListenPoint
    public void onEventInsert(CanalEntry.EventType evenType, CanalEntry.RowData rowData){
        for (CanalEntry.Column column : rowData.getAfterColumnsList()){
            System.out.println("列名：" + column.getName() + "------变更的数据:" + column.getValue());
        }
    }

    /**
     * 监听数据的修改
     */
    @UpdateListenPoint
    public void onEventUpdate(CanalEntry.EventType evenType, CanalEntry.RowData rowData){
        for (CanalEntry.Column column : rowData.getBeforeColumnsList()){
            System.out.println("修改前：列名：" + column.getName() + "------变更的数据:" + column.getValue());
        }

        for (CanalEntry.Column column : rowData.getAfterColumnsList()){
            System.out.println("修改后：列名：" + column.getName() + "------变更的数据:" + column.getValue());
        }
    }

    /**
     * 监听数据的删除
     */
    @DeleteListenPoint
    public void onEventDelete(CanalEntry.EventType evenType, CanalEntry.RowData rowData){
        for (CanalEntry.Column column : rowData.getBeforeColumnsList()){
            System.out.println("删除前：列名：" + column.getName() + "------变更的数据:" + column.getValue());
        }
    }


    /**
     * 自定义监听
     * @param evenType
     * @param rowData
     */
    @ListenPoint(
            eventType = {CanalEntry.EventType.DELETE, CanalEntry.EventType.UPDATE}, // 监听类型
            schema = {"changgou_content"}, // 指定监听的数据库
            table = {"tb_content"},  // 指定监控的表
            destination = "example" // 指定实例的地址
    )
    public void onEventCustomerUpdate(CanalEntry.EventType evenType, CanalEntry.RowData rowData){
        for (CanalEntry.Column column : rowData.getBeforeColumnsList()){
            System.out.println("自定义操作前：列名：" + column.getName() + "------变更的数据:" + column.getValue());
        }
        for (CanalEntry.Column column : rowData.getAfterColumnsList()){
            System.out.println("自定义操作后：列名：" + column.getName() + "------变更的数据:" + column.getValue());
        }
    }
}
