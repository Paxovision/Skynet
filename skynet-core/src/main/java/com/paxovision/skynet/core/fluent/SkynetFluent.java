package com.paxovision.skynet.core.fluent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface SkynetFluent<T> {
    public default T given(String msg){
        Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.info(msg);
        return (T) this;
    }
    public default T given(){
        return (T) this;
    }
    public default T when(String msg){
        Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.info(msg);
        return (T) this;
    }
    public default T when(){
        return (T) this;
    }
    public default T then(String msg){
        Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.info(msg);
        return (T) this;
    }
    public default T then(){
        return (T) this;
    }
    public default T and(String msg){
        Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.info(msg);
        return (T) this;
    }
    public default T and(){
        return (T) this;
    }
}
