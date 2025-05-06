package org.lia;

import jakarta.enterprise.context.ApplicationScoped;

import javax.faces.bean.ManagedBean;

@ManagedBean(name = "mBean")
@ApplicationScoped
public class MBean {
    private long cnt_total;
    private long cnt_not_in_area;
    private volatile String message15;

    public long getCnt_not_in_area() {
        return cnt_not_in_area;
    }

    public void setCnt_not_in_area(long cnt_not_in_area) {
        this.cnt_not_in_area = cnt_not_in_area;
    }

    public long getCnt_total() {
        return cnt_total;
    }

    public void setCnt_total(long cnt_total) {
        this.cnt_total = cnt_total;
    }

    public synchronized void setMessage15(String message15) {
        this.message15 = message15;
        System.out.println("set message15: " + this.message15);
    }

    public synchronized String getMessage15() {
        System.out.println(">> " + this.message15);
        return this.message15;
    }

    public synchronized void makeMessage15() {
        if (cnt_total % 15 == 0) {
            this.message15 = "Количество точек кратно 15";
            System.out.println(message15);
        } else {
            this.message15 = "";
        }
    }

    public float calc_percent_misses() {
        return (float)cnt_not_in_area / cnt_total * 100;
    }

}
