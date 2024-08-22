package com.inventory.perpustakaan.Model;

public class ModelBuku {
//    private String id_buku;
    private String nama_buku;
    private String penulis;
    private String penerbit;
    private String nisn_isbn;
    private String tahun_terbit;
    private String halaman_buku;
    private String id_rak;
    private String stok;
    private String tentang;
    private String gambar_buku;
    private String rating;

    public ModelBuku(String nama_buku, String penulis, String penerbit, String nisn_isbn, String tahun_terbit, String halaman_buku, String id_rak, String stok, String tentang, String gambar_buku, String rating) {
//        this.id_buku = id_buku;
        this.nama_buku = nama_buku;
        this.penulis = penulis;
        this.penerbit = penerbit;
        this.nisn_isbn = nisn_isbn;
        this.tahun_terbit = tahun_terbit;
        this.halaman_buku = halaman_buku;
        this.id_rak = id_rak;
        this.stok = stok;
        this.tentang = tentang;
        this.gambar_buku = gambar_buku;
        this.rating = rating;
    }

//    public String getId_buku() {
//        return id_buku;
//    }

    public String getNama_buku() {
        return nama_buku;
    }

    public String getPenulis() {
        return penulis;
    }

    public String getPenerbit() {
        return penerbit;
    }

    public String getNisn_isbn() {
        return nisn_isbn;
    }

    public String getTahun_terbit() {
        return tahun_terbit;
    }

    public String getHalaman_buku() {
        return halaman_buku;
    }

    public String getId_rak() {
        return id_rak;
    }

    public String getStok() {
        return stok;
    }

    public String getTentang() {
        return tentang;
    }

    public String getGambar_buku() {
        return gambar_buku;
    }

    public String getRating() {
        return rating;
    }

//    public void setid_buku(String id_buku) {
//        this.id_buku = id_buku;
//    }

    public void setNama_buku(String nama_buku) {
        this.nama_buku = nama_buku;
    }

    public void setPenulis(String penulis) {
        this.penulis = penulis;
    }

    public void setPenerbit(String penerbit) {
        this.penerbit = penerbit;
    }

    public void setNisn_isbn(String nisn_isbn) {
        this.nisn_isbn = nisn_isbn;
    }

    public void setTahun_terbit(String tahun_terbit) {
        this.tahun_terbit = tahun_terbit;
    }

    public void setHalaman_buku(String halaman_buku) {
        this.halaman_buku = halaman_buku;
    }

    public void setId_rak(String id_rak) {
        this.id_rak = id_rak;
    }

    public void setStok(String stok) {
        this.stok = stok;
    }

    public void setTentang(String tentang) {
        this.tentang = tentang;
    }

    public void setGambar_buku(String gambar_buku) {
        this.gambar_buku = gambar_buku;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

}