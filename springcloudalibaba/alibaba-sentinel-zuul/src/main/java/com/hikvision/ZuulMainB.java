package com.hikvision;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;

public class ZuulMainB {
    public static void main(String[] args) {
        ContextUtil.enter("my-entrance-node");
        Entry entryA=null;
        Entry entryB=null;
        Entry entryC=null;
        try {
            entryA= SphU.entry("resourceA");
            entryA.exit();
            entryB=SphU.entry("resourceB");
            entryB.exit();
            entryC=SphU.entry("resourceC");
            int a=1,b=2;
            int c=a+b;
            System.out.println(c);
        }catch (BlockException e){
            System.err.println("block by Sentinel:"+e.getClass());
        }finally {
            if(entryC!=null){
                entryC.exit();
            }
            ContextUtil.exit();
        }
    }
}
