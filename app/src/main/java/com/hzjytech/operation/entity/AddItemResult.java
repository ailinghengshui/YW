package com.hzjytech.operation.entity;

import java.io.Serializable;

/**
 * Created by hehongcan on 2017/9/27.
 */

public class AddItemResult implements Serializable{
    private int menuId;
    private int vmTypeId;
    private MenuInfo.ItemsBean  itemInfo;

    public AddItemResult(
            int menuId, int vmTypeId, MenuInfo.ItemsBean itemsBean) {
        this.menuId = menuId;
        this.vmTypeId = vmTypeId;
        itemInfo = itemsBean;
    }
}
