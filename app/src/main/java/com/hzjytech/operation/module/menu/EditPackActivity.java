package com.hzjytech.operation.module.menu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.bumptech.glide.Glide;
import com.hzjytech.crop.CropImage;
import com.hzjytech.crop.CropImageView;
import com.hzjytech.operation.R;
import com.hzjytech.operation.base.BaseActivity;
import com.hzjytech.operation.entity.AddPackResult;
import com.hzjytech.operation.entity.MenuInfo;
import com.hzjytech.operation.entity.PackItemResult;
import com.hzjytech.operation.http.JijiaHttpSubscriber;
import com.hzjytech.operation.http.RxBus;
import com.hzjytech.operation.http.SubscriberOnNextListener;
import com.hzjytech.operation.http.UpQiNiuWithCompress;
import com.hzjytech.operation.http.api.MenuApi;
import com.hzjytech.operation.utils.CommonUtil;
import com.hzjytech.operation.utils.MyMath;
import com.hzjytech.operation.utils.TimeUtil;
import com.hzjytech.operation.utils.UserUtils;
import com.hzjytech.operation.widgets.TitleBar;
import com.hzjytech.operation.widgets.dialogs.ITwoButtonClick;
import com.hzjytech.operation.widgets.dialogs.TitleButtonsDialog;
import com.hzjytech.operation.widgets.switchbutton.SwitchButton;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

import static com.hzjytech.operation.constants.BusMessage.REFRESH;
import static com.hzjytech.operation.module.menu.EditInfoActivity.REQUEST_ALI_PRICE;
import static com.hzjytech.operation.module.menu.EditInfoActivity.REQUEST_CH_NAME;
import static com.hzjytech.operation.module.menu.EditInfoActivity.REQUEST_EN_NAME;
import static com.hzjytech.operation.module.menu.EditInfoActivity.REQUEST_NOW_PRICE;
import static com.hzjytech.operation.module.menu.EditInfoActivity.REQUEST_WEIXIN_PRICE;

/**
 * Created by hehongcan on 2017/9/25.
 */

