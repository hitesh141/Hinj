package com.hinj.app.utils;

import java.io.File;
import java.io.FileFilter;

public class SolarexFilter implements FileFilter {

    @Override
    public boolean accept(File pathname) {
        boolean accepted = true;
        if (pathname.getName().startsWith(".")) {
            accepted = false;
        }
        return accepted;
    }

}
