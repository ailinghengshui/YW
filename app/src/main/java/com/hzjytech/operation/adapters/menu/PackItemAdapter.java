package com.hzjytech.operation.adapters.menu;

import android.content.Context;
import android.content.Intent;
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
import com.hzjytech.operation.module.menu.EditPackActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hzjytech.operation.constants.Constants.MenuAuth.AUTH_DELET_ADD;
import static com.hzjytech.operation.constants.Constants.MenuAuth.AUTH_EDIT;

/**
 * Created by hehongcan on 2017/5/3.
 */
public class PackItemAdapter extends RecyclerView.Adapter{
    private  Context context;
    private  List<MenuInfo.PacksBean> packs;
    private  LayoutInflater inflater;
    private boolean addOrDeletAuth=false;
    private boolean editAuth=false;
    private MenuInfo menuInfo;
    private int menuId;

    public PackItemAdapter(Context context, List<MenuInfo.PacksBean> packs) {
        this.context =context;
        this.packs=packs;
        inflater = LayoutInflater.from(context);
    }
    public void setPackData(
            MenuInfo menuInfo, List<MenuInfo.PacksBean> packs, int menuId, List<String> auth){
        this.menuInfo=menuInfo;
        this.packs=packs;
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
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.menu_item_pack, parent, false);
        return new PackItemViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PackItemViewHolder holder1 = (PackItemViewHolder) holder;
        final MenuInfo.PacksBean packsBean = packs.get(position);
        holder1.tvOrder.setText(position+1+"");
        holder1.tvName.setText(packsBean.getNameCh()+"/"+packsBean.getNameEn());
        holder1.tvSoldType.setText(packsBean.isSaleStatus()?"在售":"售罄");
        holder1.tvSoldType.setBackgroundResource(packsBean.isSaleStatus()?R.drawable.bg_sold_type_yes:R.drawable.bg_sold_type_no);
        holder1.tvOrginPrice.setText("原    价: "+packsBean.getOriginPrice()+"元");
        holder1.tvNowPrice.setText("现        价："+packsBean.getNowPrice()+"元");
        holder1.tvWeixinPrice.setText("微信价："+packsBean.getWxpayPrice()+"元");
        holder1.tvZfbPrice.setText("支付宝价："+packsBean.getAlipayPrice()+"元");
        holder1.mIv_right.setVisibility(View.VISIBLE);
        Glide.with(context)
                .load(packsBean.getUrl())
                .fitCenter()
                .placeholder(R.drawable.bg_photo)
                .dontAnimate()
                .into(holder1.ivPic);
        List<MenuInfo.PacksBean.CoffeesBean> coffees = packsBean.getCoffees();
        if(coffees!=null&&coffees.size()>0){
            holder1.ll_pack_container.removeAllViews();
            for (int j=0;j<coffees.size();j++){
                View itemView = inflater.inflate(R.layout.item_pack_item, null, false);
                TextView tv_name_count = (TextView) itemView.findViewById(R.id.tv_pack_item_name_count);
                if(coffees.get(j).getCount()>1){
                    tv_name_count.setText(coffees.get(j).getItemName()+"*"+coffees.get(j).getCount());
                }else{
                    tv_name_count.setText(coffees.get(j).getItemName());
                }

                holder1.ll_pack_container.addView(itemView);
            }
        }
        holder1.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditPackActivity.class);
                intent.putExtra("data",packsBean);
                intent.putExtra("menuInfo",menuInfo);
                intent.putExtra("addOrDeletAuth",addOrDeletAuth);
                intent.putExtra("editAuth",editAuth);
                intent.putExtra("menuId",menuId);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return packs == null ? 0 : packs.size();
    }

    public class PackItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_pack_order)
        public TextView tvOrder;
        @BindView(R.id.iv_pack_pic)
        ImageView ivPic;
        @BindView(R.id.tv_pack_name)
        public TextView tvName;
        @BindView(R.id.tv_pack_sold_type)
        public TextView tvSoldType;
        @BindView(R.id.tv_pack_orgin_price)
        public TextView tvOrginPrice;
        @BindView(R.id.tv_pack_weixin_price)
        public TextView tvWeixinPrice;
        @BindView(R.id.tv_pack_now_price)
        public TextView tvNowPrice;
        @BindView(R.id.tv_pack_zfb_price)
        public TextView tvZfbPrice;
        @BindView(R.id.ll_pack_item_container)
        public LinearLayout ll_pack_container;
        @BindView(R.id.iv_menu_pack_right)
        public ImageView mIv_right;
        public PackItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }
}
