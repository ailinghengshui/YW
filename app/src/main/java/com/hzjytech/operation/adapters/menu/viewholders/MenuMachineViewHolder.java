package com.hzjytech.operation.adapters.menu.viewholders;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hzjytech.operation.R;
import com.hzjytech.operation.entity.MenuInfo;
import com.hzjytech.operation.inter.MoreMachineClickListener;
import com.hzjytech.operation.inter.OnInfoClickListener;
import com.hzjytech.operation.module.machine.DetailMachineActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hzjytech.operation.constants.Constants.GroupClick.MACHINEADDMORE;
import static com.hzjytech.operation.constants.Constants.GroupClick.MACHINESETTING;
import static com.hzjytech.operation.constants.Constants.MenuClick.ADD_MACHINE;
import static com.hzjytech.operation.constants.Constants.MenuClick.EDIT_MACHINE;

/**
 * Created by hehongcan on 2017/5/3.
 */
public class MenuMachineViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.ll_machines_container)
    LinearLayout llMachinesContainer;
    public MenuMachineViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
    public void setMenuMachineData(
            final Context context,
            final List<MenuInfo.SubMachinesBean> machines,
            boolean AUTH_EDIT_MACHINE,
            final OnInfoClickListener moreMachineClickListener){
        LayoutInflater inflater = LayoutInflater.from(context);
        View head = llMachinesContainer.getChildAt(0);
        View lineView = llMachinesContainer.getChildAt(1);
        View foot = inflater.inflate(R.layout.item_footer,null, false);
        TextView mTvCheckMoreMachine = (TextView) foot.findViewById(R.id.tv_check_more_group);
        TextView mTvAddMachine = (TextView) foot.findViewById(R.id.tv_add_group);
        llMachinesContainer.removeAllViews();
        llMachinesContainer.addView(head);
        llMachinesContainer.addView(lineView);
        llMachinesContainer.addView(foot);
        if(machines==null||machines.size()==0){
            if(!AUTH_EDIT_MACHINE){
                foot.setVisibility(View.GONE);
            }
            mTvAddMachine.setVisibility(View.VISIBLE);
            mTvCheckMoreMachine.setVisibility(View.INVISIBLE);
            foot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(moreMachineClickListener!=null){
                        moreMachineClickListener.click(ADD_MACHINE);
                    }
                }
            });
        }else{
            mTvAddMachine.setVisibility(View.INVISIBLE);
            mTvCheckMoreMachine.setVisibility(View.VISIBLE);
            if(machines.size()==1){
                if(!AUTH_EDIT_MACHINE){
                    llMachinesContainer.removeViewAt(2);
                }
                View view = inflater.inflate(R.layout.item_machines, null, false);
                llMachinesContainer.addView(view, 2);
                View view1 = llMachinesContainer.getChildAt(2);
                TextView tv_machine_order = (TextView) view1.findViewById(R.id.tv_machine_order);
                TextView tv_machine_name = (TextView) view1.findViewById(R.id.tv_machine_name);
                TextView tv_machine_location = (TextView) view1.findViewById(R.id
                        .tv_machine_location);
                TextView tv_machine_id = (TextView) view1.findViewById(R.id.tv_machine_item_id);
                tv_machine_order.setText(1 + "");
                tv_machine_id.setText(machines.get(0)
                        .getId() + "");
                tv_machine_name.setText(machines.get(0)
                        .getName());
                tv_machine_location.setText(machines.get(0)
                        .getAddress());
                view1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, DetailMachineActivity.class);
                        intent.putExtra("machineId",
                                machines.get(0)
                                        .getId());
                        context.startActivity(intent);
                    }
                });
            }else{
                if (machines.size() == 2&&!AUTH_EDIT_MACHINE) {
                    llMachinesContainer.removeViewAt(2);
                }
                for (int i = 0; i < 2; i++) {
                    View view = inflater.inflate(R.layout.item_machines, null, false);
                    llMachinesContainer.addView(view, 2);
                }
                for (int i = 0; i < 2; i++) {
                    View view1 = llMachinesContainer.getChildAt(2 + i);
                    TextView tv_machine_order = (TextView) view1.findViewById(R.id
                            .tv_machine_order);
                    TextView tv_machine_name = (TextView) view1.findViewById(R.id.tv_machine_name);
                    TextView tv_machine_location = (TextView) view1.findViewById(R.id
                            .tv_machine_location);
                    TextView tv_machine_id = (TextView) view1.findViewById(R.id.tv_machine_item_id);
                    tv_machine_order.setText(1 + i + "");
                    tv_machine_id.setText(machines.get(i)
                            .getId() + "");
                    tv_machine_name.setText(machines.get(i)
                            .getName());
                    tv_machine_location.setText(machines.get(i)
                            .getAddress());
                }
                llMachinesContainer.getChildAt(2)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, DetailMachineActivity.class);
                                intent.putExtra("machineId",
                                        machines.get(0)
                                                .getId());
                                context.startActivity(intent);
                            }
                        });
                llMachinesContainer.getChildAt(3)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, DetailMachineActivity.class);
                                intent.putExtra("machineId",
                                        machines.get(1)
                                                .getId());
                                context.startActivity(intent);
                            }
                        });


            }
            foot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(moreMachineClickListener!=null){
                        moreMachineClickListener.click(EDIT_MACHINE);
                    }
                }
            });
        }
    }
}
