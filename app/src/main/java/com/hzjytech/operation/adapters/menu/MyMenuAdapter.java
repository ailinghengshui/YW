package com.hzjytech.operation.adapters.menu;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.hzjytech.operation.R;
import com.hzjytech.operation.adapters.menu.viewholders.MenuBasicViewHolder;
import com.hzjytech.operation.adapters.menu.viewholders.MenuItemViewHolder;
import com.hzjytech.operation.adapters.menu.viewholders.MenuMachineViewHolder;
import com.hzjytech.operation.adapters.menu.viewholders.MenuPackViewHolder;
import com.hzjytech.operation.constants.Constants;
import com.hzjytech.operation.entity.MenuInfo;
import com.hzjytech.operation.inter.MoreMachineClickListener;
import com.hzjytech.operation.inter.MorePackClickListener;
import com.hzjytech.operation.inter.MoreSingleItemClickListener;
import com.hzjytech.operation.inter.OnInfoClickListener;
import com.hzjytech.operation.module.data.TreeNodeHelper;
import com.hzjytech.operation.utils.TimeUtil;

import java.util.List;

import static com.hzjytech.operation.constants.Constants.MenuAuth.AUTH_DELET_ADD;
import static com.hzjytech.operation.constants.Constants.MenuAuth.AUTH_EDIT;
import static com.hzjytech.operation.constants.Constants.MenuAuth.AUTH_MACHINE;

/**
 * Created by hehongcan on 2017/5/3.
 */
public class MyMenuAdapter extends RecyclerView.Adapter {
    private static final int TYPE_BASIC = 0;
    private static final int TYPE_MACHINE = 1;
    private static final int TYPE_ITEM = 3;
    private static final int TYPE_PACK = 4;
    private  Context context;
    private  MenuInfo menuInfo;
    private final LayoutInflater inflater;
    private boolean AUTH_EDIT_MACHINE=false;
    private boolean AUTH_DELET_ADD_MENU_ITEM=false;
    private boolean AUTH_EDIT_MENU_ITEM=false;
    private OnInfoClickListener onInfoClickListener;

    public MyMenuAdapter(Context context, MenuInfo menuInfo) {
        this.context=context;
        this.menuInfo=menuInfo;
        inflater = LayoutInflater.from(context);
        resolveAuth();
    }
   public void setMenuData(MenuInfo menuInfo){
       this.menuInfo=menuInfo;
       resolveAuth();
       notifyDataSetChanged();
   }

    /**\
     * 权限管理
     */
    private void resolveAuth() {
        if(menuInfo==null){
            return;
        }
        List<String> auth = menuInfo.getAuth();
        if(auth.contains(AUTH_MACHINE)){
            AUTH_EDIT_MACHINE= true;
        }else {
            AUTH_EDIT_MACHINE=false;
        }
        if(auth.contains(AUTH_DELET_ADD)){
            AUTH_DELET_ADD_MENU_ITEM=true;
        }else{
            AUTH_DELET_ADD_MENU_ITEM=false;
        }
        if(auth.contains(AUTH_EDIT)){
            AUTH_EDIT_MENU_ITEM=true;
        }else{
            AUTH_EDIT_MENU_ITEM=false;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType== TYPE_BASIC){
            View view = inflater.inflate(R.layout.item_menu_basic, parent, false);
            return new MenuBasicViewHolder(view);

        }else if(viewType==TYPE_MACHINE){
            View view = inflater.inflate(R.layout.item_machines_container, parent, false);
            return new MenuMachineViewHolder(view);

        }else if(viewType==TYPE_ITEM){
            View view = inflater.inflate(R.layout.item_menu_item,parent, false);
            return new MenuItemViewHolder(view);
        }else{
            View view = inflater.inflate(R.layout.item_menu_pack, parent, false);
            return new MenuPackViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MenuBasicViewHolder){
            MenuBasicViewHolder menuBasicViewHolder = (MenuBasicViewHolder) holder;
            menuBasicViewHolder.tvMenuBasicType.setText(menuInfo.getVmTypeName());
            menuBasicViewHolder.tvMenuBasicTime.setText(TimeUtil.getCorrectTimeString(menuInfo.getCreateAt()));
        }else if(holder instanceof  MenuMachineViewHolder){
            MenuMachineViewHolder machineViewHolder = (MenuMachineViewHolder) holder;
            machineViewHolder.setMenuMachineData(context,menuInfo.getSubMachines(),AUTH_EDIT_MACHINE,onInfoClickListener);
        }else if(holder instanceof MenuItemViewHolder){
            MenuItemViewHolder menuItemViewHolder = (MenuItemViewHolder) holder;
            menuItemViewHolder.setMenuItemData(context,menuInfo.getItems(),AUTH_DELET_ADD_MENU_ITEM,AUTH_EDIT_MENU_ITEM,onInfoClickListener);
        }else{
            MenuPackViewHolder menuPackViewHolder = (MenuPackViewHolder) holder;
            menuPackViewHolder.setMenuPackData(context,menuInfo.getPacks(),AUTH_DELET_ADD_MENU_ITEM,AUTH_EDIT_MENU_ITEM,onInfoClickListener);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return TYPE_BASIC;
        }else if(position==1){
            return TYPE_MACHINE;
        }else if(position==2){
            return TYPE_ITEM;
        }else{
            return TYPE_PACK;
        }
    }

    @Override
    public int getItemCount() {
        return menuInfo==null?0:4;
    }
    public void setOnInfoClickListener(OnInfoClickListener onInfoClickListener){
        this.onInfoClickListener=onInfoClickListener;
    }
}
