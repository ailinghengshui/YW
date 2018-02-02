package com.hzjytech.operation.entity;

/**
 * Created by hehongcan on 2017/9/27.
 */

public class AddPackResult {
    private int menuId;
    private int vmTypeId;
    private MenuInfo.PacksBean itemInfo;

    public AddPackResult(
            int menuId, int vmTypeId, MenuInfo.PacksBean itemInfo) {
        this.menuId = menuId;
        this.vmTypeId = vmTypeId;
        this.itemInfo = itemInfo;
    }
}
