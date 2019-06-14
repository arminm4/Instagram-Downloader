// Decompiled using: fernflower
// Took: 8ms

package com.krupizde.downloader;

public interface IDownloadItem
{
    String getUrl();
    
    Suffix getSuffix();
    
    String getName();
    
    long getSizeBytes();
    
    String getDestinationFolder();
}
