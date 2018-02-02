package com.hzjytech.operation.module.menu;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.hzjytech.operation.R;
import com.hzjytech.operation.base.BaseActivity;
import com.hzjytech.operation.widgets.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.text.InputType.TYPE_CLASS_TEXT;
import static android.text.InputType.TYPE_TEXT_VARIATION_NORMAL;

/**
 * Created by hehongcan on 2017/9/25.
 */

public class EditInfoActivity extends BaseActivity {
    public static final int REQUEST_CH_NAME = 100;
    public static final int REQUEST_EN_NAME = 101;
    public static final int REQUEST_VOLUME = 102;
    public static final int REQUEST_ORIGIN_PRICE = 103;
    public static final int REQUEST_NOW_PRICE = 104;
    public static final int REQUEST_WEIXIN_PRICE = 105;
    public static final int REQUEST_ALI_PRICE = 106;
    public static final int REQUEST_SET_ADS = 107;
    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.et_info)
    EditText mEtInfo;
    @BindView(R.id.ivInfoClear)
    ImageView mIvInfoClear;
    private String mItemString;
    private int mType;
    private String mPackString;
    private boolean mIsFromPack;
    private int inputType;

    @Override
    protected int getResId() {
        return R.layout.activity_edit_info;
    }

    @Override
    protected void initView() {
        initIntent();
        initData();
    }

    private void initData() {
        mTitleBar.setLeftText("取消");
        mTitleBar.setLeftTextColor(Color.WHITE);
        inputType = InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_NUMBER_FLAG_SIGNED;
        String title = "";
        if (mIsFromPack) {
            mEtInfo.setText(mPackString);
        } else {
            mEtInfo.setText(mItemString);
        }
        switch (mType) {
            case REQUEST_CH_NAME:
                mEtInfo.setInputType( TYPE_CLASS_TEXT |TYPE_TEXT_VARIATION_NORMAL);
                mEtInfo.setHint(R.string.please_input_ch_name);
                title = getString(R.string.change_chinese_name);
                break;
            case REQUEST_EN_NAME:
                mEtInfo.setInputType( TYPE_CLASS_TEXT |TYPE_TEXT_VARIATION_NORMAL);
                mEtInfo.setHint(R.string.please_input_en_name);
                title = getString(R.string.change_english_name);
                break;
            case REQUEST_VOLUME:
                mEtInfo.setInputType(inputType);
                mEtInfo.setHint(R.string.please_input_volume);
                title = getString(R.string.change_volume);
                break;
            case REQUEST_ORIGIN_PRICE:
                mEtInfo.setInputType(inputType);
                mEtInfo.setHint(R.string.please_input_origin_price);
                title = getString(R.string.change_orgin_price);
                break;
            case REQUEST_NOW_PRICE:
                mEtInfo.setInputType(inputType);
                mEtInfo.setHint(R.string.please_input_now_price);
                title = getString(R.string.change_now_price);
                break;
            case REQUEST_WEIXIN_PRICE:
                mEtInfo.setInputType(inputType);
                mEtInfo.setHint(R.string.please_input_wx_price);
                title = getString(R.string.change_weixin_price);
                break;
            case REQUEST_ALI_PRICE:
                mEtInfo.setInputType(inputType);
                mEtInfo.setHint(R.string.please_input_ali_price);
                title = getString(R.string.change_ali_price);
                break;
            case REQUEST_SET_ADS:
                mEtInfo.setInputType( TYPE_CLASS_TEXT |TYPE_TEXT_VARIATION_NORMAL);
                mEtInfo.setHint(R.string.please_input_ad_set);
                mEtInfo.setText(mItemString);
                title = getString(R.string.change_ads);
                break;
            default:
                break;
        }
        mTitleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitleBar.setTitle(title);
        mTitleBar.addAction(new TitleBar.TextAction("完成") {
            @Override
            public void performAction(View view) {
                Intent intent = new Intent();
                intent.putExtra("itemString",mItemString);
                intent.putExtra("packString",mPackString);
                setResult(RESULT_OK, intent);
                finish();

            }
        });
        mEtInfo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                if (mIsFromPack) {
                    mPackString=(str);
                } else {
                    mItemString=(str);
                }
            }
        });
    }


    private void initIntent() {
        Intent intent = getIntent();
        mItemString = intent.getStringExtra("data");
        mPackString = intent.getStringExtra("packData");
        if(mItemString==null||mItemString.equals("0.0")){
            mItemString="";
        }
        if(mPackString==null||mPackString.equals("0.0")){
            mPackString="";
        }
        mType = intent.getIntExtra("type", -1);
        mIsFromPack = intent.getBooleanExtra("isFromPack", false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
