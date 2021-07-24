package com.hikvision.context;

public class RibbonRequestContextHolder {
    private static ThreadLocal<RibbonRequestContext> holder=new ThreadLocal<RibbonRequestContext>(){
        @Override
        protected RibbonRequestContext initialValue() {
            return new RibbonRequestContext();
        }
    };

    public static RibbonRequestContext getCurrentContext(){
        return holder.get();
    }

    public static void setCurrentContext(RibbonRequestContext ribbonRequestContext){
        holder.set(ribbonRequestContext);
    }
    public static void clearContext(){
        holder.remove();
    }
}
