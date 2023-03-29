package com.sophra.pdf_merge;

public class Files {
    String name;
    String storage;

    public Files(String name, String storage) {
        this.name = name;
        this.storage = storage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }
}
