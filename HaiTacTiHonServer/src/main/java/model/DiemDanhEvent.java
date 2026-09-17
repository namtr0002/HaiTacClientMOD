package model;

import zabstracts.AbsDiemDanh;

public class DiemDanhEvent extends AbsDiemDanh {
    private static DiemDanhEvent instance;

    public DiemDanhEvent() {
        this.id = 1;
        this.title = "Quà Báo Danh";
        this.buttonText = "Báo Danh";
        this.itemIds = new short[]{359, 349, 339, 158, 359, 866, 359, 866, 349, 866};
        this.itemNums = new short[]{10, 3, 2, 10, 20, 10, 20, 10, 5, 10};
        this.idItemBack = -999;
        this.catBack = 4;
        this.key = "DIEM_DANH_EVENT";
    }

    public static DiemDanhEvent gI() {
        if (instance == null) {
            instance = new DiemDanhEvent();
        }
        return instance;
    }
}
