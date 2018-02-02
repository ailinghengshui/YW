package com.hzjytech.operation.module.common;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hzjytech.operation.R;
import com.hzjytech.operation.base.BaseActivity;
import com.hzjytech.operation.entity.LoginInfo;
import com.hzjytech.operation.entity.LoginResultInfo;
import com.hzjytech.operation.entity.User;
import com.hzjytech.operation.http.ApiException;
import com.hzjytech.operation.http.HttpResult;
import com.hzjytech.operation.http.JijiaHttpSubscriber;
import com.hzjytech.operation.http.NetConstants;
import com.hzjytech.operation.http.SubscriberOnErrorListener;
import com.hzjytech.operation.http.SubscriberOnNextListener;
import com.hzjytech.operation.http.api.AuthorizationApi;
import com.hzjytech.operation.utils.AppUtil;
import com.hzjytech.operation.utils.MD5Util;
import com.hzjytech.operation.utils.StringUtil;
import com.hzjytech.operation.utils.TextUtil;
import com.hzjytech.operation.utils.ToastUtil;
import com.hzjytech.operation.utils.UserUtils;
import com.hzjytech.operation.widgets.dialogs.TipDingDingDialog;

import com.umeng.analytics.MobclickAgent;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by hehongcan on 2017/4/6.
 */
