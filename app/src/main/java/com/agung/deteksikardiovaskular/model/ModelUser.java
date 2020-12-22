package com.agung.deteksikardiovaskular.model;

public class ModelUser {
    private String id;
    private String name;
    private String email;
    private String instansi;
    private String photoProfil;

    public ModelUser(String name, String email, String instansi, String photoProfil) {
        this.name = name;
        this.email = email;
        this.instansi = instansi;
        this.photoProfil = photoProfil;
    }

    public String getId(String key) {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhotoProfil() {
        return photoProfil;
    }

    public void setPhotoProfil(String photoProfil) {
        this.photoProfil = photoProfil;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getInstansi() {
        return instansi;
    }

    public void setInstansi(String instansi) {
        this.instansi = instansi;
    }




}
