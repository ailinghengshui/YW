package com.hzjytech.operation.http.api;

import com.hzjytech.operation.entity.AddItemResult;
import com.hzjytech.operation.entity.AddMachineInfo;
import com.hzjytech.operation.entity.AddMenuMachineInfo;
import com.hzjytech.operation.entity.AddPackResult;
import com.hzjytech.operation.entity.ItemResult;
import com.hzjytech.operation.entity.MenuList;
import com.hzjytech.operation.entity.PackItemResult;
import com.hzjytech.operation.entity.RecipeInfo;
import com.hzjytech.operation.http.HttpResultFunc;
import com.hzjytech.operation.http.RetrofitSingleton;


import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hehongcan on 2017/5/11.
 */
public class MenuApi {
    //获取菜单列表
    public static Observable<List<MenuList>> getSingleMenuList(String token, String queryCondition){
        return RetrofitSingleton.getApiService().getSingleMenuList(token,queryCondition)
                .map(new HttpResultFunc<List<MenuList>>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    //删除菜单中包含的机器
    public static Observable<Object> detachMachine(String token, int menuId, String subMachineIds) {
        return RetrofitSingleton.getApiService()
                .detachMenuMachine(token,subMachineIds,menuId)
                .map(new HttpResultFunc<Object>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    //获取菜单可添加的机器
    public static Observable<List<AddMenuMachineInfo>>getAddMachines(String token, int menuId, int vmTypeId) {
        return RetrofitSingleton.getApiService()
                .getMenuAddMachineList(token,menuId,vmTypeId)
                .map(new HttpResultFunc<List<AddMenuMachineInfo>>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    //添加咖啡机
    public static Observable<Object> addMenuMachine(String token, int menuId, String subMachineIds) {
        return RetrofitSingleton.getApiService()
                .addMenuMachine(token,subMachineIds,menuId)
                .map(new HttpResultFunc<Object>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    //修改菜单单品属性
    public static Observable<Object> changeMenuItem(String token,  ItemResult result) {
        return RetrofitSingleton.getApiService()
                .changeMenuItem(token,result)
                .map(new HttpResultFunc<Object>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    //修改菜单套餐属性
    public static Observable<Object> changeMenuPack(String token, PackItemResult result) {
        return RetrofitSingleton.getApiService()
                .changePack(token,result)
                .map(new HttpResultFunc<Object>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    //创建菜单单品属性
    public static Observable<Object> creatMenuItem(String token,  AddItemResult result) {
        return RetrofitSingleton.getApiService()
                .creatMenuItem(token,result)
                .map(new HttpResultFunc<Object>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    //创建菜单套餐
    public static Observable<Object> creatMenuPack(String token,  AddPackResult result) {
        return RetrofitSingleton.getApiService()
                .createPackageItem(token,result)
                .map(new HttpResultFunc<Object>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    //删除单品
    public static Observable<Object> destroyItem(String token, int itemId) {
        return RetrofitSingleton.getApiService()
                .destroyItem(token,itemId)
                .map(new HttpResultFunc<Object>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    //删除套餐
    public static Observable<Object> destroyPacketItem(String token, int itemId) {
        return RetrofitSingleton.getApiService()
                .destroyItem(token,itemId)
                .map(new HttpResultFunc<Object>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    //获取配方种类
    public static Observable<List<RecipeInfo>> typeList(String token, int vmTypeId) {
        return RetrofitSingleton.getApiService()
                .typeList(token,vmTypeId)
                .map(new HttpResultFunc<List<RecipeInfo>>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
