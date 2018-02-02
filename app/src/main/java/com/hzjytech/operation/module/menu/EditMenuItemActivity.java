package com.hzjytech.operation.module.menu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.icu.util.HebrewCalendar;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.hzjytech.crop.CropImage;
import com.hzjytech.crop.CropImageView;
import com.hzjytech.operation.R;
import com.hzjytech.operation.base.BaseActivity;
import com.hzjytech.operation.entity.AddItemResult;
import com.hzjytech.operation.entity.ItemResult;
import com.hzjytech.operation.entity.MenuInfo;
import com.hzjytech.operation.entity.RecipeInfo;
import com.hzjytech.operation.http.JijiaHttpSubscriber;
import com.hzjytech.operation.http.RxBus;
import com.hzjytech.operation.http.SubscriberOnNextListener;
import com.hzjytech.operation.http.UpQiNiuWithCompress;
import com.hzjytech.operation.http.api.MenuApi;
import com.hzjytech.operation.utils.MyMath;
import com.hzjytech.operation.utils.StringUtil;
import com.hzjytech.operation.utils.UserUtils;
import com.hzjytech.operation.widgets.TitleBar;
import com.hzjytech.operation.widgets.dialogs.ITwoButtonClick;
import com.hzjytech.operation.widgets.dialogs.TitleButtonsDialog;
import com.hzjytech.operation.widgets.switchbutton.SwitchButton;

import java.util.ArrayList;
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
import static com.hzjytech.operation.module.menu.EditInfoActivity.REQUEST_ORIGIN_PRICE;
import static com.hzjytech.operation.module.menu.EditInfoActivity.REQUEST_SET_ADS;
import static com.hzjytech.operation.module.menu.EditInfoActivity.REQUEST_VOLUME;
import static com.hzjytech.operation.module.menu.EditInfoActivity.REQUEST_WEIXIN_PRICE;

/**
 * Created by hehongcan on 2017/9/25.
 */

