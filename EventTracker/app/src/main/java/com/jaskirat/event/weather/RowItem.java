package com.jaskirat.event.weather;

public class RowItem {

	int temp, temp_max, temp_min;
	String mdate, description, icon;

	public RowItem(int temp, int temp_max, int temp_min, String description,
			String icon, String mdate) {

		this.temp = temp;
		this.temp_max = temp_max;
		this.temp_min = temp_min;
		this.description = description;
		this.icon = icon;
		this.mdate = mdate;
	}

	public String getMdate() {
		return mdate;
	}

	public void setMdate(String mdate) {
		this.mdate = mdate;
	}

	public int getTemp() {
		return temp;
	}

	public void setTemp(int temp) {
		this.temp = temp;
	}

	public int getTemp_max() {
		return temp_max;
	}

	public void setTemp_max(int temp_max) {
		this.temp_max = temp_max;
	}

	public int getTemp_min() {
		return temp_min;
	}

	public void setTemp_min(int temp_min) {
		this.temp_min = temp_min;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

}
