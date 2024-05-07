package com.inventory.perpustakaan.Model;

public class ModelPinjamBuku {
    private String id_pinjam;
    private String tgl_pinjam;
    private String tgl_kembali;
    private String nama_buku;
    private String gambar_buku;
    private String denda;

    public ModelPinjamBuku(String id_pinjam, String nama_buku, String gambar_Buku, String tgl_pinjam, String tgl_kembali, String denda) {
        this.nama_buku = nama_buku;
        this.gambar_buku = gambar_Buku;
        this.id_pinjam = id_pinjam;
        this.tgl_pinjam = tgl_pinjam;
        this.tgl_kembali = tgl_kembali;
        this.denda = denda;
    }

    public String getId_pinjam() {
        return id_pinjam;
    }

    public void setId_pinjam(String id_pinjam) {
        this.id_pinjam = id_pinjam;
    }

    public String getTgl_pinjam() {
        return tgl_pinjam;
    }

    public void setTgl_pinjam(String tgl_pinjam) {
        this.tgl_pinjam = tgl_pinjam;
    }

    public String getTgl_kembali() {
        return tgl_kembali;
    }

    public void setTgl_kembali(String tgl_kembali) {
        this.tgl_kembali = tgl_kembali;
    }

    public String getNama_buku() {
        return nama_buku;
    }

    public void setNama_buku(String nama_buku) {
        this.nama_buku = nama_buku;
    }

    public String getGambar_buku() {
        return gambar_buku;
    }

    public void setGambar_buku(String gambar_buku) {
        this.gambar_buku = gambar_buku;
    }

    public String getDenda() {
        return denda;
    }

    public void setDenda(String denda) {
        this.denda = denda;
    }
}