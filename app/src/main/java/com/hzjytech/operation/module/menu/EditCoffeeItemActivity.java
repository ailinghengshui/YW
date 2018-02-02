package com.hzjytech.operation.module.menu;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.hzjytech.operation.R;
import com.hzjytech.operation.base.BaseActivity;
import com.hzjytech.operation.entity.MenuInfo;
import com.hzjytech.operation.widgets.TitleBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hehongcan on 2017/9/26.
 */

public class EditCoffeeItemActivity extends BaseActivity {
    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.tv_coffee_item_name)
    TextView mTvCoffeeItemName;
    @BindView(R.id.tv_coffee_item_count)
    TextView mTvCoffeeItemCount;
    private int mIndex;
    private MenuInfo.PacksBean.CoffeesBean mData;
    private MenuInfo mMenuInfo;
    private MenuInfo.PacksBean.CoffeesBean mCoffeesBean;

    @Override
    protected int getResId() {
        return R.layout.activity_edit_coffee_item;
    }

    @Override
    protected void initView() {
        initTitle();
        initIntent();
        initData();
    }

    private void initData() {
        mCoffeesBean = new MenuInfo.PacksBean
                .CoffeesBean();
        if(mData==null){
            return;
        }
        mCoffeesBean.setItemId(mData.getItemId());
        mCoffeesBean.setItemName(mData.getItemName());
        mCoffeesBean.setPrice(mData.getPrice());
        mCoffeesBean.setCount(mData.getCount());
        mTvCoffeeItemName.setText(mData.getItemName());
        mTvCoffeeItemCount.setText(mData.getCount()+"");
    }

    private void initTitle() {
        mTitleBar.setLeftText(R.string.cancel);
        mTitleBar.setLeftTextColor(Color.WHITE);
        mTitleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitleBar.setTitle(R.string.select_item);
        mTitleBar.addAction(new TitleBar.TextAction("完成") {
            @Override
            public void performAction(View view) {
                Intent intent = new Intent();
                intent.putExtra("result",mCoffeesBean);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    private void initIntent() {
        Intent intent = getIntent();
        mData = intent.getParcelableExtra("data");
        mMenuInfo = intent.getParcelableExtra("menuInfo");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.rl_cofffee_item_name, R.id.rl_cofffee_item_count})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_cofffee_item_name:
                chooseItemName();
                break;
            case R.id.rl_cofffee_item_count:
                chooseItemCount();
                break;
        }
    }

    private void chooseItemCount() {
        String[] strings = {"0","1", "2", "3", "4", "5", "6", "7", "8", "9"};
        final List<String> countStr = Arrays.asList(strings);
        //条件选择器
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this,
                new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3, View v) {
                        mTvCoffeeItemCount.setText(countStr.get(options1));
                        mCoffeesBean.setCount(Integer.valueOf(countStr.get(options1)));
                    }
                }).build();
        pvOptions.setPicker(countStr);
        pvOptions.show();
    }

    private void chooseItemName() {
        final ArrayList<String> nameStr = new ArrayList<>();
        for (MenuInfo.ItemsBean itemsBean : mMenuInfo.getItems()) {
            nameStr.add(itemsBean.getNameCh());
        }
        //条件选择器
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this,
                new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3, View v) {
                        List<MenuInfo.ItemsBean> items = mMenuInfo.getItems();
                        MenuInfo.ItemsBean itemsBean = items.get(options1);
                        mTvCoffeeItemName.setText(nameStr.get(options1));
                        mCoffeesBean.setItemId(itemsBean.getId());
                        mCoffeesBean.setItemName(itemsBean.getNameCh());
                        mCoffeesBean.setPrice(itemsBean.getPrice());

                    }
                }).build();
        pvOptions.setPicker(nameStr);
        pvOptions.show();
    }
}
