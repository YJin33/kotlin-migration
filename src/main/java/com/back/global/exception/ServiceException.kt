package com.back.global.exception;

import com.back.global.rsData.RsData

class ServiceException(
    private val msg: String,
    private val resultCode : String
): RuntimeException(msg) {

//    constructor(msg: String?, resultCode: String?) : this (msg,resultCode)
//
//    public ServiceException(String resultCode, String msg) {
//        super(msg);
//        this.msg = msg;
//        this.resultCode = resultCode;
//    }
//
    val rsData : RsData<Void>
        get() = RsData(msg, resultCode)
}
