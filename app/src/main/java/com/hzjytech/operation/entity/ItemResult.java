package com.hzjytech.operation.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hehongcan on 2017/9/26.
 */

public class ItemResult implements Serializable {
    private int itemId;
    private List<Integer> itemIds;
    private MenuInfo.ItemsBean  itemInfo;

    public ItemResult(
            int itemId, List<Integer> itemIds, MenuInfo.ItemsBean itemInfo) {
        this.itemId = itemId;
        this.itemIds = itemIds;
        this.itemInfo = itemInfo;
    }
}
