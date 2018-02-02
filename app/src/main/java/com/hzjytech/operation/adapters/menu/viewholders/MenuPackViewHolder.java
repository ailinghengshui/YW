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
import com.hzjytech.operation.inter.MorePackClickListener;
import com.hzjytech.operation.inter.OnInfoClickListener;
import com.hzjytech.operation.utils.DensityUtil;
import com.hzjytech.operation.utils.MyMath;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hzjytech.operation.constants.Constants.MenuClick.ADD_MENU_PACK;
import static com.hzjytech.operation.constants.Constants.MenuClick.EDIT_MENU_PACK;

/**
 * Created by hehongcan on 2017/5/3.
 */
public class MenuPackViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.ll_pack_pack_container)
    LinearLayout mLlPackPackContainer;

    public MenuPackViewHolder(View view) {
        super(view);
        //R.layout.item_menu_pack
        ButterKnife.bind(this, view);
    }

    public void setMenuPackData(
            Context context,
            List<MenuInfo.PacksBean> packs,
            boolean AUTH_DELET_ADD_MENU_ITEM,
            boolean AUTH_EDIT_MENU_ITEM,
            final OnInfoClickListener morePackClickListener) {
        View head = mLlPackPackContainer.getChildAt(0);
        LayoutInflater inflater = LayoutInflater.from(context);
        View foot = inflater.inflate(R.layout.item_footer,null, false);
        TextView  mTvCheckMorePack = (TextView) foot.findViewById(R.id.tv_check_more_group);
        TextView mTvAddPack= (TextView) foot.findViewById(R.id.tv_add_group);
        mLlPackPackContainer.removeAllViews();
        mLlPackPackContainer.addView(head);
        mLlPackPackContainer.addView(foot);
        if (packs == null || packs.size() == 0) {
            if(!AUTH_DELET_ADD_MENU_ITEM){
              foot.setVisibility(View.GONE);
            }else {
                mTvAddPack.setVisibility(View.VISIBLE);
                mTvCheckMorePack.setVisibility(View.INVISIBLE);
                View view = new View(context, null);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup
                        .LayoutParams.MATCH_PARENT,
                        DensityUtil.dp2px(context, 1));
                params.leftMargin = DensityUtil.dp2px(context, 12);
                view.setLayoutParams(params);
                view.setBackgroundColor(ContextCompat.getColor(context, R.color.line_color));
                mLlPackPackContainer.addView(view, 1);
                foot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (morePackClickListener != null) {
                            morePackClickListener.click(ADD_MENU_PACK);
                        }
                    }
                });
            }

        } else {
            mTvAddPack.setVisibility(View.INVISIBLE);
            mTvCheckMorePack.setVisibility(View.VISIBLE);
            if (packs.size() == 1) {
                View view = inflater.inflate(R.layout.menu_item_pack, null, false);
                mLlPackPackContainer.addView(view, 1);
                TextView tv_order = (TextView) view.findViewById(R.id.tv_pack_order);
                ImageView iv_pic = (ImageView) view.findViewById(R.id.iv_pack_pic);
                TextView tv_name = (TextView) view.findViewById(R.id.tv_pack_name);
                TextView tv_sold_type = (TextView) view.findViewById(R.id.tv_pack_sold_type);
                TextView tv_orgin_price = (TextView) view.findViewById(R.id.tv_pack_orgin_price);
                TextView tv_now_price = (TextView) view.findViewById(R.id.tv_pack_now_price);
                TextView tv_weixin_price = (TextView) view.findViewById(R.id.tv_pack_weixin_price);
                TextView tv_zfb_price = (TextView) view.findViewById(R.id.tv_pack_zfb_price);
                LinearLayout ll_pack_item_container = (LinearLayout) view.findViewById(R.id
                        .ll_pack_item_container);
                MenuInfo.PacksBean packsBean = packs.get(0);
                tv_order.setText(1 + "");
                Glide.with(context)
                        .load(packsBean.getUrl())
                        .fitCenter()
                        .placeholder(R.drawable.bg_photo)
                        .dontAnimate()
                        .into(iv_pic);
                tv_name.setText(packsBean.getNameCh() + "/" + packsBean.getNameEn());
                tv_sold_type.setText(packsBean.isSaleStatus() ? "在售" : "售罄");
                tv_sold_type.setBackgroundResource(packsBean.isSaleStatus() ? R.drawable
                        .bg_sold_type_yes : R.drawable.bg_sold_type_no);
                tv_orgin_price.setText(context.getString(R.string.original_price,
                        MyMath.getTwoRoundNum(packsBean.getOriginPrice())));
                tv_now_price.setText(context.getString(R.string.now_price,
                        MyMath.getTwoRoundNum(packsBean.getNowPrice())));
                tv_weixin_price.setText(context.getString(R.string.weixin_price,
                        MyMath.getTwoRoundNum(packsBean.getWxpayPrice())));
                tv_zfb_price.setText(context.getString(R.string.ali_price,
                        MyMath.getTwoRoundNum(packsBean.getAlipayPrice())));
                List<MenuInfo.PacksBean.CoffeesBean> coffees = packsBean.getCoffees();
                if (coffees != null && coffees.size() > 0) {
                    for (int j = 0; j < coffees.size(); j++) {
                        View itemView = inflater.inflate(R.layout.item_pack_item, null, false);
                        TextView tv_name_count = (TextView) itemView.findViewById(R.id
                                .tv_pack_item_name_count);
                        if (coffees.get(j)
                                .getCount() > -1) {
                            tv_name_count.setText(coffees.get(j)
                                    .getItemName() + "*" + coffees.get(j)
                                    .getCount());
                        } else {
                            tv_name_count.setText(coffees.get(j)
                                    .getItemName());
                        }
                        if (j != 0) {
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT);
                            layoutParams.setMargins(DensityUtil.dp2px(context, 10), 0, 0, 0);
                            itemView.setLayoutParams(layoutParams);
                        }
                        ll_pack_item_container.addView(itemView);
                    }
                }
            } else {
                for (int i = 0; i < 2; i++) {
                    View view = inflater.inflate(R.layout.menu_item_pack, null, false);
                    mLlPackPackContainer.addView(view, 1);
                }
                for (int i = 0; i < 2; i++) {
                    View view = mLlPackPackContainer.getChildAt(i + 1);
                    TextView tv_order = (TextView) view.findViewById(R.id.tv_pack_order);
                    ImageView iv_pic = (ImageView) view.findViewById(R.id.iv_pack_pic);
                    TextView tv_name = (TextView) view.findViewById(R.id.tv_pack_name);
                    TextView tv_sold_type = (TextView) view.findViewById(R.id.tv_pack_sold_type);
                    TextView tv_orgin_price = (TextView) view.findViewById(R.id
                            .tv_pack_orgin_price);
                    TextView tv_now_price = (TextView) view.findViewById(R.id.tv_pack_now_price);
                    TextView tv_weixin_price = (TextView) view.findViewById(R.id
                            .tv_pack_weixin_price);
                    TextView tv_zfb_price = (TextView) view.findViewById(R.id.tv_pack_zfb_price);
                    LinearLayout ll_pack_item_container = (LinearLayout) view.findViewById(R.id
                            .ll_pack_item_container);
                    MenuInfo.PacksBean packsBean = packs.get(i);
                    tv_order.setText(i+1+"");
                    Glide.with(context)
                            .load(packsBean.getUrl())
                            .fitCenter()
                            .placeholder(R.drawable.bg_photo)
                            .dontAnimate()
                            .into(iv_pic);
                    tv_name.setText(packsBean.getNameCh() + "/" + packsBean.getNameEn());
                    tv_sold_type.setText(packsBean.isSaleStatus() ? "在售" : "售罄");
                    tv_sold_type.setBackgroundResource(packsBean.isSaleStatus() ? R.drawable
                            .bg_sold_type_yes : R.drawable.bg_sold_type_no);
                    tv_orgin_price.setText(context.getString(R.string.original_price,
                            MyMath.getTwoRoundNum(packsBean.getOriginPrice())));
                    tv_now_price.setText(context.getString(R.string.now_price,
                            MyMath.getTwoRoundNum(packsBean.getNowPrice())));
                    tv_weixin_price.setText(context.getString(R.string.weixin_price,
                            MyMath.getTwoRoundNum(packsBean.getWxpayPrice())));
                    tv_zfb_price.setText(context.getString(R.string.ali_price,
                            MyMath.getTwoRoundNum(packsBean.getAlipayPrice())));
                    List<MenuInfo.PacksBean.CoffeesBean> coffees = packsBean.getCoffees();
                    if (coffees != null && coffees.size() > 0) {
                        for (int j = 0; j < coffees.size(); j++) {
                            View itemView = inflater.inflate(R.layout.item_pack_item, null, false);
                            TextView tv_name_count = (TextView) itemView.findViewById(R.id
                                    .tv_pack_item_name_count);
                            if (coffees.get(j)
                                    .getCount() > -1) {
                                tv_name_count.setText(coffees.get(j)
                                        .getItemName() + "*" + coffees.get(j)
                                        .getCount());
                            } else {
                                tv_name_count.setText(coffees.get(j)
                                        .getItemName());
                            }
                            if (j != 0) {
                                LinearLayout.LayoutParams layoutParams = new LinearLayout
                                        .LayoutParams(
                                        ViewGroup.LayoutParams.WRAP_CONTENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT);
                                layoutParams.setMargins(DensityUtil.dp2px(context, 10), 0, 0, 0);
                                itemView.setLayoutParams(layoutParams);
                            }
                            ll_pack_item_container.addView(itemView);
                        }
                    }
                }


            }
            foot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    morePackClickListener.click(EDIT_MENU_PACK);
                }
            });
        }
    }
}
