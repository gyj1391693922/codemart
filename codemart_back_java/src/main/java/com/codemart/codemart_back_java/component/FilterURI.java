package com.codemart.codemart_back_java.component;


import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class FilterURI {

    private ArrayList<String> arrayList = new ArrayList<>();

    public void addFilter(String... uri){

        for (String s : uri) {

            this.arrayList.add(s);

        }

    }

    public boolean isin(String uri){

        return this.arrayList.contains(uri);

    }

    public void scanFilterURL(){
        System.out.println(arrayList);
    }

    public void flushAll(){
        this.arrayList.clear();
    }

}
