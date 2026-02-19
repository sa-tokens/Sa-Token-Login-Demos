package com.pj.stld.utils;

import java.util.Arrays;
import java.util.List;

/**
 * Ajax发生异常时，直接抛出此异常即可
 *
 * @author click33
 */
public class AjaxError extends RuntimeException {

	
	/** 以下元素会在isNull函数中被判定为Null， */
	public static final Object[] NULL_ELEMENT_ARRAY = {null, "", 0, 0L, 0.0, "0", "0.0"};
	public static final List<Object> NULL_ELEMENT_LIST;

	static {
		NULL_ELEMENT_LIST = Arrays.asList(NULL_ELEMENT_ARRAY);
	}

	
	// ========================= 定义属性 =========================  
	
	private static final long serialVersionUID = 1L; 
	
	private int code = 500;		// 底层code码
	private Object extra;		// 额外数据，设置的值将会被返回给前端
	/**
	 * @return 获取code码  
	 */
	public int getCode() {
		return code;
	}
	/**
	 * @return 写入code码 ，连缀风格 
	 */
	public AjaxError setCode(int code) {
		this.code = code;
		return this;
	}
	/**
	 * @return 获取 extra 数据
	 */
	public Object getExtra() {
		return extra;
	}
	/**
	 * @return 写入 extra 数据 ，连缀风格
	 */
	public AjaxError setExtra(Object extra) {
		this.extra = extra;
		return this;
	}
	
	
	// ========================= 构造方法 =========================  

	public AjaxError(int code, String message) {
        super(message);
		setCode(code);
    }
	public AjaxError(String message) {
        super(message);
    }
	public AjaxError(Throwable e) {
        super(e);
    }
	public AjaxError(String message, Throwable e) {
        super(message, e);
    }
	

	// ========================= 获取相关 =========================  
	
	/** 获得一个异常AjaxError */
	public static AjaxError get(String errorMsg){
		return new AjaxError(errorMsg);
	}
	/** 获得一个异常AjaxError */
	public static AjaxError get(int code, String errorMsg){
		return new AjaxError(code, errorMsg);
	}
	/** 获得一个异常AjaxError */
	public static AjaxError get(Throwable e){
		return new AjaxError(e);
	}
	

	// ========================= 获取并抛出 =========================  
	
	/** 抛出一个异常 */
	public static void throwMsg(String errorMsg) {
		throw new AjaxError(errorMsg);
	}

	/** 值不能是 true，否则抛出异常 */
	public static void notTrue(boolean bo, int code, String errorMsg) {
		if(bo) {
			throw get(code, errorMsg);
		}
	}
	/** 值不能是 true，否则抛出异常 */
	public static void notTrue(boolean bo, String errorMsg) {
		if(bo) {
			throw get(errorMsg);
		}
	}
	/** 值不能是 true，否则抛出异常 */
	public static void notTrue(boolean bo) {
		if(bo) {
			throw get("error");
		}
	}
	

	/** 如果是无效受影响行数，则抛出异常 (大于0通过，小于等于0抛出error) */ 
	public static void notLine(int line, int code, String errorMsg){
		if(line <= 0){
			throw get(code, errorMsg);
		}
	}
	/** 如果是无效受影响行数，则抛出异常 (大于0通过，小于等于0抛出error) */ 
	public static void notLine(int line, String errorMsg){
		if(line <= 0){
			throw get(errorMsg);
		}
	}
	/** 如果是无效受影响行数，则抛出异常 (大于0通过，小于等于0抛出error) */ 
	public static void notLine(int line){
		if(line <= 0){
			throw get("受影响行数：0");
		}
	}
	


	/** 值不能是 null，否则抛出异常 */ 
	public static void notIsNull(Object value, int code, String errorMsg){
		if(isNull(value)){
			throw get(code, errorMsg);
		}
	}
	/** 值不能是 null，否则抛出异常 */ 
	public static void notIsNull(Object value, String errorMsg){
		if(isNull(value)){
			throw get(errorMsg);
		}
	}
	/** 值不能是 null，否则抛出异常 */ 
	public static void notIsNull(Object value){
		if(isNull(value)){
			throw get("不能为空");
		}
	}
	
	/** 
	 * 指定值是否为以下其一：null、""、0、"0"  
	 */
	public static boolean isNull(Object value) {
		return NULL_ELEMENT_LIST.contains(value);
	}
	
}
