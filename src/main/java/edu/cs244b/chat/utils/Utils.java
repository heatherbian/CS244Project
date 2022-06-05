package edu.cs244b.chat.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils
{

	public static String getDate() {
		return new SimpleDateFormat("MM-dd HH:mm:ss").format(new Date());
	}

	public static String getDate(long timeStamp) {
		return new SimpleDateFormat("HH:mm:ss").format(new Date(timeStamp));
	}

	public static String getDate(Timestamp timeStamp) {
		return new SimpleDateFormat("HH:mm:ss").format(new Date(timeStamp.getTime()));
	}
}
