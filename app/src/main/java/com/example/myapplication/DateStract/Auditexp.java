/**
 * Copyright 2020 bejson.com
 */
package com.example.myapplication.DateStract;

import java.io.Serializable;

/**
 * Auto-generated: 2020-03-23 4:21:52
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Auditexp implements Serializable {//审计实例

    private String uuid_Tocken;//对应的令牌UUID
    private String RNG;//随机数
    private String time;//生成时间
    private Accexp accexp;//授权实例
    public void setUuid_Tocken(String uuid_Tocken) {
        this.uuid_Tocken = uuid_Tocken;
    }
    public String getUuid_Tocken() {
        return uuid_Tocken;
    }

    public void setRNG(String RNG) {
        this.RNG = RNG;
    }
    public String getRNG() {
        return RNG;
    }

    public void setTime(String time) {
        this.time = time;
    }
    public String getTime() {
        return time;
    }

    public void setAccexp(Accexp accexp) {
        this.accexp = accexp;
    }
    public Accexp getAccexp() {
        return accexp;
    }

}