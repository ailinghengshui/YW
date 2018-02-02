package com.hzjytech.operation.entity;

/**
 * Created by hehongcan on 2017/9/25.
 */

import java.util.List;

/**
 * {"id":144,"name":"testnwl","subMachines":[{"machineId":56,"machineName":"test0006","address":"江干区白杨街道杭州市高科技企业孵化园区","franchiseeId":14,"franchiseeName":"pctest"}]}
 */
public class AddMenuMachineInfo {

    /**
     * id : 144
     * name : testnwl
     * subMachines : [{"machineId":56,"machineName":"test0006","address":"江干区白杨街道杭州市高科技企业孵化园区",
     * "franchiseeId":14,"franchiseeName":"pctest"}]
     */

    private int id;
    private String name;
    private List<SubMachinesBean> subMachines;

    public int getId() { return id;}

    public void setId(int id) { this.id = id;}

    public String getName() { return name;}

    public void setName(String name) { this.name = name;}

    public List<SubMachinesBean> getSubMachines() { return subMachines;}

    public void setSubMachines(List<SubMachinesBean> subMachines) { this.subMachines = subMachines;}

    public static class SubMachinesBean {
        /**
         * machineId : 56
         * machineName : test0006
         * address : 江干区白杨街道杭州市高科技企业孵化园区
         * franchiseeId : 14
         * franchiseeName : pctest
         */

        private int machineId;
        private String machineName;
        private String address;
        private int franchiseeId;
        private String franchiseeName;

        public int getMachineId() { return machineId;}

        public void setMachineId(int machineId) { this.machineId = machineId;}

        public String getMachineName() { return machineName;}

        public void setMachineName(String machineName) { this.machineName = machineName;}

        public String getAddress() { return address;}

        public void setAddress(String address) { this.address = address;}

        public int getFranchiseeId() { return franchiseeId;}

        public void setFranchiseeId(int franchiseeId) { this.franchiseeId = franchiseeId;}

        public String getFranchiseeName() { return franchiseeName;}

        public void setFranchiseeName(String franchiseeName) { this.franchiseeName = franchiseeName;}
    }
}
