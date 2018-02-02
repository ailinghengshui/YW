package com.hzjytech.operation.entity;

import java.io.Serializable;

/**
 * Created by hehongcan on 2017/9/27.
 */

public class PackItemResult implements Serializable{
    private int itemId;
    private MenuInfo.PacksBean itemInfo;

    public PackItemResult(int itemId, MenuInfo.PacksBean itemInfo) {
        this.itemId = itemId;
        this.itemInfo = itemInfo;
    }
}
