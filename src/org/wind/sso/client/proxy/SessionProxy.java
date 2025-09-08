package org.wind.sso.client.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @描述 : session代理类——主要是对session数据（Map类型）做控制
 * @作者 : 胡璐璐
 * @时间 : 2020年11月29日 13:54:50
 */
public class SessionProxy implements InvocationHandler {

	private Map<Object,Object> obj;
	private Map<Object,Map<String,Object>> handlerKeyMap;		//处理的关键字Map（key=obj的key；value=Map【key=操作；value=对应操作的值】）
	
    public Map<Object, Map<String, Object>> getHandlerKeyMap() {
		return handlerKeyMap;
	}
	public SessionProxy(Map<Object,Object> obj) {
        this.obj = obj;
        this.handlerKeyMap=new HashMap<Object, Map<String,Object>>();
    }
    
    /**反射执行**/
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object invoke = method.invoke(this.obj, args);
        String methodName=method.getName();
        //对put、remove做控制
        if(methodName.equals("put") || methodName.equals("remove")) {
        	String key=(String) args[0];
        	Map<String,Object> t_valueMap=handlerKeyMap.get(key);
        	if(t_valueMap==null) {
        		t_valueMap=new HashMap<String, Object>();
        		handlerKeyMap.put(key, t_valueMap);
        	}
	        if(methodName.equals("put")) {
	        	t_valueMap.put("type", 1);	//添加或修改
	        	t_valueMap.put("value", args[1]);	//添加或修改
	        }else if(methodName.equals("remove")) {
	        	t_valueMap.put("type", 2);	//删除
	        }
		}
        return invoke;
	}

}
