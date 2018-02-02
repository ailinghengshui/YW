package com.hzjytech.operation.adapters.menu;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hzjytech.operation.R;
import com.hzjytech.operation.entity.MenuInfo;
import com.hzjytech.operation.module.menu.EditMenuItemActivity;
import com.hzjytech.operation.utils.MyMath;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hzjytech.operation.constants.Constants.MenuAuth.AUTH_DELET_ADD;
import static com.hzjytech.operation.constants.Constants.MenuAuth.AUTH_EDIT;

/**
 * Created by hehongcan on 2017/5/3.
 */
public class SingleItemAdapter extends RecyclerView.Adapter {
   
    private Context context;
    private List<MenuInfo.ItemsBean> items;
    private final LayoutInflater inflater;
    private boolean addOrDeletAuth=false;
    private boolean editAuth=false;
    private MenuInfo menuInfo;
    private int menuId;

    public void setItemData(
            List<MenuInfo.ItemsBean> items, MenuInfo menuInfo, int menuId, List<String> auth){
        this.menuInfo=menuInfo;
        this.items=items;
        this.menuId=menuId;
        resolveAuth(auth);
        notifyDataSetChanged();
    }

    /**
     * 处理权限问题
     * @param auth
     */
    private void resolveAuth(List<String> auth) {
        if(auth.contains(AUTH_DELET_ADD)){
            addOrDeletAuth=true;

        }
        if(auth.contains(AUTH_EDIT)){
            editAuth=true;
        }
    }

    public SingleItemAdapter(Context context, List<MenuInfo.ItemsBean> items) {
        this.context = context;
        this.items = items;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.menu_item, parent, false);
        return new SingleItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SingleItemViewHolder holder1 = (SingleItemViewHolder) holder;
        final MenuInfo.ItemsBean bean = items.get(position);
        holder1.tvOrder.setText(position+1+"");
        holder1.tvName.setText(bean.getNameCh()+"/"+bean.getNameEn());
        holder1.iv_menu_item_right.setVisibility(View.VISIBLE);
       // holder1.ivPic.setScaleType(ImageView.ScaleType.FIT_CENTER);
      /*  DisplayImageOptions options=new DisplayImageOptions.Builder()
                .cacheInMemory(true)*//*缓存至内存*//*
                .cacheOnDisk(true)*//*缓存值SDcard*//*
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();*/
        String app_image = bean.getUrl();
        Glide.with(context).load(app_image).fitCenter().placeholder(R.drawable.bg_photo).dontAnimate().into(holder1.ivPic);
        holder1.tvReceipeType.setText("配方种类： "+bean.getRecipeName());
        holder1.tvSoldType.setText(bean.isSaleStatus()?"在售":"售罄");
        holder1.tvSoldType.setBackgroundResource(bean.isSaleStatus()?R.drawable.bg_sold_type_yes:R.drawable.bg_sold_type_no);
        holder1.tvVolume.setText(context.getString(R.string.volume,
                MyMath.getIntOrDouble(bean.getVolume())));
        holder1.tvOrginPrice.setText(context.getString(R.string.original_price,
                MyMath.getTwoRoundNum(bean.getPrice())));
        holder1.tvNowPrice.setText(context.getString(R.string.now_price,
                MyMath.getTwoRoundNum(bean.getDiscountPrice())));
        holder1.tvWeixinPrice.setText(context.getString(R.string.weixin_price,
                MyMath.getTwoRoundNum(bean.getWxpayPrice())));
        holder1.tvZfbPrice.setText(context.getString(R.string.ali_price,
                MyMath.getTwoRoundNum(bean.getAlipayPrice())));
        holder1.bgHot.setBackgroundResource(bean.isIsHot()?R.color.color_green:R.color.bg_grey);
        holder1.bgNew.setBackgroundResource(bean.isIsNew()?R.color.color_green:R.color.bg_grey);
        holder1.bgIced.setBackgroundResource(bean.isIsIced()?R.color.color_green:R.color.bg_grey);
        holder1.bgSweet.setBackgroundResource(bean.isIsSweet()?R.color.color_green:R.color.bg_grey);
        holder1.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditMenuItemActivity.class);
                intent.putExtra("data",bean);
                intent.putExtra("addOrDeletAuth",addOrDeletAuth);
                intent.putExtra("editAuth",editAuth);
                intent.putExtra("vmId",menuInfo.getVmTypeId());
                intent.putExtra("menuInfo",menuInfo);
                intent.putExtra("menuId",menuId);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public class SingleItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_order)
        public TextView tvOrder;
        @BindView(R.id.iv_pic)
        ImageView ivPic;
        @BindView(R.id.tv_name)
        public TextView tvName;
        @BindView(R.id.tv_receipe_type)
        public TextView tvReceipeType;
        @BindView(R.id.tv_sold_type)
        public TextView tvSoldType;
        @BindView(R.id.tv_volume)
        public TextView tvVolume;
        @BindView(R.id.tv_orgin_price)
        public TextView tvOrginPrice;
        @BindView(R.id.tv_weixin_price)
        public TextView tvWeixinPrice;
        @BindView(R.id.tv_now_price)
        public TextView tvNowPrice;
        @BindView(R.id.tv_zfb_price)
        public TextView tvZfbPrice;
        @BindView(R.id.bg_hot)
        public View bgHot;
        @BindView(R.id.bg_new)
        public View bgNew;
        @BindView(R.id.bg_iced)
        public View bgIced;
        @BindView(R.id.bg_sweet)
        public View bgSweet;
        @BindView(R.id.iv_menu_item_right)
        public ImageView iv_menu_item_right;
        public SingleItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }
}
