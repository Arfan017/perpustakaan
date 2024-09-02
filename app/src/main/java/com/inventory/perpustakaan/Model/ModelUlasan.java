package com.inventory.perpustakaan.Model;

public class ModelUlasan {

    private int id_ulasan;
    private String nisn_isbn;
    private int rating;
    private String ulasan;
    private String nama;
    private String gambarbuku;

    public ModelUlasan(int id_ulasan, String nisn_isbn, int rating, String ulasan, String gambarbuku, String nama) {
        this.id_ulasan = id_ulasan;
        this.nisn_isbn = nisn_isbn;
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

    public String getnisn_isbn() {
        return nisn_isbn;
    }

    public void setnisn_isbn(String nisn_isbn) {
        this.nisn_isbn = nisn_isbn;
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