public class EditPackActivity extends BaseActivity {
    private static final int REQUEST_CHANGETIME = 121;
    private static final int REQUEST_COFFEES_CHANGE = 122;
    private static final int REQUEST_COFFEES_ADD = 123;
    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.sb_pack_sale_statu)
    SwitchButton mSbPackSaleStatu;
    @BindView(R.id.tv_pack_sale_statu)
    TextView mTvPackSaleStatu;
    @BindView(R.id.iv_pack_drink_item)
    ImageView mIvPackDrinkItem;
    @BindView(R.id.iv_pack_pic_drink_right)
    ImageView mIvPackPicDrinkRight;
    @BindView(R.id.tv_pack_chinese_name)
    TextView mTvPackChineseName;
    @BindView(R.id.iv_pack_chinese_name)
    ImageView mIvPackChineseName;
    @BindView(R.id.tv_pack_english_name)
    TextView mTvPackEnglishName;
    @BindView(R.id.iv_pack_english_name)
    ImageView mIvPackEnglishName;
    @BindView(R.id.tv_pack_now_price)
    TextView mTvPackNowPrice;
    @BindView(R.id.iv_pack_now_price)
    ImageView mIvPackNowPrice;
    @BindView(R.id.tv_pack_weixin_price)
    TextView mTvPackWeixinPrice;
    @BindView(R.id.iv_pack_weixin_price)
    ImageView mIvPackWeixinPrice;
    @BindView(R.id.tv_pack_ali_price)
    TextView mTvPackAliPrice;
    @BindView(R.id.iv_pack_ali_price)
    ImageView mIvPackAliPrice;
    @BindView(R.id.tv_pack_first)
    TextView mTvPackFirst;
    @BindView(R.id.iv_pack_item_first)
    ImageView mIvPackItemFirst;
    @BindView(R.id.tv_pack_second)
    TextView mTvPackSecond;
    @BindView(R.id.iv_pack_item_second)
    ImageView mIvPackItemSecond;
    @BindView(R.id.tv_pack_third)
    TextView mTvPackThird;
    @BindView(R.id.iv_pack_item_third)
    ImageView mIvPackItemThird;
    @BindView(R.id.tv_pack_four)
    TextView mTvPackFour;
    @BindView(R.id.iv_pack_item_four)
    ImageView mIvPackItemFour;
    @BindView(R.id.tv_pack_five)
    TextView mTvPackFive;
    @BindView(R.id.iv_pack_item_five)
    ImageView mIvPackItemFive;
    @BindView(R.id.btnPackDelete)
    Button mBtnPackDelete;
    @BindView(R.id.rl_pack_change_drink_pic)
    RelativeLayout mRlPackChangeDrinkPic;
    @BindView(R.id.rl_pack_chinese_name)
    RelativeLayout mRlPackChineseName;
    @BindView(R.id.rl_pack_english_name)
    RelativeLayout mRlPackEnglishName;
    @BindView(R.id.rl_pack_now_price)
    RelativeLayout mRlPackNowPrice;
    @BindView(R.id.rl_pack_weixin_price)
    RelativeLayout mRlPackWeixinPrice;
    @BindView(R.id.rl_pack_ali_price)
    RelativeLayout mRlPackAliPrice;
    @BindView(R.id.rl_pack_item_first)
    RelativeLayout mRlPackItemFirst;
    @BindView(R.id.rl_pack_item_second)
    RelativeLayout mRlPackItemSecond;
    @BindView(R.id.rl_pack_item_third)
    RelativeLayout mRlPackItemThird;
    @BindView(R.id.rl_pack_item_four)
    RelativeLayout mRlPackItemFour;
    @BindView(R.id.rl_pack_item_five)
    RelativeLayout mRlPackItemFive;
    @BindView(R.id.tv_pack_open_time)
    TextView mTvPackOpenTime;
    @BindView(R.id.iv_pack_open_time)
    ImageView mIvPackOpenTime;
    @BindView(R.id.rl_pack_open_time)
    RelativeLayout mRlPackOpenTime;
    @BindView(R.id.tv_pack_close_time)
    TextView mTvPackCloseTime;
    @BindView(R.id.iv_pack_close_time)
    ImageView mIvPackCloseTime;
    @BindView(R.id.rl_pack_close_time)
    RelativeLayout mRlPackCloseTime;
    private boolean mAddOrDeletAuth;
    private boolean mEditAuth;
    private MenuInfo.PacksBean mData;
    private MenuInfo mMenuInfo;
    private boolean mPicChanged = false;
    private Subscription mSubscribe;
    private boolean mIsAdd;
    private int mIndex;
    private int mMenuId;
    private TitleBar.TextAction mTextAction;

    @Override
    protected int getResId() {
        return R.layout.activity_edit_packs;
    }

    @Override
    protected void initView() {
        initIntent();
        initTitle();
        initData();
    }

    private void initTitle() {
        mTitleBar.setLeftTextColor(Color.WHITE);
        mTitleBar.setLeftText(R.string.cancel);
        mTitleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (mIsAdd) {
            mTitleBar.setTitle(R.string.add_pack);
        } else {
            mTitleBar.setTitle(R.string.edit_pack);
        }
        mTextAction = new TitleBar.TextAction("完成") {
            @Override
            public void performAction(View view) {
                commitAnser();
            }
        };
        mTitleBar.addAction(mTextAction);
    }

    /**
     * 提交结果
     */
    private void commitAnser() {
        if (mPicChanged) {
            UpQiNiuWithCompress upQiNiuWithCompress = new UpQiNiuWithCompress(this,true);
            ArrayList<String> photo = new ArrayList<>();
            photo.add(mData.getUrl());
            upQiNiuWithCompress.upLoadPic(photo);
            mSubscribe = RxBus.getDefault()
                    .toObservable()
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object event) {
                            if (event instanceof ArrayList) {
                                mData.setFileName(((ArrayList<String>) event).get(0));
                                commit();
                            }
                        }
                    });
        } else {
            String url = mData.getUrl();
            try {
                String fileName = url.substring(url.lastIndexOf("/")+1, url.length());
                mData.setFileName(fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            commit();
        }

    }

    /**
     * 实际提交网络请求位置
     */
    private void commit() {
        if(mSubscribe!=null){
            mSubscribe.unsubscribe();
        }
        if(!checkParams()){
            return;
        }
        List<MenuInfo.PacksBean.CoffeesBean> coffees = mData.getCoffees();
        String idString="";
        String countString="";
        for (MenuInfo.PacksBean.CoffeesBean coffee : coffees) {
            idString+=coffee.getItemId()+",";
            countString+=coffee.getCount()+",";
        }
        if(!idString.equals("")){
            mData.setItemIds(idString.substring(0,idString.length()-1));
        }else{
            mData.setItemIds("");
        }
        if(!countString.equals("")){
            mData.setItemCount(countString.substring(0,countString.length()-1));
        }else{
            mData.setItemCount("");
        }
          if(mIsAdd){
              mData.setSequence(mMenuInfo.getPacks().size());
              creatPack();
          }else{
              editPack();
          }
    }

    /**
     * 编辑套餐
     */
    private void editPack() {
        Observable<Object> observable = MenuApi.changeMenuPack(UserUtils.getUserInfo()
                .getToken(), new PackItemResult(mData.getId(), mData));
        JijiaHttpSubscriber subscriber = JijiaHttpSubscriber.buildSubscriber(this)
                .setOnNextListener(new SubscriberOnNextListener() {
                    @Override
                    public void onNext(Object o) {
                      showTip(R.string.commit_success);
                        RxBus.getDefault().send(REFRESH);
                        finish();
                    }
                })
                .build();
        observable.subscribe(subscriber);
    }

    /**
     * 创建套餐
     */
    private void creatPack() {
        Observable<Object> observable = MenuApi.creatMenuPack(UserUtils.getUserInfo()
                .getToken(), new AddPackResult(mMenuId, mMenuInfo.getVmTypeId(), mData));
        JijiaHttpSubscriber subscriber = JijiaHttpSubscriber.buildSubscriber(this)
                .setOnNextListener(new SubscriberOnNextListener() {
                    @Override
                    public void onNext(Object o) {
                        showTip(R.string.add_success);
                        RxBus.getDefault().send(REFRESH);
                        finish();
                    }
                })
                .build();
        observable.subscribe(subscriber);
    }

    /**
     * 删除当前套餐，删除完finish
     */
    private void deletPack() {
        Observable<Object> observable = MenuApi.destroyItem(UserUtils.getUserInfo()
                .getToken(), mData.getId());
        JijiaHttpSubscriber subscriber = JijiaHttpSubscriber.buildSubscriber(this)
                .setOnNextListener(new SubscriberOnNextListener() {
                    @Override
                    public void onNext(Object o) {
                        showTip(R.string.delete_success);
                        RxBus.getDefault()
                                .send(REFRESH);
                        finish();
                    }
                })
                .build();
        observable.subscribe(subscriber);
    }

    private void initData() {
        if (mData == null) {
            mData = new MenuInfo.PacksBean();
            ArrayList<MenuInfo.PacksBean.CoffeesBean> coffees = new ArrayList<>();
            mData.setCoffees(coffees);
        }
        Glide.with(this)
                .load(mData.getUrl())
                .fitCenter()
                .placeholder(R.drawable.bg_photo)
                .dontAnimate()
                .into(mIvPackDrinkItem);
        mTvPackChineseName.setText(mData.getNameCh());
        mTvPackEnglishName.setText(mData.getNameEn());
        if(mIsAdd){
            mTvPackNowPrice.setText(mData.getNowPrice()==0?"":MyMath.getIntOrDouble(mData.getNowPrice()) + "元");
            mTvPackWeixinPrice.setText(mData.getWxpayPrice()==0?"":MyMath.getIntOrDouble(mData.getWxpayPrice()) + "元");
            mTvPackAliPrice.setText(mData.getAlipayPrice()==0?"":MyMath.getIntOrDouble(mData.getAlipayPrice()) + "元");
        }else {
            mTvPackNowPrice.setText(MyMath.getIntOrDouble(mData.getNowPrice()) + "元");
            mTvPackWeixinPrice.setText(MyMath.getIntOrDouble(mData.getWxpayPrice()) + "元");
            mTvPackAliPrice.setText(MyMath.getIntOrDouble(mData.getAlipayPrice()) + "元");
        }

        mTvPackOpenTime.setText(mData.getOpeningTime());
        mTvPackCloseTime.setText(mData.getClosingTime());
        List<TextView> coffeesViews = new ArrayList<>();
        coffeesViews.add(mTvPackFirst);
        coffeesViews.add(mTvPackSecond);
        coffeesViews.add(mTvPackThird);
        coffeesViews.add(mTvPackFour);
        coffeesViews.add(mTvPackFive);
        for (TextView coffeesView : coffeesViews) {
            if(mEditAuth){
                coffeesView.setText("");
            }else{
                coffeesView.setText(" ");
            }

        }
        for (int i = 0; i < mData.getCoffees()
                .size(); i++) {
            MenuInfo.PacksBean.CoffeesBean coffeesBean = mData.getCoffees()
                    .get(i);
            coffeesViews.get(i)
                    .setText(coffeesBean.getItemName() + "*" + coffeesBean.getCount());
        }


        if (mEditAuth) {
            mTvPackSaleStatu.setVisibility(View.GONE);
            mSbPackSaleStatu.setVisibility(View.VISIBLE);
            mSbPackSaleStatu.setChecked(mData == null ? false : mData.isSaleStatus());
            mSbPackSaleStatu.setOnCheckedChangeListener(new CompoundButton
                    .OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mData.setSaleStatus(isChecked);
                }
            });

        } else {
            mTitleBar.removeAction(mTextAction);
            mTvPackSaleStatu.setVisibility(View.VISIBLE);
            mSbPackSaleStatu.setVisibility(View.GONE);
            mTvPackSaleStatu.setText((mData != null && mData.isSaleStatus()) ? "在售" : "售罄");
            mIvPackPicDrinkRight.setVisibility(View.INVISIBLE);
            mIvPackChineseName.setVisibility(View.INVISIBLE);
            mIvPackEnglishName.setVisibility(View.INVISIBLE);
            mIvPackNowPrice.setVisibility(View.INVISIBLE);
            mIvPackWeixinPrice.setVisibility(View.INVISIBLE);
            mIvPackAliPrice.setVisibility(View.INVISIBLE);
            mIvPackItemFirst.setVisibility(View.INVISIBLE);
            mIvPackItemSecond.setVisibility(View.INVISIBLE);
            mIvPackItemThird.setVisibility(View.INVISIBLE);
            mIvPackItemFour.setVisibility(View.INVISIBLE);
            mIvPackItemFive.setVisibility(View.INVISIBLE);
            mIvPackOpenTime.setVisibility(View.INVISIBLE);
            mIvPackCloseTime.setVisibility(View.INVISIBLE);
            mRlPackAliPrice.setClickable(false);
            mRlPackChangeDrinkPic.setClickable(false);
            mRlPackChineseName.setClickable(false);
            mRlPackEnglishName.setClickable(false);
            mRlPackItemFirst.setClickable(false);
            mRlPackNowPrice.setClickable(false);
            mRlPackWeixinPrice.setClickable(false);
            mRlPackItemSecond.setClickable(false);
            mRlPackItemThird.setClickable(false);
            mRlPackItemFour.setClickable(false);
            mRlPackItemFive.setClickable(false);
            mRlPackOpenTime.setClickable(false);
            mRlPackCloseTime.setClickable(false);
        }
        if (mAddOrDeletAuth&&!mIsAdd) {
            mBtnPackDelete.setVisibility(View.VISIBLE);
        } else {
            mBtnPackDelete.setVisibility(View.GONE);
        }
    }

    private void initIntent() {
        Intent intent = getIntent();
        mData = intent.getParcelableExtra("data");
        mMenuInfo = intent.getParcelableExtra("menuInfo");
        mAddOrDeletAuth = intent.getBooleanExtra("addOrDeletAuth", false);
        mEditAuth = intent.getBooleanExtra("editAuth", false);
        mIsAdd = intent.getBooleanExtra("isAdd", false);
        mMenuId = intent.getIntExtra("menuId", -1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.rl_pack_change_drink_pic, R.id.rl_pack_chinese_name, R.id
            .rl_pack_english_name, R.id.rl_pack_now_price, R.id
            .rl_pack_weixin_price, R.id.rl_pack_ali_price, R.id.rl_pack_item_first, R.id
            .rl_pack_item_second, R.id.rl_pack_item_third, R.id.rl_pack_item_four, R.id
            .rl_pack_item_five, R.id.btnPackDelete, R.id.rl_pack_open_time, R.id
            .rl_pack_close_time})
    public void onViewClicked(View view) {
        if(!mEditAuth){
            return;
        }
        Intent intent = new Intent(this, EditInfoActivity.class);
        Intent intent1 = new Intent(this, EditCoffeeItemActivity.class);
        List<MenuInfo.PacksBean.CoffeesBean> coffees = mData.getCoffees();
        switch (view.getId()) {
            case R.id.rl_pack_change_drink_pic:
                choosePic();
                break;
            case R.id.rl_pack_chinese_name:
                intent.putExtra("packData", mData.getNameCh());
                intent.putExtra("type", REQUEST_CH_NAME);
                intent.putExtra("isFromPack", true);
                startActivityForResult(intent, REQUEST_CH_NAME);
                break;
            case R.id.rl_pack_english_name:
                intent.putExtra("packData", mData.getNameEn());
                intent.putExtra("type", REQUEST_EN_NAME);
                intent.putExtra("isFromPack", true);
                startActivityForResult(intent, REQUEST_EN_NAME);
                break;

            case R.id.rl_pack_now_price:
                intent.putExtra("packData", mData.getNowPrice()+"");
                intent.putExtra("type", REQUEST_NOW_PRICE);
                intent.putExtra("isFromPack", true);
                startActivityForResult(intent, REQUEST_NOW_PRICE);
                break;
            case R.id.rl_pack_weixin_price:
                intent.putExtra("packData", mData.getWxpayPrice()+"");
                intent.putExtra("type", REQUEST_WEIXIN_PRICE);
                intent.putExtra("isFromPack", true);
                startActivityForResult(intent, REQUEST_WEIXIN_PRICE);
                break;
            case R.id.rl_pack_ali_price:
                intent.putExtra("packData", mData.getAlipayPrice()+"");
                intent.putExtra("type", REQUEST_ALI_PRICE);
                intent.putExtra("isFromPack", true);
                startActivityForResult(intent, REQUEST_ALI_PRICE);
                break;
            case R.id.rl_pack_item_first:
                mIndex=0;
                intent1.putExtra("menuInfo", mMenuInfo);
                if(coffees!=null&&coffees.size()>0){
                    intent1.putExtra("data", coffees.get(0));
                    startActivityForResult(intent1, REQUEST_COFFEES_CHANGE);
                }else{
                    startActivityForResult(intent1, REQUEST_COFFEES_ADD);
                }

                break;
            case R.id.rl_pack_item_second:
                mIndex=1;
                intent1.putExtra("menuInfo", mMenuInfo);
                if(coffees!=null&&coffees.size()>1){
                    intent1.putExtra("data", coffees.get(1));
                    startActivityForResult(intent1, REQUEST_COFFEES_CHANGE);
                }else{
                    startActivityForResult(intent1, REQUEST_COFFEES_ADD);
                }
                break;
            case R.id.rl_pack_item_third:
                mIndex=2;
                intent1.putExtra("menuInfo", mMenuInfo);
                if(coffees!=null&&coffees.size()>2){
                    intent1.putExtra("data", coffees.get(2));
                    startActivityForResult(intent1, REQUEST_COFFEES_CHANGE);
                }else{
                    startActivityForResult(intent1, REQUEST_COFFEES_ADD);
                }
                break;
            case R.id.rl_pack_item_four:
                mIndex=3;
                intent1.putExtra("menuInfo", mMenuInfo);
                if(coffees!=null&&coffees.size()>3){
                    intent1.putExtra("data", coffees.get(3));
                    startActivityForResult(intent1, REQUEST_COFFEES_CHANGE);
                }else{
                    startActivityForResult(intent1, REQUEST_COFFEES_ADD);
                }
                break;
            case R.id.rl_pack_item_five:
                mIndex=4;
                intent1.putExtra("menuInfo", mMenuInfo);
                if(coffees!=null&&coffees.size()>4){
                    intent1.putExtra("data", coffees.get(4));
                    startActivityForResult(intent1, REQUEST_COFFEES_CHANGE);
                }else{
                    startActivityForResult(intent1, REQUEST_COFFEES_ADD);
                }
                break;
            case R.id.btnPackDelete:
                TitleButtonsDialog descCenterDialog = TitleButtonsDialog.newInstance("确定删除当前套餐?",
                        "取消",
                        "确定");
                descCenterDialog.setOnTwoClickListener(new ITwoButtonClick() {
                    @Override
                    public void onLeftButtonClick() {

                    }

                    @Override
                    public void onRightButtonClick() {
                        deletPack();
                    }
                });
                descCenterDialog.show(getSupportFragmentManager(), "desc");

                break;
            case R.id.rl_pack_open_time:
                TimePickerView.Builder builder = new TimePickerView.Builder(this,
                        new TimePickerView.OnTimeSelectListener() {
                            @Override
                            public void onTimeSelect(Date date, View v) {
                                long l = TimeUtil.dateToLong(date);
                                String time = TimeUtil.getLongTime(l);
                                mData.setOpeningTime(time);
                                initData();

                            }
                        });
                TimePickerView pickerView = builder.build();
                pickerView.show();
                break;
            case R.id.rl_pack_close_time:
                TimePickerView.Builder builder2 = new TimePickerView.Builder(this,
                        new TimePickerView.OnTimeSelectListener() {
                            @Override
                            public void onTimeSelect(Date date, View v) {
                                long l = TimeUtil.dateToLong(date);
                                String time = TimeUtil.getLongTime(l);
                                mData.setClosingTime(time);
                                initData();

                            }
                        });
                TimePickerView pickerView2 = builder2.build();
                pickerView2.show();
                break;
        }
    }


    private void choosePic() {
        CropImage.startPickImageActivity(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            switch (requestCode ){
                case CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE:
                    Uri mImageUri = CropImage.getPickImageResultUri(this, data);
                    if (mImageUri != null) {
                        startCropImageActivity(mImageUri);
                    }
                    break;
                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    CropImage.ActivityResult cropResult = CropImage.getActivityResult(data);
                    mData.setUrl(cropResult.getUri().getPath());
                    mPicChanged = true;
                    break;
                case REQUEST_CHANGETIME:
                    String startTime = data.getStringExtra("startTime");
                    String endTime = data.getStringExtra("endTime");
                    mData.setOpeningTime(startTime);
                    mData.setClosingTime(endTime);
                    break;
                case REQUEST_CH_NAME:
                    String chName = data.getStringExtra("packString");
                    mData.setNameCh(chName);
                    break;
                case REQUEST_EN_NAME:
                    String enName= data.getStringExtra("packString");
                    mData.setNameEn(enName);
                    break;
                case REQUEST_NOW_PRICE:
                    String nowPrice = data.getStringExtra("packString");
                    mData.setNowPrice(Float.parseFloat(MyMath.getIntOrDouble(Float.valueOf(nowPrice))));
                    break;
                case REQUEST_WEIXIN_PRICE:
                    String wxPrice = data.getStringExtra("packString");
                    mData.setWxpayPrice(Float.parseFloat(MyMath.getIntOrDouble(Float.valueOf(wxPrice))));
                    break;
                case REQUEST_ALI_PRICE:
                    String aliPrice = data.getStringExtra("packString");
                    mData.setAlipayPrice(Float.parseFloat(MyMath.getIntOrDouble(Float.valueOf(aliPrice))));
                    break;
                case REQUEST_COFFEES_ADD:
                    MenuInfo.PacksBean.CoffeesBean result = data.getParcelableExtra("result");
                   if(result.getCount()!=0){
                       mData.getCoffees().add(result);
                   }
                    break;
                case REQUEST_COFFEES_CHANGE:
                    MenuInfo.PacksBean.CoffeesBean result2 = data.getParcelableExtra("result");
                    if(result2.getCount()==0){
                        mData.getCoffees().remove(mIndex);
                    }else{
                        mData.getCoffees().set(mIndex,result2);
                    }

                    break;
            }
           initData();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }

    @Override
    protected void onDestroy() {
        CommonUtil.unSubscribeSubs(mSubscribe);
        super.onDestroy();
    }
    private boolean checkParams() {
        if(mData.getNameCh().equals("")){
            showTip(R.string.ch_not_empty);
            return false;
        }
        if(mData.getNameEn().equals("")){
            showTip(R.string.en_not_empty);
            return false;
        }
        if(mData.getNowPrice()==0){
            showTip(R.string.discount_price_not_empty);
            return false;
        }
        if(mData.getWxpayPrice()==0){
            showTip(R.string.wx_price_not_empty);
            return false;
        }
        if(mData.getAlipayPrice()==0){
            showTip(R.string.ali_price_not_empty);
            return false;
        }
        if(mData.getOpeningTime().equals("")||mData.getClosingTime().equals("")){
            showTip(R.string.start_end_time_not_empty);
            return false;
        }
        int count=0;
        List<MenuInfo.PacksBean.CoffeesBean> coffees = mData.getCoffees();
        if(coffees==null||coffees.size()==0){
            showTip(R.string.item_type_not_empty);
            return false;
        }
        for (MenuInfo.PacksBean.CoffeesBean coffee : coffees) {
            count+=coffee.getCount();
        }
        if(count>9){
            showTip(R.string.item_count_not_large_nine);
            return false;
        }
        try {
            if(TimeUtil.stringToLong(mData.getOpeningTime(),TimeUtil.DATE_FORMAT_LONG)>TimeUtil.stringToLong(mData.getClosingTime(),TimeUtil.DATE_FORMAT_LONG)){
                showTip(R.string.time_error);
                return false;
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;

    }
}
