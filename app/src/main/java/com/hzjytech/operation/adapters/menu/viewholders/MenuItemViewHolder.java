package com.hzjytech.operation.adapters.menu.viewholders;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hzjytech.operation.R;
import com.hzjytech.operation.entity.MenuInfo;
import com.hzjytech.operation.inter.MoreSingleItemClickListener;
import com.hzjytech.operation.inter.OnInfoClickListener;
import com.hzjytech.operation.utils.DensityUtil;
import com.hzjytech.operation.utils.MyMath;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hzjytech.operation.R.layout.line;
import static com.hzjytech.operation.constants.Constants.GroupClick.MACHINEADDMORE;
import static com.hzjytech.operation.constants.Constants.MenuClick.ADD_MENU_ITEM;
import static com.hzjytech.operation.constants.Constants.MenuClick.EDI_MENU_ITEM;

/**
 * Created by hehongcan on 2017/5/3.
 */
public class MenuItemViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.ll_menu_item_container)
    LinearLayout mLlMenuItemContainer;

    public MenuItemViewHolder(View view) {
        super(view);
       // R.layout.item_menu_item
        ButterKnife.bind(this, view);
    }

    public void setMenuItemData(
            final Context context,
            List<MenuInfo.ItemsBean> itemsBeens,
            boolean AUTH_DELET_ADD_MENU_ITEM,
            boolean AUTH_EDIT_MENU_ITEM,
            final OnInfoClickListener moreSingleItemClickListener) {
        View head = mLlMenuItemContainer.getChildAt(0);
        LayoutInflater inflater = LayoutInflater.from(context);
        View foot = inflater.inflate(R.layout.item_footer,null, false);
        TextView  mTvCheckMoreMenu = (TextView) foot.findViewById(R.id.tv_check_more_group);
        TextView mTvAddMenu= (TextView) foot.findViewById(R.id.tv_add_group);
        mLlMenuItemContainer.removeAllViews();
        mLlMenuItemContainer.addView(head);
        mLlMenuItemContainer.addView(foot);

        if (itemsBeens == null || itemsBeens.size() == 0) {
            if(!AUTH_DELET_ADD_MENU_ITEM){
               foot.setVisibility(View.GONE);
            }else {
                foot.setVisibility(View.VISIBLE);
                mTvAddMenu.setVisibility(View.VISIBLE);
                mTvCheckMoreMenu.setVisibility(View.INVISIBLE);
                View view = new View(context, null);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup
                        .LayoutParams.MATCH_PARENT, DensityUtil.dp2px(context,1));
                params.leftMargin=DensityUtil.dp2px(context,12);
                view.setLayoutParams(params);
                view.setBackgroundColor(ContextCompat.getColor(context,R.color.line_color));
                mLlMenuItemContainer.addView(view,1);
                foot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(moreSingleItemClickListener!=null){
                            moreSingleItemClickListener.click(ADD_MENU_ITEM);
                        }
                    }
                });
            }

        } else {
            mTvAddMenu.setVisibility(View.INVISIBLE);
            mTvCheckMoreMenu.setVisibility(View.VISIBLE);
            if (itemsBeens.size() == 1) {
                View view = inflater.inflate(R.layout.menu_item, null, false);
                mLlMenuItemContainer.addView(view,1);
                TextView tv_order = (TextView) view.findViewById(R.id.tv_order);
                ImageView iv_pic = (ImageView) view.findViewById(R.id.iv_pic);
                TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
                TextView tv_receipe_type = (TextView) view.findViewById(R.id.tv_receipe_type);
                TextView tv_sold_type = (TextView) view.findViewById(R.id.tv_sold_type);
                TextView tv_volume = (TextView) view.findViewById(R.id.tv_volume);
                TextView tv_orgin_price = (TextView) view.findViewById(R.id.tv_orgin_price);
                TextView tv_now_price = (TextView) view.findViewById(R.id.tv_now_price);
                TextView tv_weixin_price = (TextView) view.findViewById(R.id.tv_weixin_price);
                TextView tv_zfb_price = (TextView) view.findViewById(R.id.tv_zfb_price);
                View bg_hot = view.findViewById(R.id.bg_hot);
                View bg_iced = view.findViewById(R.id.bg_iced);
                View bg_new = view.findViewById(R.id.bg_new);
                View bg_sweet = view.findViewById(R.id.bg_sweet);
                MenuInfo.ItemsBean itemsBean = itemsBeens.get(0);
                tv_order.setText(1 + "");
                /*  iv_pic.setImageResource(0);
                iv_pic.setScaleType(ImageView.ScaleType.FIT_CENTER);
                DisplayImageOptions options=new DisplayImageOptions.Builder()
                        .cacheInMemory(true)*//*缓存至内存*//*
                        .cacheOnDisk(true)*//*缓存值SDcard*//*
                        .bitmapConfig(Bitmap.Config.RGB_565)
                        .build();*/
                String app_image = itemsBean.getUrl();
                Glide.with(context)
                        .load(app_image)
                        .fitCenter()
                        .placeholder(R.drawable.bg_photo)
                        .dontAnimate()
                        .into(iv_pic);
                // ImageLoader.getInstance().displayImage(app_image,iv_pic,options);
                tv_name.setText(itemsBean.getNameCh() + "/" + itemsBean.getNameEn());
                tv_receipe_type.setText("配方种类： " + itemsBean.getRecipeName());
                tv_sold_type.setText(itemsBean.isSaleStatus() ? "在售" : "售罄");
                tv_sold_type.setBackgroundResource(itemsBean.isSaleStatus() ? R.drawable
                        .bg_sold_type_yes : R.drawable.bg_sold_type_no);
                tv_volume.setText(context.getString(R.string.volume,
                        MyMath.getIntOrDouble(itemsBean.getVolume())));
                tv_orgin_price.setText(context.getString(R.string.original_price,
                        MyMath.getTwoRoundNum(itemsBean.getPrice())));
                tv_now_price.setText(context.getString(R.string.now_price,
                        MyMath.getTwoRoundNum(itemsBean.getDiscountPrice())));
                tv_weixin_price.setText(context.getString(R.string.weixin_price,
                        MyMath.getTwoRoundNum(itemsBean.getWxpayPrice())));
                tv_zfb_price.setText(context.getString(R.string.ali_price,
                        MyMath.getTwoRoundNum(itemsBean.getAlipayPrice())));
                bg_hot.setBackgroundResource(itemsBean.isIsHot() ? R.color.color_green : R.color
                        .bg_grey);
                bg_new.setBackgroundResource(itemsBean.isIsNew() ? R.color.color_green : R.color
                        .bg_grey);
                bg_iced.setBackgroundResource(itemsBean.isIsIced() ? R.color.color_green : R
                        .color.bg_grey);
                bg_sweet.setBackgroundResource(itemsBean.isIsSweet() ? R.color.color_green : R
                        .color.bg_grey);
            } else {

                for (int i = 0; i < 2; i++) {
                    View view = inflater.inflate(R.layout.menu_item, null, false);
                    mLlMenuItemContainer.addView(view,1);
                }
                for (int i = 0; i < 2; i++) {
                    View view = mLlMenuItemContainer.getChildAt(i + 1);
                    TextView tv_order = (TextView) view.findViewById(R.id.tv_order);
                    ImageView iv_pic = (ImageView) view.findViewById(R.id.iv_pic);
                    TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
                    TextView tv_receipe_type = (TextView) view.findViewById(R.id.tv_receipe_type);
                    TextView tv_sold_type = (TextView) view.findViewById(R.id.tv_sold_type);
                    TextView tv_volume = (TextView) view.findViewById(R.id.tv_volume);
                    TextView tv_orgin_price = (TextView) view.findViewById(R.id.tv_orgin_price);
                    TextView tv_now_price = (TextView) view.findViewById(R.id.tv_now_price);
                    TextView tv_weixin_price = (TextView) view.findViewById(R.id.tv_weixin_price);
                    TextView tv_zfb_price = (TextView) view.findViewById(R.id.tv_zfb_price);
                    View bg_hot = view.findViewById(R.id.bg_hot);
                    View bg_iced = view.findViewById(R.id.bg_iced);
                    View bg_new = view.findViewById(R.id.bg_new);
                    View bg_sweet = view.findViewById(R.id.bg_sweet);
                    MenuInfo.ItemsBean itemsBean = itemsBeens.get(i);
                    tv_order.setText(1 + i + "");
                    iv_pic.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    String app_image = itemsBean.getUrl();
                    Glide.with(context)
                            .load(app_image)
                            .fitCenter()
                            .dontAnimate()
                            .placeholder(R.drawable.bg_photo)
                            .into(iv_pic);
                    tv_name.setText(itemsBean.getNameCh() + "/" + itemsBean.getNameEn());
                    tv_receipe_type.setText("配方种类： " + itemsBean.getRecipeName());
                    tv_sold_type.setText(itemsBean.isSaleStatus() ? "在售" : "售罄");
                    tv_sold_type.setBackgroundResource(itemsBean.isSaleStatus() ? R.drawable
                            .bg_sold_type_yes : R.drawable.bg_sold_type_no);
                    tv_volume.setText(context.getString(R.string.volume,
                            MyMath.getIntOrDouble(itemsBean.getVolume())));
                    tv_orgin_price.setText(context.getString(R.string.original_price,
                            MyMath.getTwoRoundNum(itemsBean.getPrice())));
                    tv_now_price.setText(context.getString(R.string.now_price,
                            MyMath.getTwoRoundNum(itemsBean.getDiscountPrice())));
                    tv_weixin_price.setText(context.getString(R.string.weixin_price,
                            MyMath.getTwoRoundNum(itemsBean.getWxpayPrice())));
                    tv_zfb_price.setText(context.getString(R.string.ali_price,
                            MyMath.getTwoRoundNum(itemsBean.getAlipayPrice())));
                    bg_hot.setBackgroundResource(itemsBean.isIsHot() ? R.color.color_green : R
                            .color.bg_grey);
                    bg_new.setBackgroundResource(itemsBean.isIsNew() ? R.color.color_green : R
                            .color.bg_grey);
                    bg_iced.setBackgroundResource(itemsBean.isIsIced() ? R.color.color_green : R
                            .color.bg_grey);
                    bg_sweet.setBackgroundResource(itemsBean.isIsSweet() ? R.color.color_green : 
                            R.color.bg_grey);
                }

            }
            foot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    moreSingleItemClickListener.click(EDI_MENU_ITEM);
                }
            });
        }
    }
}
