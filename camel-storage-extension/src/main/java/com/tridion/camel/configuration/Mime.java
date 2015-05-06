package com.tridion.camel.configuration;

import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: AEY5753
 * Date: 10/04/15
 * Time: 08:54
 * To change this template use File | Settings | File Templates.
 */
public class Mime {

  private static Properties mimeMap = new Properties();
  static {
    mimeMap.put("abs", "audio/x-mpeg");
    mimeMap.put("ai", "application/postscript");
    mimeMap.put("air","application/vnd.adobe.apollo-application-installer-package+zip");
    mimeMap.put("aif", "audio/x-aiff");
    mimeMap.put("aifc", "audio/x-aiff");
    mimeMap.put("aiff", "audio/x-aiff");
    mimeMap.put("aim", "application/x-aim");
    mimeMap.put("art", "image/x-jg");
    mimeMap.put("asf", "video/x-ms-asf");
    mimeMap.put("asx", "video/x-ms-asf");
    mimeMap.put("au", "audio/basic");
    mimeMap.put("avi", "video/x-msvideo");
    mimeMap.put("avx", "video/x-rad-screenplay");
    mimeMap.put("bcpio", "application/x-bcpio");
    mimeMap.put("bin", "application/octet-stream");
    mimeMap.put("bmp", "image/bmp");
    mimeMap.put("body", "text/html");
    mimeMap.put("cdf", "application/x-cdf");
    mimeMap.put("cer", "application/x-x509-ca-cert");
    mimeMap.put("class", "application/java");
    mimeMap.put("cpio", "application/x-cpio");
    mimeMap.put("csh", "application/x-csh");
    mimeMap.put("css", "text/css");
    mimeMap.put("csv", "text/csv");
    mimeMap.put("dib", "image/bmp");
    mimeMap.put("doc", "application/msword");
    mimeMap.put("dtd", "text/plain");
    mimeMap.put("dv", "video/x-dv");
    mimeMap.put("dvi", "application/x-dvi");
    mimeMap.put("eps", "application/postscript");
    mimeMap.put("etx", "text/x-setext");
    mimeMap.put("exe", "application/octet-stream");
    mimeMap.put("flv", "video/x-flv");
    mimeMap.put("gif", "image/gif");
    mimeMap.put("gtar", "application/x-gtar");
    mimeMap.put("gz", "application/x-gzip");
    mimeMap.put("hdf", "application/x-hdf");
    mimeMap.put("hqx", "application/mac-binhex40");
    mimeMap.put("htc", "text/x-component");
    mimeMap.put("htm", "text/html");
    mimeMap.put("html", "text/html");
    mimeMap.put("hqx", "application/mac-binhex40");
    mimeMap.put("ief", "image/ief");
    mimeMap.put("jad", "text/vnd.sun.j2me.app-descriptor");
    mimeMap.put("jar", "application/java-archive");
    mimeMap.put("java", "text/plain");
    mimeMap.put("jnlp", "application/x-java-jnlp-file");
    mimeMap.put("jpe", "image/jpeg");
    mimeMap.put("jpeg", "image/jpeg");
    mimeMap.put("jpg", "image/jpeg");
    mimeMap.put("js", "text/javascript");
    mimeMap.put("jsf", "text/plain");
    mimeMap.put("jspf", "text/plain");
    mimeMap.put("kar", "audio/x-midi");
    mimeMap.put("latex", "application/x-latex");
    mimeMap.put("m3u", "audio/x-mpegurl");
    mimeMap.put("mac", "image/x-macpaint");
    mimeMap.put("man", "application/x-troff-man");
    mimeMap.put("me", "application/x-troff-me");
    mimeMap.put("mid", "audio/x-midi");
    mimeMap.put("midi", "audio/x-midi");
    mimeMap.put("mif", "application/x-mif");
    mimeMap.put("mov", "video/quicktime");
    mimeMap.put("movie", "video/x-sgi-movie");
    mimeMap.put("mp1", "audio/x-mpeg");
    mimeMap.put("mp2", "audio/x-mpeg");
    mimeMap.put("mp3", "audio/x-mpeg");
    mimeMap.put("mp4", "video/mp4");
    mimeMap.put("mpa", "audio/x-mpeg");
    mimeMap.put("mpe", "video/mpeg");
    mimeMap.put("mpeg", "video/mpeg");
    mimeMap.put("mpega", "audio/x-mpeg");
    mimeMap.put("mpg", "video/mpeg");
    mimeMap.put("mpv2", "video/mpeg2");
    mimeMap.put("ms", "application/x-wais-source");
    mimeMap.put("nc", "application/x-netcdf");
    mimeMap.put("oda", "application/oda");
    mimeMap.put("ogv", "video/ogv");
    mimeMap.put("pbm", "image/x-portable-bitmap");
    mimeMap.put("pct", "image/pict");
    mimeMap.put("pdf", "application/pdf");
    mimeMap.put("pgm", "image/x-portable-graymap");
    mimeMap.put("pic", "image/pict");
    mimeMap.put("pict", "image/pict");
    mimeMap.put("pls", "audio/x-scpls");
    mimeMap.put("png", "image/png");
    mimeMap.put("pnm", "image/x-portable-anymap");
    mimeMap.put("pnt", "image/x-macpaint");
    mimeMap.put("ppm", "image/x-portable-pixmap");
    mimeMap.put("ps", "application/postscript");
    mimeMap.put("psd", "image/x-photoshop");
    mimeMap.put("qt", "video/quicktime");
    mimeMap.put("qti", "image/x-quicktime");
    mimeMap.put("qtif", "image/x-quicktime");
    mimeMap.put("ras", "image/x-cmu-raster");
    mimeMap.put("rgb", "image/x-rgb");
    mimeMap.put("rm", "application/vnd.rn-realmedia");
    mimeMap.put("roff", "application/x-troff");
    mimeMap.put("rtf", "application/rtf");
    mimeMap.put("rtx", "text/richtext");
    mimeMap.put("sh", "application/x-sh");
    mimeMap.put("shar", "application/x-shar");
    mimeMap.put("sit", "application/x-stuffit");
    mimeMap.put("smf", "audio/x-midi");
    mimeMap.put("snd", "audio/basic");
    mimeMap.put("src", "application/x-wais-source");
    mimeMap.put("sv4cpio", "application/x-sv4cpio");
    mimeMap.put("sv4crc", "application/x-sv4crc");
    mimeMap.put("swf", "application/x-shockwave-flash");
    mimeMap.put("t", "application/x-troff");
    mimeMap.put("tar", "application/x-tar");
    mimeMap.put("tcl", "application/x-tcl");
    mimeMap.put("tex", "application/x-tex");
    mimeMap.put("texi", "application/x-texinfo");
    mimeMap.put("texinfo", "application/x-texinfo");
    mimeMap.put("tif", "image/tiff");
    mimeMap.put("tiff", "image/tiff");
    mimeMap.put("tr", "application/x-troff");
    mimeMap.put("tsv", "text/tab-separated-values");
    mimeMap.put("txt", "text/plain");
    mimeMap.put("ulw", "audio/basic");
    mimeMap.put("ustar", "application/x-ustar");
    mimeMap.put("xbm", "image/x-xbitmap");
    mimeMap.put("xht", "application/xhtml+xml");
    mimeMap.put("xhtml", "application/xhtml+xml");
    mimeMap.put("xml", "text/xml");
    mimeMap.put("xpm", "image/x-xpixmap");
    mimeMap.put("xsl", "text/xml");
    mimeMap.put("xwd", "image/x-xwindowdump");
    mimeMap.put("wav", "audio/x-wav");
    mimeMap.put("svg", "image/svg+xml");
    mimeMap.put("svgz", "image/svg+xml");
    mimeMap.put("wbmp", "image/vnd.wap.wbmp");
    mimeMap.put("webm", "video/webm");
    mimeMap.put("wml", "text/vnd.wap.wml");
    mimeMap.put("wmlc", "application/vnd.wap.wmlc");
    mimeMap.put("wmls", "text/vnd.wap.wmlscript");
    mimeMap.put("wmlscriptc", "application/vnd.wap.wmlscriptc");
    mimeMap.put("wrl", "x-world/x-vrml");
    mimeMap.put("Z", "application/x-compress");
    mimeMap.put("z", "application/x-compress");
    mimeMap.put("zip", "application/zip");

  }

  public static String getMime(String ext) {
    return mimeMap.getProperty(ext);
  }
}