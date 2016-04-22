package com.brainsoon.solr.util;

import java.io.File;
import java.io.IOException;

public class OSCommandUtil  {	
	
	private static String osName= System.getProperty("os.name").toUpperCase();
	private static String OS_WINDOWS = "WINDOWS";	

	public static void copyFile(String sourceFile, String destDir) throws IOException, InterruptedException {
		StringBuffer sb = new StringBuffer(256);
		if(osName.indexOf(OS_WINDOWS) >= 0){			
			sb.append("cmd.exe /C  copy ").append(sourceFile.replaceAll("/", "\\\\")).append(" ").append(destDir.replaceAll("/", "\\\\"));
		}else{
			sb.append("cp ").append(sourceFile).append(" ").append(destDir);
		}
		System.out.println(sb.toString());
		execSystemCommon(sb.toString());		
	}

	public static void mvFile(String sourceFile, String destDir) throws IOException, InterruptedException {
		StringBuffer sb = new StringBuffer(256);
		if(osName.indexOf(OS_WINDOWS) >= 0){
			sourceFile = "\"" + sourceFile + "\"";
			sb.append("cmd.exe /C  move ").append(sourceFile.replaceAll("/", "\\\\")).append(" ").append(destDir.replaceAll("/", "\\\\"));
		}else{
			sb.append("mv ").append(sourceFile).append(" ").append(destDir);
		}
		System.out.println(sb.toString());
		execSystemCommon(sb.toString());
	}

	public static void hide(String path) throws InterruptedException, IOException {
		String command = "attrib +h " + path;
	    execSystemCommon(command); 
	}
	
	public static void renameFile(String sourceFileName, String destFileName) {
		File sourceFile = new File(sourceFileName);
		File destFile = new File(destFileName);
		sourceFile.renameTo(destFile);
	}
	
	private static void execSystemCommon(String command) throws IOException, InterruptedException {
		try {
			Process proc = Runtime.getRuntime().exec(command);
			proc.waitFor();
		} catch (IOException e) {
			throw e;
		}
		catch(InterruptedException exp)
		{
			throw exp;
		}
	}	
	

	
	public static void main(String[] args) throws InterruptedException, IOException {
		String sourceFile = "C:\\Users\\Administrator\\Brainsoon\\CloudDisk\\pic/Telnet.java";
		String destFile = "C:\\Users\\Administrator\\Brainsoon\\CloudDisk\\pic/Telnet.java";		
	}

}





