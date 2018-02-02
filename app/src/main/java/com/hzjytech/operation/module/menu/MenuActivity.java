package com.hzjytech.operation.module.menu;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.PtrHandler;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.hzjytech.operation.R;
import com.hzjytech.operation.adapters.menu.MyMenuAdapter;
import com.hzjytech.operation.base.BaseActivity;
import com.hzjytech.operation.constants.BusMessage;
import com.hzjytech.operation.entity.GroupInfo;
import com.hzjytech.operation.entity.MenuInfo;
import com.hzjytech.operation.entity.User;
import com.hzjytech.operation.http.JijiaHttpSubscriber;
import com.hzjytech.operation.http.RxBus;
import com.hzjytech.operation.http.SubscriberOnNextListener;
import com.hzjytech.operation.http.api.MachinesApi;
import com.hzjytech.operation.inter.MoreMachineClickListener;
import com.hzjytech.operation.inter.MorePackClickListener;
import com.hzjytech.operation.inter.MoreSingleItemClickListener;
import com.hzjytech.operation.inter.OnInfoClickListener;
import com.hzjytech.operation.module.group.DetailGroupActivity;
import com.hzjytech.operation.module.home.MorePackItemActivity;
import com.hzjytech.operation.module.home.MoreSingleItemActivity;
import com.hzjytech.operation.utils.UserUtils;
import com.hzjytech.operation.widgets.TitleBar;
import com.hzjytech.operation.widgets.selectfrags.SelectActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.hzjytech.operation.constants.Constants.EditType.EDIT_MACHINE_FROM_GROUP;
import static com.hzjytech.operation.constants.Constants.EditType.EDIT_MACHINE_FROM_MENU;
import static com.hzjytech.operation.constants.Constants.GroupClick.GROUPADDMORE;
import static com.hzjytech.operation.constants.Constants.GroupClick.GROUPSETTING;
import static com.hzjytech.operation.constants.Constants.GroupRefresh.GROUPREFRESH;
import static com.hzjytech.operation.constants.Constants.MenuAuth.AUTH_DELET_ADD;
import static com.hzjytech.operation.constants.Constants.MenuAuth.AUTH_EDIT;
import static com.hzjytech.operation.constants.Constants.MenuAuth.AUTH_MACHINE;
import static com.hzjytech.operation.constants.Constants.MenuClick.ADD_MACHINE;
import static com.hzjytech.operation.constants.Constants.MenuClick.ADD_MENU_ITEM;
import static com.hzjytech.operation.constants.Constants.MenuClick.ADD_MENU_PACK;
import static com.hzjytech.operation.constants.Constants.MenuClick.EDIT_MACHINE;
import static com.hzjytech.operation.constants.Constants.MenuClick.EDIT_MENU_PACK;
import static com.hzjytech.operation.constants.Constants.MenuClick.EDI_MENU_ITEM;

/**
 * Created by hehongcan on 2017/5/3.
 */
public class MenuActivity extends BaseActivity implements OnInfoClickListener {
    private static final int REQUEST_MACHINE_EDIT = 100;
    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.rv_menu)
    RecyclerView rvMenu;
    @BindView(R.id.pcfl_menu)
    PtrClassicFrameLayout pcflMenu;
    private MyMenuAdapter menuAdapter;
    private RecyclerAdapterWithHF mAdapter;
    private int menuId;
    private PtrHandler ptrDefaultHandler=new PtrDefaultHandler() {
        @Override
        public void onRefreshBegin(PtrFrameLayout frame) {
            initData();
        }
    };
    private OnLoadMoreListener onLoadMoreListener;
    private MenuInfo menuInfo;
    private boolean addOrDeletAuth;
    private boolean editAuth;
    private Subscription mSubscribe;

    @Override
    protected int getResId() {
        return R.layout.activity_menu;
    }

    @Override
    protected void initView() {
        initTitle();
        initRecyclerView();
        initPtrc();
        initData();
        initListeners();
    }
