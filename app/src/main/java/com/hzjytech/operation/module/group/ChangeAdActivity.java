package com.hzjytech.operation.module.group;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.JsonArray;
import com.hzjytech.operation.R;
import com.hzjytech.operation.adapters.task.DraggableImgGridAdapter;
import com.hzjytech.operation.base.BaseActivity;
import com.hzjytech.operation.entity.GroupInfo;
import com.hzjytech.operation.http.JijiaHttpSubscriber;
import com.hzjytech.operation.http.RxBus;
import com.hzjytech.operation.http.SubscriberOnCompletedListener;
import com.hzjytech.operation.http.SubscriberOnNextListener;
import com.hzjytech.operation.http.UpQiNiuWithCompress;
import com.hzjytech.operation.http.api.GroupApi;
import com.hzjytech.operation.module.task.PicsPageViewActivity;
import com.hzjytech.operation.utils.UserUtils;
import com.hzjytech.operation.widgets.TitleBar;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;

/**
 * Created by liuyuzhi on 2017/9/23.
 */

public class ChangeAdActivity extends BaseActivity implements DraggableImgGridAdapter.OnItemAddListener, DraggableImgGridAdapter.OnItemClickListener, DraggableImgGridAdapter.OnItemDeleteListener {
    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.rv_change_ad)
    RecyclerView mRvChangeAd;
    private GridLayoutManager layoutManager;
    private int photoSizeLimit;
    private DisplayMetrics dm;
    private int imageSize;
    private DraggableImgGridAdapter mGridAdapter;
    private GroupInfo mGroupInfo;
    private int mGroupId;
    private List<LocalMedia> selectList;
    private static final String TAG = "ChangeAdActivity";
    private ArrayList<String> mImages;
    private Subscription mPicSubscriber;
    private ArrayList<String> mMyImages;
    private ArrayList<String> mIsNetImages;
    private UpQiNiuWithCompress mUpQiNiuWithCompress;

    @Override
    protected int getResId() {
        return R.layout.activity_change_ad;
    }

    @Override
    protected void initView() {
        initTitle();
        initRecycler();
        initIntent();

    }

    private void addPics() {
        if (selectList!=null&&selectList.size() == photoSizeLimit) {
            showTip(R.string.choose_photo_limit_out);
            return;
        }
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()
                // 、视频.ofVideo()
                .maxSelectNum(9)// 最大图片选择数量 int
                .minSelectNum(1)// 最小选择数量 int
                .imageSpanCount(4)// 每行显示个数 int
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or
                // PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片 true or false
                //.compressGrade(Luban.THIRD_GEAR)// luban压缩档次，默认3档 Luban.THIRD_GEAR、Luban
                // .FIRST_GEAR、Luban.CUSTOM_GEAR
                .selectionMedia(selectList)// 是否传入已选图片 List<LocalMedia> list
                .isCamera(true)// 是否显示拍照按钮 true or false
                .compress(false)// 是否压缩 true or false
                // .compressMode(PictureConfig.LUBAN_COMPRESS_MODE)//系统自带 or 鲁班压缩 PictureConfig
                // .SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
                .glideOverride(200, 200)// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示 true or false
                .isGif(false)// 是否显示gif图片 true or false
                .openClickSound(false)// 是否开启点击声音 true or false
                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                .cropCompressQuality(90)// 裁剪压缩质量 默认90 int
                .compressMaxKB(500)//压缩最大值kb compressGrade()为Luban.CUSTOM_GEAR有效 int
                .compressWH(200, 200) // 压缩宽高比 compressGrade()为Luban.CUSTOM_GEAR有效  int
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    /**
     * 初始化其他界面传递过来的数据
     */
    private void initIntent() {
        mGroupInfo= (GroupInfo) getIntent().getSerializableExtra("groupInfo");
        mGroupId = getIntent().getIntExtra("groupId", -1);
        // TODO: 2017/9/23 加入传递过来的图片
        selectList=new ArrayList<>();
        List<String> pics = mGroupInfo.getAdPics();
        for (String pic : pics) {
            selectList.add(new LocalMedia(pic,0,0,""));
        }
        mGridAdapter.setList((ArrayList<LocalMedia>) selectList);

    }

    private void initRecycler() {
        layoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        photoSizeLimit = 9;
        dm = getResources().getDisplayMetrics();
        imageSize = (int) ((dm.widthPixels - 62 * dm.density) / 3);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mGridAdapter = new DraggableImgGridAdapter(this, null, imageSize, photoSizeLimit);
        mGridAdapter.setOnItemAddListener(this);
        mGridAdapter.setOnItemClickListener(this);
        mGridAdapter.setOnItemDeleteListener(this);
        //adapter = dragDropManager.createWrappedAdapter(gridAdapter);
        mRvChangeAd.setLayoutManager(layoutManager);
        mRvChangeAd.setAdapter(mGridAdapter);

    }

    private void initTitle() {
        mTitleBar.setTitle(R.string.set_add);
        mTitleBar.setLeftText(R.string.cancel);
        mTitleBar.setLeftTextColor(Color.WHITE);
        mTitleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitleBar.addAction(new TitleBar.TextAction("完成") {
            @Override
            public void performAction(View view) {
             commitResult();
            }
        });

    }

    /**
     * 提交广告修改结果
     */
    private void commitResult() {
        if(mImages==null){
            mImages = new ArrayList<>();
        }else{
            mImages.clear();
        }
        if(mIsNetImages==null){
            mIsNetImages = new ArrayList<>();
        }else{
            mIsNetImages.clear();
        }
        if(mMyImages==null){
            mMyImages = new ArrayList<>();
        }else{
            mMyImages.clear();
        }
        for (LocalMedia localMedia : selectList) {
            mImages.add(localMedia.getPath());
        }
        for (String image : mImages) {
            if(image.startsWith("http")){
                mIsNetImages.add(image);
            }
        }
        mMyImages.addAll(mImages);
        mMyImages.removeAll(mIsNetImages);
        if(!(mMyImages.size()>0)){
            ArrayList<String> fileNames = new ArrayList<String>();
            for (String isNetImage : mIsNetImages) {
                fileNames.add(isNetImage.substring(isNetImage.lastIndexOf("/"),isNetImage.length()));
            }
            commit(fileNames);
        }
        if(mUpQiNiuWithCompress==null){
            mUpQiNiuWithCompress = new UpQiNiuWithCompress(this, true);
        }
        mUpQiNiuWithCompress.upLoadPic(mMyImages);
        mPicSubscriber = RxBus.getDefault()
                .toObservable()
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        if (o instanceof ArrayList) {
                            ArrayList<String> fileNames = new ArrayList<String>();
                            for (String isNetImage : mIsNetImages) {
                                fileNames.add(isNetImage.substring(isNetImage.lastIndexOf("/"),isNetImage.length()));
                            }
                            fileNames.addAll((ArrayList<String>)o);
                            commit(fileNames);
                        }
                    }
                });
    }

    private void commit(List<String>fileNames) {
        if(mPicSubscriber!=null&&!mPicSubscriber.isUnsubscribed()){
            mPicSubscriber.unsubscribe();
        }
        JsonArray jsonArray = new JsonArray();
        for (String fileName : fileNames) {
            jsonArray.add(fileName);
        }
        Observable<Object> observable = GroupApi.updateAdPicDb(UserUtils.getUserInfo()
                .getToken(), mGroupId, jsonArray.toString()).doOnSubscribe(new Action0() {
            @Override
            public void call() {
                //mProgressDlg.show();
            }
        }).subscribeOn(AndroidSchedulers.mainThread());
        JijiaHttpSubscriber subscriber = JijiaHttpSubscriber.buildSubscriber(this)
                .setOnNextListener(new SubscriberOnNextListener() {
                    @Override
                    public void onNext(Object o) {
                        showTip(R.string.commit_success);
                        setResult(RESULT_OK,new Intent());
                    }
                }).setOnCompletedListener(new SubscriberOnCompletedListener() {
                    @Override
                    public void onCompleted() {
                       // mProgressDlg.cancel();
                        ChangeAdActivity.this.finish();
                    }
                })
                .build();
        observable.subscribe(subscriber);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void onItemAdd() {
        addPics();
    }

    @Override
    public void onItemClick(int adapterPosition, LocalMedia item) {
        if(mImages==null){
            mImages = new ArrayList<>();
        }else{
            mImages.clear();
        }
        for (LocalMedia localMedia : selectList) {
            mImages.add(localMedia.getPath());
        }
        Intent intent = new Intent(this, PicsPageViewActivity.class);
        intent.putStringArrayListExtra("images", mImages);
        intent.putExtra("position",adapterPosition);
        startActivity(intent);
    }

    @Override
    public void onItemDelete(int position) {
        selectList.remove(position);
        mGridAdapter.notifyItemRemoved(position);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    mGridAdapter.setList((ArrayList<LocalMedia>) selectList);
                    Log.e(TAG, "onActivityResult: " + selectList.size());
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
