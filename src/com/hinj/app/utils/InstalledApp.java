/**
 * Copyright OCTAL INFO SOLUTIONS PVT LTD.  11:37:25 AM  Dec 13, 2013
 */
package com.hinj.app.utils;

/**
 * @author jitendrav
 *
 */
public class InstalledApp {
	
	private String applicationName;
	/**
	 * @return the applicationName
	 */
	public String getApplicationName() {
		return applicationName;
	}
	/**
	 * @param applicationName the applicationName to set
	 */
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	/**
	 * @return the applicationPackage
	 */
	public String getApplicationPackage() {
		return applicationPackage;
	}
	/**
	 * @param applicationPackage the applicationPackage to set
	 */
	public void setApplicationPackage(String applicationPackage) {
		this.applicationPackage = applicationPackage;
	}
	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	/**
	 * @return the installedDate
	 */
	public long getInstalledDate() {
		return installedDate;
	}
	/**
	 * @param installedDate the installedDate to set
	 */
	public void setInstalledDate(long installedDate) {
		this.installedDate = installedDate;
	}
	private String applicationPackage;
	private String version;
	private long installedDate;

	/**
	 * 
	 */
	public InstalledApp(String applicationName,String applicationPackage,String version,long installedDate) {
		this.applicationName=applicationName;
		this.applicationPackage=applicationPackage;
		this.version=version;
		this.installedDate=installedDate;
				
	}
	
}