// TODO: 2017/5/3  
    /**
     * 初始化各种监听,记得根据getIntent确定传入的路径
     */
    private void initListeners() {
       menuAdapter.setOnInfoClickListener(this);
        mSubscribe = RxBus.getDefault()
                .toObservable()
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        if ((o instanceof BusMessage)||o .equals(GROUPREFRESH)) {
                            if (pcflMenu != null) {
                                pcflMenu.autoRefresh();
                            }
                        }
                    }
                });
    }


    /**
     * 初始化下拉刷新、上拉加载更多
     */
    private void initPtrc() {
       pcflMenu.setPtrHandler(ptrDefaultHandler);//设置下拉监听
        pcflMenu .setOnLoadMoreListener(onLoadMoreListener);//设置上拉监听
        pcflMenu.setLoadMoreEnable(false);//设置可以加载更多
    }

    /**
     * 初始化recyclerview
     */
    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvMenu.setLayoutManager(linearLayoutManager);
        menuAdapter = new MyMenuAdapter(this, null);
        mAdapter = new RecyclerAdapterWithHF(menuAdapter);
        rvMenu.setAdapter(mAdapter);

    }

    /**
     * 初始化数据
     */
    private void initData() {
        User userInfo = UserUtils.getUserInfo();
        Intent intent = getIntent();
        menuId = intent.getIntExtra("menuId", -1);
        Observable<MenuInfo> observable = MachinesApi.getMenuInfo(userInfo.getToken(), menuId);
        JijiaHttpSubscriber subscriber = JijiaHttpSubscriber.buildSubscriber(this).setOnNextListener(new SubscriberOnNextListener<MenuInfo>() {
            @Override
            public void onNext(MenuInfo menuInfo) {
                MenuActivity.this.menuInfo=menuInfo;
                resolveAuth(menuInfo.getAuth());
                 menuAdapter.setMenuData(menuInfo);
                 titleBar.setTitle(menuInfo.getName());
            }
        }).setProgressDialog(pcflMenu.isRefreshing()||pcflMenu.isLoadingMore() ? null : mProgressDlg).setPtcf(pcflMenu).build();
        observable.subscribe(subscriber);
    }
    /**
     * 处理权限问题
     * @param auth
     */
    private void resolveAuth(List<String> auth) {
        if(auth.contains(AUTH_DELET_ADD)){
            addOrDeletAuth=true;

        }else {
            addOrDeletAuth=false;
        }
        if(auth.contains(AUTH_EDIT)){
            editAuth=true;
        }else {
            editAuth=false;
        }
    }

    /**
     * 初始化标题栏
     */
    private void initTitle() {
        titleBar.setLeftImageResource(R.drawable.icon_left);
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleBar.setTitleColor(Color.WHITE);
        titleBar.setTitleBold(true);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void click(int type) {
        switch (type){
            case ADD_MACHINE:
                addMachine();
                break;
            case EDIT_MACHINE:
                editMachine();
                break;
            case ADD_MENU_ITEM:
                addMenuItem();
                break;
            case EDI_MENU_ITEM:
                moreMenuItem();
                break;
            case ADD_MENU_PACK:
                addMenuPack();
                break;
            case EDIT_MENU_PACK:
                moreMenuPack();
                break;
        }
    }

    private void addMenuItem() {
        Intent intent = new Intent(this, EditMenuItemActivity.class);
        intent.putExtra("addOrDeletAuth",addOrDeletAuth);
        intent.putExtra("editAuth",editAuth);
        intent.putExtra("vmId",menuInfo.getVmTypeId());
        intent.putExtra("isAdd",true);
        intent.putExtra("menuInfo",menuInfo);
        intent.putExtra("menuId",menuId);
        startActivity(intent);
    }

    private void addMenuPack() {
        Intent intent = new Intent(this, EditPackActivity.class);
        intent.putExtra("menuInfo", menuInfo);
        intent.putExtra("addOrDeletAuth", addOrDeletAuth);
        intent.putExtra("editAuth", editAuth);
        intent.putExtra("isAdd",true);
        intent.putExtra("menuId",menuId);
        startActivity(intent);
    }

    private void moreMenuPack() {
        Intent intent = new Intent(MenuActivity.this, MorePackItemActivity.class);
        intent.putExtra("menuId",menuId);
        startActivity(intent);
    }

    private void moreMenuItem() {
        Intent intent = new Intent(MenuActivity.this, MoreSingleItemActivity.class);
        intent.putExtra("menuId",menuId);
        startActivity(intent);
    }

    private void addMachine() {
        Intent intent = new Intent(this, SelectActivity.class);
        intent.putExtra("fragType",EDIT_MACHINE_FROM_MENU);
        intent.putExtra("editOrAdd",GROUPADDMORE);
        intent.putExtra("menuId",menuId);
        intent.putExtra("vmId",menuInfo.getVmTypeId());
        intent.putParcelableArrayListExtra("data",
                (ArrayList<MenuInfo.SubMachinesBean>) menuInfo.getSubMachines());
        intent.putExtra("machineAuth",menuInfo.getAuth().contains(AUTH_MACHINE));
        startActivityForResult(intent,REQUEST_MACHINE_EDIT);
    }

    /**
     * 编辑包含的信息
     */
    private void editMachine() {
        Intent intent = new Intent(this, SelectActivity.class);
        intent.putExtra("fragType",EDIT_MACHINE_FROM_MENU);
        intent.putExtra("editOrAdd",GROUPSETTING);
        intent.putExtra("menuId",menuId);
        intent.putExtra("vmId",menuInfo.getVmTypeId());
        intent.putParcelableArrayListExtra("data",
                (ArrayList<MenuInfo.SubMachinesBean>) menuInfo.getSubMachines());
        intent.putExtra("machineAuth",menuInfo.getAuth().contains(AUTH_MACHINE));
        startActivityForResult(intent,REQUEST_MACHINE_EDIT);
    }
}
