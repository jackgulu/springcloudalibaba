package com.hikvision;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;

public class ZuulMainA {
    public static void main(String[] args) {
        ContextUtil.enter("my-entrance-node");
        Entry entryA=null;
        Entry entryB=null;
        Entry entryC=null;
        try {
            entryA= SphU.entry("resourceA");
            entryB=SphU.entry("resourceB");
            entryC=SphU.entry("resourceC");
        }catch (BlockException e){
            System.out.println("block by sentinel: "+e.getClass());
        }finally {
            if(entryC!=null){
                entryC.exit();
            }
            if(entryB!=null){
                entryB.exit();
            }
            if(entryC!=null){
                entryC.exit();
            }
            ContextUtil.exit();
        }
    }
}
