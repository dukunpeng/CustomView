package com.mark.views.common.observer;


import java.util.HashSet;
import java.util.Set;

/**
 * @author Mark
 * @Date on 2018/11/22
 **/
public abstract class AbstractObservable<T> implements Observable<T> {

    private Set<Observer> observerSet;

    @Override
    final public void register(Observer observer) {

        if (observerSet == null) {
            observerSet = new HashSet<>();
        }
        observerSet.add(observer);
    }

    @Override
    final public void unRegister(Observer observer) {

        observerSet.remove(observer);
    }

    @Override
    public void notify(T t, int type) {

        if (observerSet != null) {
            for (Observer observer :
                    observerSet) {
                observer.update(t, type);
            }
        }
    }

    public void recycle() {
        observerSet.clear();
        observerSet = null;
    }

/*    //以下实现单例的可继承性

    private static Map<String, AbstractObservable> registryMap = new HashMap<String, AbstractObservable>();

    *//**
     * 防止子类new对象
     * @throws SingletonException
     *//*
     AbstractObservable() throws SingletonException{
        String clazzName = this.getClass().getName();
        if (registryMap.containsKey(clazzName)){
            throw new SingletonException("Cannot construct instance for class " + clazzName + ", since an instance already exists!");
        } else {
            synchronized(registryMap){
                if (registryMap.containsKey(clazzName)){
                    throw new SingletonException("Cannot construct instance for class " + clazzName + ", since an instance already exists!");
                } else {
                    registryMap.put(clazzName, this);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends AbstractObservable> T getInstance(final Class<T> clazz) throws InstantiationException, IllegalAccessException{
        String clazzName = clazz.getName();
        if(!registryMap.containsKey(clazzName)){
            synchronized(registryMap){
                if(!registryMap.containsKey(clazzName)){
                    T instance = clazz.newInstance();
                    return instance;
                }
            }
        }
        return (T) registryMap.get(clazzName);
    }

    public static AbstractObservable getInstance(final String clazzName)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException{
        if(!registryMap.containsKey(clazzName)){
            Class<? extends AbstractObservable> clazz = Class.forName(clazzName).asSubclass(AbstractObservable.class);
            synchronized(registryMap){
                if(!registryMap.containsKey(clazzName)){
                    AbstractObservable instance = clazz.newInstance();
                    return instance;
                }
            }
        }
        return registryMap.get(clazzName);
    }

    @SuppressWarnings("unchecked")
    public static <T extends AbstractObservable> T getInstance(final Class<T> clazz, Class<?>[] parameterTypes, Object[] initargs)
            throws SecurityException, NoSuchMethodException, IllegalArgumentException,
            InvocationTargetException, InstantiationException, IllegalAccessException{
        String clazzName = clazz.getName();
        if(!registryMap.containsKey(clazzName)){
            synchronized(registryMap){
                if(!registryMap.containsKey(clazzName)){
                    Constructor<T> constructor = clazz.getConstructor(parameterTypes);
                    T instance = constructor.newInstance(initargs);
                    return instance;
                }
            }
        }
        return (T) registryMap.get(clazzName);
    }

   public static class SingletonException extends Exception {
        *//**
     *
     *//*
        private static final long serialVersionUID = -8633183690442262445L;

        private SingletonException(String message){
            super(message);
        }
    }*/
}
