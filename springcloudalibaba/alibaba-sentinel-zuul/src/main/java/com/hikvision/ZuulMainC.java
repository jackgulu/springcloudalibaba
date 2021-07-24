package com.hikvision;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;

import java.util.Arrays;

public class ZuulMainC {
    public static void main(String[] args) {
        FlowRule rule=new FlowRule();
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule.setCount(0);
        rule.setResource("resourceA");
        rule.setRefResource("my-entrance-node2");
        rule.setStrategy(RuleConstant.STRATEGY_CHAIN);
        rule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_DEFAULT);
        FlowRuleManager.loadRules(Arrays.asList(rule));
        ContextUtil.enter("my-entrance-node1");
        Entry entryA=null;
        try {
            entryA= SphU.entry("resourceA");
        }catch (BlockException e){
            System.out.println("my-entrance-node1 block by Sentinel: "+e.getClass());
        }finally {
            if(entryA!=null){
                entryA.exit();
            }
            ContextUtil.exit();
        }

        ContextUtil.enter("my-entrance-node2");
        try {
            entryA= SphU.entry("resourceA");
        }catch (BlockException e){
            System.out.println("my-entrance-node2 block by Sentinel: "+e.getClass());
        }finally {
            if(entryA!=null){
                entryA.exit();
            }
            ContextUtil.exit();
        }


    }
}
