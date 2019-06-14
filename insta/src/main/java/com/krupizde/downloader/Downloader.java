
package com.krupizde.downloader;

import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.File;
import java.net.URL;

public class Downloader {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$krupizde$downloader$Suffix;
    
    public Downloader() {
        super();
    }
    
    private boolean downloadPic(final IDownloadItem item) throws IllegalAccessException {
        InputStream is = null;
        OutputStream os = null;
        if (!this.createDestination(item.getDestinationFolder())) {
            return false;
        }
        try {
            final URL url = new URL(item.getUrl());
            is = url.openStream();
            os = new FileOutputStream(String.valueOf(item.getDestinationFolder()) + File.separator + item.getName() + "." + item.getSuffix().name().toLowerCase());
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        final byte[] b = new byte[(int)this.getFileSize(item.getUrl())];
        try {
            int length;
            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
            }
            os.flush();
        }
        catch (IOException e2) {
            e2.printStackTrace();
            return false;
        }
        finally {
            try {
                is.close();
                os.close();
            }
            catch (IOException ex) {}
        }
        try {
            is.close();
            os.close();
        }
        catch (IOException ex2) {}
        System.out.println("downloaded");
        return true;
    }
    
    private boolean downloadVideo(final IDownloadItem item, final SpeedObject speed) throws IOException {
        BufferedOutputStream outStream = null;
        InputStream is = null;
        if (!this.createDestination(item.getDestinationFolder())) {
            return false;
        }
        final File f = new File(String.valueOf(item.getDestinationFolder()) + File.separator + item.getName() + "." + item.getSuffix().name().toLowerCase());
        try {
            int byteRead = 0;
            final URL url = new URL(item.getUrl());
            outStream = new BufferedOutputStream(new FileOutputStream(f));
            final URLConnection conn = url.openConnection();
            is = conn.getInputStream();
            final byte[] buf = new byte[(int)this.getFileSize(item.getUrl())];
            long start = System.currentTimeMillis();
            long stop = 0L;
            long counter = 0L;
            while ((byteRead = is.read(buf)) != -1) {
                outStream.write(buf, 0, byteRead);
                counter += byteRead;
                if (speed != null) {
                    stop = System.currentTimeMillis();
                    if (stop < start + 1000L) {
                        continue;
                    }
                    synchronized (speed) {
                        speed.setSpeed((int)(counter / 1024L));
                    }
                    counter = 0L;
                    start = stop;
                }
            }
            outStream.flush();
        }catch(IOException e1) {
        	throw new IOException();
        }
        catch (Exception e) {
            return false;
        }
        finally {
            try {
                is.close();
                outStream.close();
                System.gc();
            }
            catch (IOException ex) {}
        }
        try {
            is.close();
            outStream.close();
			System.gc();
        }
        catch (IOException ex2) {}
        return true;
    }
    
    FileType extractMediumType(final Suffix suffix) {
        FileType medium = null;
        switch (suffix) {
            case PNG: {
                medium = FileType.PHOTO;
                break;
            }
            case JPG: {
                medium = FileType.PHOTO;
                break;
            }
            case MP4: {
                medium = FileType.VIDEO;
                break;
            }
            default: {
                return FileType.UNSUPPORTED;
            }
        }
        return medium;
    }
    
    public synchronized boolean  downloadMedium(final IDownloadItem item, final SpeedObject speed) throws IllegalAccessException, IOException {
        final FileType medium = this.extractMediumType(item.getSuffix());
        if (medium == FileType.UNSUPPORTED) {
            return false;
        }
        System.out.println(item.getDestinationFolder());
        return (medium == FileType.PHOTO) ? this.downloadPic(item) : this.downloadVideo(item, speed);
    }
    
    private boolean createDestination(final String destinationDir) {
        try {
            final File fl = new File(destinationDir);
            if (!fl.exists()) {
                fl.mkdirs();
            }
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public synchronized String getFinalLocation(final String address) throws IOException {
        final URL url = new URL(address);
        final HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        final int status = conn.getResponseCode();
        if (status != 200 && (status == 302 || status == 301 || status == 303)) {
            final String newLocation = conn.getHeaderField("Location");
            return this.getFinalLocation(newLocation);
        }
        return address;
    }
    
    public synchronized long getFileSize(final String urlString) throws IllegalAccessException {
        URL url = null;
        try {
            url = new URL(this.getFinalLocation(urlString));
        }
        catch (IOException e1) {
            e1.printStackTrace();
        }
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("HEAD");
            return conn.getContentLengthLong();
        }
        catch (IOException e2) {
            throw new IllegalAccessException();
        }
        finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
    
    static /* synthetic */ int[] $SWITCH_TABLE$com$krupizde$downloader$Suffix() {
        final int[] $switch_TABLE$com$krupizde$downloader$Suffix = Downloader.$SWITCH_TABLE$com$krupizde$downloader$Suffix;
        if ($switch_TABLE$com$krupizde$downloader$Suffix != null) {
            return $switch_TABLE$com$krupizde$downloader$Suffix;
        }
        final int[] $switch_TABLE$com$krupizde$downloader$Suffix2 = new int[Suffix.values().length];
        try {
            $switch_TABLE$com$krupizde$downloader$Suffix2[Suffix.JPG.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            $switch_TABLE$com$krupizde$downloader$Suffix2[Suffix.MP4.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError2) {}
        try {
            $switch_TABLE$com$krupizde$downloader$Suffix2[Suffix.PNG.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError3) {}
        return Downloader.$SWITCH_TABLE$com$krupizde$downloader$Suffix = $switch_TABLE$com$krupizde$downloader$Suffix2;
    }
}
