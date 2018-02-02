package com.hzjytech.operation.widgets.selectfrags;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.hzjytech.operation.R;
import com.hzjytech.operation.base.BaseActivity;

import java.util.ArrayList;

import static com.hzjytech.operation.constants.Constants.GroupClick.GROUPADDMORE;
import static com.hzjytech.operation.constants.Constants.GroupClick.GROUPSETTING;

/**
 * Created by hehongcan on 2017/9/22.
 * 相同的操作流程，不同的数据传入
 */

public  class SelectActivity<T>  extends BaseActivity implements DisplayFragment.FEditButtonClick,RemoveFragment.FAddButtonClick{

    private ArrayList<T> mData;
    private int mGroupId;
    private String mFragType;
    private int vmId;
    private int mEditOrAdd;
    private boolean mGroupAuth;
    private boolean mMachineAuth;
    private int mMenuId;
    private FragmentManager mFragmentManager;
    private DisplayFragment mDisplayFragment;
    private RemoveFragment<Object> mRemoveFragment;
    private AddFragment mAddFragment;

    @Override
    protected int getResId() {
        return R.layout.activity_more_group;
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        mEditOrAdd = intent.getIntExtra("editOrAdd", -1);
        mGroupId = intent.getIntExtra("groupId", -1);
        mMenuId = intent.getIntExtra("menuId", -1);
        mFragType = intent.getStringExtra("fragType");
        mData = (ArrayList<T>) intent.getParcelableArrayListExtra("data");
        vmId = intent.getIntExtra("vmId", -1);
        mGroupAuth = intent.getBooleanExtra("groupAuth", false);
        mMachineAuth = intent.getBooleanExtra("machineAuth", false);
        mFragmentManager = getSupportFragmentManager();

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        mDisplayFragment = new DisplayFragment();
        mRemoveFragment = new RemoveFragment<>();
        mAddFragment = new AddFragment();
        //编辑页面需要参数无法直接跳转
        if(mEditOrAdd ==GROUPSETTING){
            transaction.replace(R.id.groups_content,mDisplayFragment,"display");
            transaction.commit();
        }else if(mEditOrAdd ==GROUPADDMORE){
            transaction.replace(R.id.groups_content,mAddFragment, "add");
            transaction.commit();
        }
        Log.e("size",getSupportFragmentManager().getBackStackEntryCount()+"");
    }
    public ArrayList<T> getData(){
        if(mData==null){
            mData=new ArrayList<>();
        }
        return mData;
    }
    public int getGroupId(){
        return mGroupId;
    }
    public int getMenuId(){
        return mMenuId;
    }
    public  String getFragsType(){
        return mFragType;
    }
    public int getVmId(){
        return vmId;
    }
    public int getEditOrAdd(){
        return mEditOrAdd;
    }
    public boolean getEditGroupAuth(){
        return mGroupAuth;
    }
    public boolean getEditMachineAuth(){
        return mMachineAuth;
    }
    public FragmentManager getMyFragmentManager(){
        return mFragmentManager;
    }

    public DisplayFragment getDisplayFragment() {
        return mDisplayFragment;
    }


    @Override
    public void onFEditButtonClick() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.groups_content,mRemoveFragment,"remove");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void addButtonClick() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.groups_content,mAddFragment,"add");
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
