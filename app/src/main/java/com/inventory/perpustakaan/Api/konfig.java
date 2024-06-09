package com.inventory.perpustakaan.Api;

public class konfig {
    public static String ip = "beliaukece.cloud";
    public static String api = "https://" + ip + "/perpustakaan/php/query/";
    /* RS 192.168.5.147*/
    public static final String UrlImageBuku = "https://" + ip + "/perpustakaan/images/images_buku/";
    public static final String UrlImageProfile = "https://" + ip + "/perpustakaan/images/images_profile/";
    public static final String UrlUserLogin = api + "userlogin.php";
    public static final String UrlUserRegister = api + "userregister.php";
    public static final String UrlUserDaftar = api + "userdaftar.php";
    public static final String UrlUserProfile = api + "userprofile.php";
    public static final String UrlUserEditProfile = api + "usereditprofile.php";
    public static final String UrlGetDetailDataBuku = api + "urlgetdetaildatabuku.php";
    public static final String UrlGetDataBukuByRating = api + "urlgetdatabukubyrating.php";
    public static final String UrlGetDataBukuByRak = api + "urlgetdatabukubyrak.php";
    public static final String UrlGetDataPinjamBuku = api + "urlgetdatapinjambuku.php";
    public static final String UrlUserCekStatusMember = api + "usercekmember.php";
    public static final String UrlGetUlasan = api + "urlgetulasan.php";
    public static final String UrlAddUlasan = api + "addulasan.php";
    public static final String UrlPinjamBuku = api + "addpinjambuku.php";
    public static final String UrlUserLupaPassword = api + "userlupapass.php";
}