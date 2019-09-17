package com.estimote.showroom.estimote;

import java.util.Arrays;

public class Product {
    private String name;
    private int[] sum;
    private int rssi;
    private String summary;
    private int counter;

    public Product(String name, String summary) {
        this.name = name;
        this.sum = new int[5];
        this.summary = summary;
        this.counter=0;
    }

    public String getName() {
        return name;
    }

    public void setSummary(int rrsi) {
        this.rssi = rrsi;
        if(this.counter<5){
            this.sum[this.counter++] = rrsi;
        }
    }

    public String getRssi() {
        return Integer.toString(this.rssi);
    }

    public String getSum(){
        int result=0;
        for(int i : sum) {
            result += i;
        }
        result /= counter;
        return Integer.toString(result);
    }

    public String getCounter(){
        return Integer.toString(counter);
    }

    public void flush(){
        Arrays.fill(sum, 0);
        counter = 0;
    }
}