public class LoginActivity extends BaseActivity {
    @BindView(R.id.metLoginTel)
    EditText metLoginTel;
    @BindView(R.id.metLoginPsd)
    EditText metLoginPsd;
    @BindView(R.id.btnLoginFgpsd)
    TextView btnLoginFgpsd;
    @BindView(R.id.ivOldpsdfrgClear1)
    ImageView ivOldpsdfrgClear1;
    @BindView(R.id.ivOldpsdfrgClear2)
    ImageView ivOldpsdfrgClear2;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.et_identfy_code)
    EditText mEtIdentfyCode;
    @BindView(R.id.iv_identfy_code)
    ImageView mIvIdentfyCode;
    @BindView(R.id.ll_identify_code)
    LinearLayout mLlIdentifyCode;
    @BindView(R.id.ll_login_edit)
    LinearLayout mLlLoginEdit;
    private String token;


    @Override
    protected int getResId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        mLlIdentifyCode.setVisibility(View.GONE);
        metLoginTel.addTextChangedListener(mTextWathcer1);
        metLoginPsd.addTextChangedListener(mTextWathcer2);
        mEtIdentfyCode.addTextChangedListener(mTextWathcer3);
        String s = metLoginPsd.getText()
                .toString();
        if (UserUtils.getMobile() == null) {
            return;
        }
        metLoginTel.setText(UserUtils.getMobile());
        if (UserUtils.getMobile() != null && !UserUtils.getMobile()
                .equals("")) {
            metLoginTel.setSelection(metLoginTel.getText()
                    .length());
            ivOldpsdfrgClear1.setVisibility(View.VISIBLE);
            ivOldpsdfrgClear1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    metLoginTel.setText("");
                    ivOldpsdfrgClear1.setVisibility(View.GONE);
                }
            });
        }
    }

    @OnClick({R.id.btnLogin, R.id.btnLoginFgpsd, R.id.iv_identfy_code})
    public void onClick(View v) {
        if (AppUtil.isFastClick())
            return;
        switch (v.getId()) {
            case R.id.btnLogin:
                if (TextUtil.checkParams(metLoginTel.getText()
                                .toString()
                                .trim(),
                        metLoginPsd.getText()
                                .toString()
                                .trim()) && mLlIdentifyCode.getVisibility() == View.GONE) {
                    loginButtonOnClick();
                } else if (TextUtil.checkParams(metLoginTel.getText()
                                .toString()
                                .trim(),
                        metLoginPsd.getText()
                                .toString()
                                .trim(),
                        mEtIdentfyCode.getText()
                                .toString()
                                .trim())) {
                    loginButtonOnClick();
                } else {
                    if (mLlIdentifyCode.getVisibility() == View.VISIBLE) {
                        ToastUtil.showShort(LoginActivity.this, "请输入验证码");
                    }
                    ToastUtil.showShort(LoginActivity.this, "请输入账号或密码");
                }
                break;
            case R.id.btnLoginFgpsd:
                TipDingDingDialog tipDingDingDialog = new TipDingDingDialog();
                tipDingDingDialog.show(getSupportFragmentManager(), "dingding");
                break;
            case R.id.iv_identfy_code:
                loginButtonOnClick();
                break;
        }


    }

    private void loginButtonOnClick() {
        String tel = metLoginTel.getText()
                .toString()
                .trim();
        String password = metLoginPsd.getText()
                .toString()
                .trim();
        String identifyCode = mEtIdentfyCode.getText()
                .toString()
                .trim();
        Observable<User> observable = AuthorizationApi.login(tel,
                MD5Util.encrypt(password),
                NetConstants.IS_ONLINE,
                identifyCode)
                .flatMap(new Func1<Object, Observable<User>>() {
                    @Override
                    public Observable<User> call(Object loginInfo) {
                        String json = new Gson().toJson(loginInfo);
                        LoginInfo info = new Gson().fromJson(json, LoginInfo.class);
                        token = info.getToken();
                        return AuthorizationApi.getPersonalData(info.getToken());
                    }
                });
        long l = System.currentTimeMillis();
        JijiaHttpSubscriber subscriber = JijiaHttpSubscriber.buildSubscriber(this)
                .setOnNextListener(new SubscriberOnNextListener<User>() {
                    @Override
                    public void onNext(User user) {
                            user.setToken(token);
                            JPushInterface.setAlias(LoginActivity.this,
                                    user.getAdminId() + "",
                                    null);
                            UserUtils.saveLocalMobileAndPwd(metLoginTel.getText()
                                    .toString(), "");
                            UserUtils.saveUserInfo(user);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                })
                .setOnErrorListener(new SubscriberOnErrorListener() {
                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof ApiException) {
                            ApiException exception = (ApiException) e;
                            HttpResult result = exception.getResult();
                            if (result.getStatusCode() == 101 || result.getStatusCode() == 102) {
                                Gson gson = new Gson();
                                String s = gson.fromJson(gson.toJson(result.getResults()),
                                        String.class);
                                mLlIdentifyCode.setVisibility(View.VISIBLE);
                                Glide.with(LoginActivity.this)
                                        .load(s.replace("\"", ""))
                                        .placeholder(R.color.bg_grey)
                                        .into(mIvIdentfyCode);
                            }
                        }
                    }
                })
                .setProgressDialog(mProgressDlg)
                .build();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    /**
     * edittext的内容观察者
     */
    private TextWatcher mTextWathcer1 = new TextWatcher() {

        private CharSequence temp;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            temp = s;
            if (temp.length() > 0) {
                ivOldpsdfrgClear1.setVisibility(View.VISIBLE);
                ivOldpsdfrgClear1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        metLoginTel.setText("");
                        btnLogin.setEnabled(false);
                        ivOldpsdfrgClear1.setVisibility(View.GONE);
                    }
                });
                if (!StringUtil.isNullOrEmpty(metLoginPsd.getText()
                        .toString())) {
                    btnLogin.setEnabled(true);
                }
            }

            if (temp.length() == 0) {
                btnLogin.setEnabled(false);
                ivOldpsdfrgClear1.setVisibility(View.GONE);
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {
                ivOldpsdfrgClear1.setVisibility(View.VISIBLE);
                ivOldpsdfrgClear1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        metLoginTel.setText("");
                        btnLogin.setEnabled(false);
                        ivOldpsdfrgClear1.setVisibility(View.GONE);
                    }
                });
                if (!StringUtil.isNullOrEmpty(metLoginPsd.getText()
                        .toString())) {
                    btnLogin.setEnabled(true);
                }
            }

            if (s.length() == 0) {
                btnLogin.setEnabled(false);
                ivOldpsdfrgClear1.setVisibility(View.GONE);
            }
        }
    };

    private TextWatcher mTextWathcer2 = new TextWatcher() {

        private CharSequence temp;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            temp = s;
            if (temp.length() > 0) {
                ivOldpsdfrgClear2.setVisibility(View.VISIBLE);
                ivOldpsdfrgClear2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        metLoginPsd.setText("");
                        btnLogin.setEnabled(false);
                        ivOldpsdfrgClear2.setVisibility(View.GONE);
                    }
                });
                if (!StringUtil.isNullOrEmpty(metLoginTel.getText()
                        .toString())) {
                    btnLogin.setEnabled(true);
                }
            }

            if (temp.length() == 0) {
                btnLogin.setEnabled(false);
                ivOldpsdfrgClear2.setVisibility(View.GONE);
            }

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            if (s.length() > 0) {
                ivOldpsdfrgClear2.setVisibility(View.VISIBLE);
                ivOldpsdfrgClear2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        metLoginPsd.setText("");
                        btnLogin.setEnabled(false);
                        ivOldpsdfrgClear2.setVisibility(View.GONE);
                    }
                });
            }

            if (s.length() == 0) {
                ivOldpsdfrgClear2.setVisibility(View.GONE);
            }
        }
    };
    private TextWatcher mTextWathcer3 = new TextWatcher() {

        private CharSequence temp;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 4) {
                btnLogin.setEnabled(true);
            } else {
                btnLogin.setEnabled(false);
            }
        }
    };

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
