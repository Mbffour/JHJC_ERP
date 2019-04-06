package com.jhj.comm;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class TypeModel<K,V,D> implements Serializable {


    private class TypeInfo<K,V> {
        private K key;
        private V value;

        public TypeInfo(K k ,V v ){
            key = k;
            value = v;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
        }
    }
    private List<TypeInfo<K,V>> data = new ArrayList<>();


    public List<TypeInfo<K, V>> getData() {
        return data;
    }

    public void setData(List<TypeInfo<K, V>> data) {
        this.data = data;
    }

    public TypeModel(List<D> list){
        init(list);
    }

    private void init(List<D> list){
        if(list==null||list.size()<=0)return;

        for(D d :list){
            data.add(new TypeInfo<K, V>(fillKey(d),fillValue(d)));
        }
    }

    abstract  public  V fillValue(D d) ;

    abstract public K  fillKey(D d);

}
