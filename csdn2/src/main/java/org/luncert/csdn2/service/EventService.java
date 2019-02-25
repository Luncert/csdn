package org.luncert.csdn2.service;

import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.luncert.csdn2.util.NormalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventService
{

    @Autowired
    private LogService logService;

    private Map<String, List<EventListener>> listeners = new HashMap<>();

    /**
     * 发布事件，EventService会根据参数列表到注册的EventListener中寻找合适的事件处理方法
     * @param eventName
     * @param params
     */
    public void submit(String eventName, Object...params)
    {
        List<EventListener> list = listeners.get(eventName);
        if (list != null) {
            for (EventListener listener : list)
            {
                Method method = getQulifiedMethod(listener, params);
                if (method != null)
                {
                    try {
                        method.invoke(listener, params);
                    } catch (Exception e) {
                        logService.error("exception on submit(" + eventName + ")",
                            NormalUtil.throwableToString(e));
                    }
                }
            }
        }
    }

    /**
     * 寻找长度和参数类型都一致的method
     */
    private Method getQulifiedMethod(Object object, Object[] params)
    {

        for (Method method : object.getClass().getMethods())
        {
            Class<?>[] types = method.getParameterTypes();
            int i = 0, len = types.length;
            if (len != params.length)
                continue;
            for (; i < len && types[i].equals(params[i].getClass()); i++);
            if (i == len)
                return method;
        }
        return null;
    }

    /**
     * 注册EventListener到事件上，EventService会根据事件发布时携带的参数列表
     * 去EventListener中寻找合适的方法处理时间，注意，只有public类型方法会被
     * 作为搜索目标
     * @param eventName
     * @param listener
     */
    public void register(String eventName, EventListener listener)
    {
        List<EventListener> list = listeners.get(eventName);
        if (list == null)
        {
            list = new LinkedList<>();
            listeners.put(eventName, list);
        }
        list.add(listener);
    }

    /**
     * 注销EventListener
     * @param eventName
     * @param listener
     */
    public void unregister(String eventName, EventListener listener)
    {
        List<EventListener> list = listeners.get(eventName);
        if (list != null)
            list.remove(listener);
    }

}