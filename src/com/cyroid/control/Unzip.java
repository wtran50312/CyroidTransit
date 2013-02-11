package com.cyroid.control;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.os.Environment;
import android.util.Log;

/**
 * A utility to unzip a zip file from HTTP (unzipFileHTTP) or from localhost
 * (openZipFile).
 * 
 * @author Matt Graves
 */
public class Unzip {

	/**
	 * The zip file to unzip (must be downloaded first, if from HTTP).
	 */
	private File zipFile = null;

	/**
	 * The files that are unzipped from the zip file.
	 */
	private ArrayList<File> files = null;

	/**
	 * Stores the directory path of the current GTFS source
	 */
	private String path;

	/**
	 * Creates a new Unzip object, with an empty file collection.
	 */
	public Unzip() {
		files = new ArrayList<File>();
	}

	/**
	 * A method to download a zip file from an HTTP host (via the Internet),
	 * then unzip it and return the files within it.
	 * 
	 * @param zipFileURL
	 *            the location (url) of the zip file
	 * @return an ArrayList of the unzipped files
	 */
	public ArrayList<File> unzipFileHTTP(String zipFileURL, final Activity act)  {
		try {
			
		
			File gtfsFolder = new File("data/data/com.cyroid/gtfs");
			
			if (!gtfsFolder.exists()) {
				gtfsFolder.mkdir();
			}

			BufferedInputStream in = null;
			//try {
				in = new BufferedInputStream(new URL(zipFileURL).openStream(),
						1024);
		

			path = "data/data/com.cyroid/gtfs/";

			zipFile = new File(path + "gtfsdata.zip");
			FileOutputStream fos = new FileOutputStream(zipFile);
			BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);

			byte[] data = new byte[1024];
			int x = 0;

			while ((x = in.read(data, 0, 1024)) >= 0) {
				bout.write(data, 0, x);
			}
			bout.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return openZipFile(zipFile.toURI());
	}

	/**
	 * Allows for full reading of files (our zip file), instead of cutting off
	 * on byte addresses (if we didn't have this method, our zip file would not
	 * be fully downloaded).
	 * 
	 * @param in
	 *            the input stream to copy
	 * @param out
	 *            the output stream to copy to
	 * @throws IOException
	 *             if either stream cannot be read from / written to
	 */
	public void copyInputStream(InputStream in, OutputStream out)
			throws IOException {
		byte[] buffer = new byte[1024];
		int len;

		while ((len = in.read(buffer)) >= 0)
			out.write(buffer, 0, len);

		in.close();
		out.close();
	}

	/**
	 * A method to open a zip file from the localhost file system.
	 * 
	 * @param zipFileLocation
	 *            the file:///"path" URI of the zip file
	 * @return an ArrayList of the unzipped files from the zip file
	 */
	public ArrayList<File> openZipFile(URI zipFileLocation) {
		try {
			ZipFile zipFile = new ZipFile(new File(zipFileLocation));
			Enumeration<? extends ZipEntry> entries = zipFile.entries();

			ZipEntry entry = null;
			File toAdd = null;
			while (entries.hasMoreElements()) {
				entry = (ZipEntry) entries.nextElement();
				if (entry.isDirectory()) {
					// Assume directories are stored parents first then
					// children.
					// This is not robust, just for demonstration purposes.
					(new File(entry.getName())).mkdirs();
					continue;
				}
				toAdd = new File(path + entry.getName());
				copyInputStream(zipFile.getInputStream(entry),
						new BufferedOutputStream(new FileOutputStream(toAdd),
								1024));
				files.add(toAdd);
			}

			zipFile.close();
		} catch (IOException ioe) {
			System.err.println("Unhandled exception:");
			ioe.printStackTrace();
			return null;
		}
		return files;
	}

}