public class EditMenuItemActivity extends BaseActivity {

    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.sb_sale_statu)
    SwitchButton mSbSaleStatu;
    @BindView(R.id.tv_sale_statu)
    TextView mTvSaleStatu;
    @BindView(R.id.iv_drink_item)
    ImageView mIvDrinkItem;
    @BindView(R.id.iv_pic_drink_right)
    ImageView mIvPicDrinkRight;
    @BindView(R.id.tv_chinese_name)
    TextView mTvChineseName;
    @BindView(R.id.iv_chinese_name)
    ImageView mIvChineseName;
    @BindView(R.id.rl_chinese_name)
    RelativeLayout mRlChineseName;
    @BindView(R.id.tv_english_name)
    TextView mTvEnglishName;
    @BindView(R.id.iv_english_name)
    ImageView mIvEnglishName;
    @BindView(R.id.rl_english_name)
    RelativeLayout mRlEnglishName;
    @BindView(R.id.tv_volume)
    TextView mTvVolume;
    @BindView(R.id.iv_volume)
    ImageView mIvVolume;
    @BindView(R.id.rl_volume)
    RelativeLayout mRlVolume;
    @BindView(R.id.tv_origin_price)
    TextView mTvOriginPrice;
    @BindView(R.id.iv_origin_price)
    ImageView mIvOriginPrice;
    @BindView(R.id.rl_change_drink_pic)
    RelativeLayout mRlChangeDrinkPic;
    @BindView(R.id.rl_origin_price)
    RelativeLayout mRlOriginPrice;
    @BindView(R.id.tv_now_price)
    TextView mTvNowPrice;
    @BindView(R.id.iv_now_price)
    ImageView mIvNowPrice;
    @BindView(R.id.rl_now_price)
    RelativeLayout mRlNowPrice;
    @BindView(R.id.tv_weixin_price)
    TextView mTvWeixinPrice;
    @BindView(R.id.iv_weixin_price)
    ImageView mIvWeixinPrice;
    @BindView(R.id.rl_weixin_price)
    RelativeLayout mRlWeixinPrice;
    @BindView(R.id.tv_ali_price)
    TextView mTvAliPrice;
    @BindView(R.id.iv_ali_price)
    ImageView mIvAliPrice;
    @BindView(R.id.rl_ali_price)
    RelativeLayout mRlAliPrice;
    @BindView(R.id.tv_set_ads)
    TextView mTvSetAds;
    @BindView(R.id.iv_set_ads)
    ImageView mIvSetAds;
    @BindView(R.id.rl_set_ads)
    RelativeLayout mRlSetAds;
    @BindView(R.id.sb_hot)
    SwitchButton mSbHot;
    @BindView(R.id.tv_hot)
    TextView mTvHot;
    @BindView(R.id.sb_new)
    SwitchButton mSbNew;
    @BindView(R.id.tv_new)
    TextView mTvNew;
    @BindView(R.id.sb_iced)
    SwitchButton mSbIced;
    @BindView(R.id.tv_iced)
    TextView mTvIced;
    @BindView(R.id.sb_sweet)
    SwitchButton mSbSweet;
    @BindView(R.id.tv_sweet)
    TextView mTvSweet;
    @BindView(R.id.btnDelete)
    Button mBtnDelete;
    @BindView(R.id.tv_recipe_name)
    TextView mTvRecipeName;
    @BindView(R.id.iv_recipe_name)
    ImageView mIvRecipeName;
    @BindView(R.id.rl_recipe)
    RelativeLayout mRlRecipe;
    private MenuInfo.ItemsBean mData;
    private boolean mAddOrDeletAuth;
    private boolean mEditAuth;
    private boolean mPicChanged;
    private Subscription mSubscribe;

    private boolean mIsAdd;
    private int mVmId;
    private List<RecipeInfo> infos;
    private List<String> recipeNames;
    private MenuInfo mMenuInfo;
    private int mMenuId;
    private TitleBar.TextAction mTextAction;

    @Override
    protected int getResId() {
        return R.layout.activity_edit_menu_item;
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
            mTitleBar.setTitle(R.string.add_menu_item);
        } else {
            mTitleBar.setTitle(R.string.edit_menu_item);
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
                String fileName = url.substring(url.lastIndexOf("/"), url.length());
                mData.setFileName(fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            commit();
        }

    }

    /**
     * 删除当前item
     */
    private void deletItem() {
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
            mData = new MenuInfo.ItemsBean();
        }
        Glide.with(this)
                .load(mData.getUrl())
                .fitCenter()
                .placeholder(R.drawable.bg_photo)
                .dontAnimate()
                .into(mIvDrinkItem);
        mTvChineseName.setText(mData.getNameCh());
        mTvEnglishName.setText(mData.getNameEn());
        if(mIsAdd){
            mTvOriginPrice.setText(mData.getPrice()==0?"":MyMath.getIntOrDouble(mData.getPrice()) + "元");
            mTvVolume.setText(mData.getVolume()==0?"":MyMath.getIntOrDouble(mData.getVolume()) + "ml");
            mTvNowPrice.setText(mData.getDiscountPrice()==0?"":MyMath.getIntOrDouble(mData.getDiscountPrice()) + "元");
            mTvWeixinPrice.setText(mData.getWxpayPrice()==0?"":MyMath.getIntOrDouble(mData.getWxpayPrice()) + "元");
            mTvAliPrice.setText(mData.getAlipayPrice()==0?"":MyMath.getIntOrDouble(mData.getAlipayPrice()) + "元");
        }else{
            mTvOriginPrice.setText(MyMath.getIntOrDouble(mData.getPrice()) + "元");
            mTvVolume.setText(MyMath.getIntOrDouble(mData.getVolume()) + "ml");
            mTvNowPrice.setText(MyMath.getIntOrDouble(mData.getDiscountPrice()) + "元");
            mTvWeixinPrice.setText(MyMath.getIntOrDouble(mData.getWxpayPrice()) + "元");
            mTvAliPrice.setText(MyMath.getIntOrDouble(mData.getAlipayPrice()) + "元");
        }
        mTvSetAds.setText(mData.getVideoId());
        mTvRecipeName.setText(mData.getRecipeName());
        if (mEditAuth) {
            mTvSaleStatu.setVisibility(View.GONE);
            mSbSaleStatu.setVisibility(View.VISIBLE);
            mSbSaleStatu.setChecked(mData.isSaleStatus());
            mSbSaleStatu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mData.setSaleStatus(isChecked);
                }
            });

            mRlChangeDrinkPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    choosePhoto();
                }
            });

            mRlChineseName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EditMenuItemActivity.this, EditInfoActivity.class);
                    intent.putExtra("data", mData.getNameCh());
                    intent.putExtra("type", REQUEST_CH_NAME);
                    EditMenuItemActivity.this.startActivityForResult(intent, REQUEST_CH_NAME);
                }
            });

            mRlEnglishName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EditMenuItemActivity.this, EditInfoActivity.class);
                    intent.putExtra("data", mData.getNameEn());
                    intent.putExtra("type", REQUEST_EN_NAME);
                    EditMenuItemActivity.this.startActivityForResult(intent, REQUEST_EN_NAME);
                }
            });

            mRlVolume.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EditMenuItemActivity.this, EditInfoActivity.class);
                    intent.putExtra("data", mData.getVolume() + "");
                    intent.putExtra("type", REQUEST_VOLUME);
                    EditMenuItemActivity.this.startActivityForResult(intent, REQUEST_VOLUME);
                }
            });

            mRlOriginPrice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EditMenuItemActivity.this, EditInfoActivity.class);
                    intent.putExtra("data", mData.getPrice() + "");
                    intent.putExtra("type", REQUEST_ORIGIN_PRICE);
                    EditMenuItemActivity.this.startActivityForResult(intent, REQUEST_ORIGIN_PRICE);
                }
            });

            mRlNowPrice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EditMenuItemActivity.this, EditInfoActivity.class);
                    intent.putExtra("data", mData.getDiscountPrice() + "");
                    intent.putExtra("type", REQUEST_NOW_PRICE);
                    EditMenuItemActivity.this.startActivityForResult(intent, REQUEST_NOW_PRICE);
                }
            });

            mRlWeixinPrice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EditMenuItemActivity.this, EditInfoActivity.class);
                    intent.putExtra("data", mData.getWxpayPrice() + "");
                    intent.putExtra("type", REQUEST_WEIXIN_PRICE);
                    EditMenuItemActivity.this.startActivityForResult(intent, REQUEST_WEIXIN_PRICE);
                }
            });

            mRlAliPrice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EditMenuItemActivity.this, EditInfoActivity.class);
                    intent.putExtra("data", mData.getAlipayPrice() + "");
                    intent.putExtra("type", REQUEST_ALI_PRICE);
                    EditMenuItemActivity.this.startActivityForResult(intent, REQUEST_ALI_PRICE);
                }
            });
            mRlSetAds.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EditMenuItemActivity.this, EditInfoActivity.class);
                    intent.putExtra("data", mData.getVideoId());
                    intent.putExtra("type", REQUEST_SET_ADS);
                    EditMenuItemActivity.this.startActivityForResult(intent, REQUEST_SET_ADS);
                }
            });
            mRlRecipe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeRecipeName();
                }
            });


            mTvHot.setVisibility(View.GONE);
            mSbHot.setChecked(mData.isIsHot());
            mSbHot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mData.setIsHot(isChecked);
                }
            });
            mTvNew.setVisibility(View.GONE);
            mSbNew.setChecked(mData.isIsNew());
            mSbNew.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mData.setIsNew(isChecked);
                }
            });
            mTvIced.setVisibility(View.GONE);
            mSbIced.setChecked(mData.isIsIced());
            mSbIced.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mData.setIsIced(isChecked);
                }
            });
            mTvSweet.setVisibility(View.GONE);
            mSbSweet.setChecked(mData.isIsSweet());
            mSbSweet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mData.setIsSweet(isChecked);
                }
            });
        } else {
            mTitleBar.removeAction(mTextAction);
            mTvSaleStatu.setVisibility(View.VISIBLE);
            mSbSaleStatu.setVisibility(View.GONE);
            mTvSaleStatu.setText(mData.isSaleStatus() ? "在售" : "售罄");
            mIvPicDrinkRight.setVisibility(View.INVISIBLE);
            mIvChineseName.setVisibility(View.INVISIBLE);
            mIvEnglishName.setVisibility(View.INVISIBLE);
            mIvVolume.setVisibility(View.INVISIBLE);
            mIvOriginPrice.setVisibility(View.INVISIBLE);
            mIvNowPrice.setVisibility(View.INVISIBLE);
            mIvWeixinPrice.setVisibility(View.INVISIBLE);
            mIvAliPrice.setVisibility(View.INVISIBLE);
            mIvSetAds.setVisibility(View.INVISIBLE);
            mIvRecipeName.setVisibility(View.INVISIBLE);
            mSbHot.setVisibility(View.GONE);
            mTvHot.setText(mData.isIsHot() ? "是" : "否");
            mSbNew.setVisibility(View.GONE);
            mTvNew.setText(mData.isIsNew() ? "是" : "否");
            mSbIced.setVisibility(View.GONE);
            mTvIced.setText(mData.isIsIced() ? "是" : "否");
            mSbSweet.setVisibility(View.GONE);
            mTvSweet.setText(mData.isIsSweet() ? "是" : "否");

        }
        if (mAddOrDeletAuth&&!mIsAdd) {
            mBtnDelete.setVisibility(View.VISIBLE);
            mBtnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TitleButtonsDialog descCenterDialog = TitleButtonsDialog.newInstance
                            ("确定删除当前单品?",
                            "取消",
                            "确定");
                    descCenterDialog.setOnTwoClickListener(new ITwoButtonClick() {
                        @Override
                        public void onLeftButtonClick() {

                        }

                        @Override
                        public void onRightButtonClick() {
                            deletItem();
                        }
                    });
                    descCenterDialog.show(getSupportFragmentManager(), "desc");

                }
            });
        } else {
            mBtnDelete.setVisibility(View.GONE);
        }
    }

    /**
     * 更换配方种类
     */
    private void changeRecipeName() {
        if(recipeNames==null){
            Observable<List<RecipeInfo>> observable = MenuApi.typeList(UserUtils.getUserInfo()
                    .getToken(), mVmId);
            JijiaHttpSubscriber subscriber = JijiaHttpSubscriber.buildSubscriber(this)
                    .setOnNextListener(new SubscriberOnNextListener<List<RecipeInfo>>() {
                        @Override
                        public void onNext(List<RecipeInfo> infos) {
                           EditMenuItemActivity.this.infos=infos;
                            recipeNames=new ArrayList<String>();
                            for (RecipeInfo info : infos) {
                                recipeNames.add(info.getName());
                            }
                            changeRecipeWithWheel();
                        }
                    })
                    .setProgressDialog(mProgressDlg)
                    .build();
            observable.subscribe(subscriber);
        }else{
            changeRecipeWithWheel();
        }

    }

    private void changeRecipeWithWheel() {
        //条件选择器
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this,
                new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3, View v) {
                        RecipeInfo recipeInfo = infos.get(options1);
                        mData.setRecipeId(recipeInfo.getId());
                        mData.setRecipeName(recipeInfo.getName());
                        initData();

                    }
                }).build();
        pvOptions.setPicker(recipeNames);
        pvOptions.show();
    }


    /**
     * 选择图片
     */
    private void choosePhoto() {
        CropImage.startPickImageActivity(this);
    }


    private void initIntent() {
        Intent intent = getIntent();
        mData = intent.getParcelableExtra("data");
        mAddOrDeletAuth = intent.getBooleanExtra("addOrDeletAuth", false);
        mEditAuth = intent.getBooleanExtra("editAuth", false);
        mIsAdd = intent.getBooleanExtra("isAdd", false);
        mVmId = intent.getIntExtra("vmId", -1);
        mMenuInfo = intent.getParcelableExtra("menuInfo");
        mMenuId = intent.getIntExtra("menuId", -1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            switch (requestCode) {
                case CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE:
                   Uri mImageUri = CropImage.getPickImageResultUri(this, data);
                    if (mImageUri != null) {
                        startCropImageActivity(mImageUri);
                    }
                    break;
                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    mData.setUrl(result.getUri().getPath());
                    mPicChanged = true;
                       break;
                case REQUEST_CH_NAME:
                    String chName = data.getStringExtra("itemString");
                    mData.setNameCh(chName);
                    break;
                case REQUEST_EN_NAME:
                    String enName = data.getStringExtra("itemString");
                    mData.setNameEn(enName);
                    break;
                case REQUEST_VOLUME:
                    String volume = data.getStringExtra("itemString");
                    mData.setVolume(Float.parseFloat(MyMath.getIntOrDouble(Float.valueOf(volume))));
                    break;
                case REQUEST_ORIGIN_PRICE:
                    String originPrice = data.getStringExtra("itemString");
                    mData.setPrice(Float.parseFloat(MyMath.getIntOrDouble(Float.valueOf(originPrice))));
                    break;
                case REQUEST_NOW_PRICE:
                    String nowPrice = data.getStringExtra("itemString");
                    mData.setDiscountPrice(Float.parseFloat(MyMath.getIntOrDouble(Float.valueOf(nowPrice))));
                    break;
                case REQUEST_WEIXIN_PRICE:
                    String wxPrice = data.getStringExtra("itemString");
                    mData.setWxpayPrice(Float.parseFloat(MyMath.getIntOrDouble(Float.valueOf(wxPrice))));
                    break;
                case REQUEST_ALI_PRICE:
                    String aliPrice = data.getStringExtra("itemString");
                    mData.setAlipayPrice(Float.parseFloat(MyMath.getIntOrDouble(Float.valueOf(aliPrice))));
                    break;
                case REQUEST_SET_ADS:
                    String ads = data.getStringExtra("itemString");
                    mData.setVideoId(ads);
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

    /**
     * { "discountPrice": 0, "isHot": false, "isSweet": false, "isIced": false, "isNew": false,
     * "wxpayPrice": 0, "alipayPrice": 0, "nameCh": "美式咖啡", "nameEn": "1", "price": 0, "volume":
     * 0, "recipeId": 92, "sequence": 3, "saleStatus": true,
     * "fileName":"jijia_testline_1473757683911.png" }
     */
    private void commit() {
        if(mIsAdd){
           addCommit();
        }else {
            changeCommit();
        }

    }

    private void addCommit() {
        if(!checkParams()){
            return;
        }
        Observable<Object> observable = MenuApi.creatMenuItem(UserUtils.getUserInfo()
                .getToken(), new AddItemResult(mMenuId,mVmId,mData));
        JijiaHttpSubscriber subscriber = JijiaHttpSubscriber.buildSubscriber(this)
                .setOnNextListener(new SubscriberOnNextListener() {
                    @Override
                    public void onNext(Object o) {
                        showTip(R.string.add_sucess);
                        RxBus.getDefault().send(REFRESH);
                        finish();
                    }
                })
                .build();
        observable.subscribe(subscriber);
    }

    private void changeCommit() {
        if(!checkParams()){
            return;
        }
        Log.e("mData", mData.toString());
        ArrayList<Integer> objects = new ArrayList<>();
        if(!mData.isSaleStatus()){
            for (MenuInfo.PacksBean packsBean : mMenuInfo.getPacks()) {
                for (MenuInfo.PacksBean.CoffeesBean coffeesBean : packsBean.getCoffees()) {
                    if(coffeesBean.getItemId()==mData.getId()){
                        objects.add(packsBean.getId());
                    }
                }
            }
        }
        Observable<Object> observable = MenuApi.changeMenuItem(UserUtils.getUserInfo()
                .getToken(), new ItemResult(mData.getId(),objects,mData));
        JijiaHttpSubscriber subscriber = JijiaHttpSubscriber.buildSubscriber(this)
                .setOnNextListener(new SubscriberOnNextListener() {
                    @Override
                    public void onNext(Object o) {
                        showTip(R.string.change_sucess);
                        RxBus.getDefault().send(REFRESH);
                        finish();
                    }
                })
                .build();
        observable.subscribe(subscriber);
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
        if(mData.getVolume()==0){
            showTip(R.string.volume_not_empty);
            return false;
        }
        if(mData.getPrice()==0){
            showTip(R.string.original_price_not_empty);
            return false;
        }
        if(mData.getDiscountPrice()==0){
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

        return true;

    }

}
