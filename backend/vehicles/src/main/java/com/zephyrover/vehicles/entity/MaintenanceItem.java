package com.zephyrover.vehicles.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.math.BigDecimal;

/**
 * 维保项目实体类
 */
@TableName("maintenance_item")
public class MaintenanceItem {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("record_id")
    private Long recordId;

    @TableField("item_name")
    private String itemName;

    @TableField("item_cost")
    private BigDecimal itemCost;

    public MaintenanceItem() {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public BigDecimal getItemCost() {
        return itemCost;
    }

    public void setItemCost(BigDecimal itemCost) {
        this.itemCost = itemCost;
    }
}
