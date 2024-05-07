package com.inventory.perpustakaan.Model;

public class ModelUlasan {

    private int id_ulasan;
    private int id_buku;
    private int rating;
    private String ulasan;
    private String nama;
    private String gambarbuku;

    public ModelUlasan(int id_ulasan, int id_buku, int rating, String ulasan, String gambarbuku, String nama) {
        this.id_ulasan = id_ulasan;
        this.id_buku = id_buku;
        this.rating = rating;
        this.ulasan = ulasan;
        this.gambarbuku = gambarbuku;
        this.nama = nama;

    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getGambarbuku() {
        return gambarbuku;
    }

    public void setGambarbuku(String gambarbuku) {
        this.gambarbuku = gambarbuku;
    }

    public int getId_ulasan() {
        return id_ulasan;
    }

    public void setId_ulasan(int id_ulasan) {
        this.id_ulasan = id_ulasan;
    }

    public int getId_buku() {
        return id_buku;
    }

    public void setId_buku(int id_buku) {
        this.id_buku = id_buku;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getUlasan() {
        return ulasan;
    }

    public void setUlasan(String ulasan) {
        this.ulasan = ulasan;
    }
}