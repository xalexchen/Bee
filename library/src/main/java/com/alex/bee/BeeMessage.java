package com.alex.bee;

/**
 * Created by Alex on 1/27/14.
 */
public class BeeMessage<T> {

    private Class<T> type;

    public BeeMessage(Class<T> type) {
        this.type = type;
    }

    public Class<T> getType() {
        return type;
    }

    public T newInstance() {
        try {
            return type.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
