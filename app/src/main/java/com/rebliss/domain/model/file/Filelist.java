package com.rebliss.domain.model.file;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Filelist {
    @SerializedName("outside_photos")
    @Expose
    private List<File> outsidePhotos = null;
}
