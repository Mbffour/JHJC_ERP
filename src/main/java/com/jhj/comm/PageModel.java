package com.jhj.comm;

import java.util.List;

public class PageModel<T> {

    private int total;
    private List<T> items;
    private Object ext;


    private PageModel(){};

    public static PageModel genData(List items,int total,Object ext){
        PageModel pageModel = genData(items, total);
        if(pageModel==null)return null;
        pageModel.setExt(ext);
        return pageModel;
    }
    public static PageModel genData(List items,int total){
        if(items==null||items.size()<=0){
            return null;
        }
        PageModel pageModel = new PageModel();
        pageModel.setItems(items);
        pageModel.setTotal(total);
        return pageModel;
    }

    public Object getExt() {
        return ext;
    }

    public void setExt(Object ext) {
        this.ext = ext;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
