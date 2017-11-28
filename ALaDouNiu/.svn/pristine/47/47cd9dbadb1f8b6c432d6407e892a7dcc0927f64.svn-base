package com.ems358.sdk.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import android.annotation.SuppressLint;

public class DateUtil {

	Calendar cald;

	public String mDateStringFormat = "yyyy-MM-dd";
	public String mTimeStringFormat = "HH:mm:ss";
	public String mDateTimeStringFormat = "yyyy-MM-dd HH:mm:ss";
	public final static long ONE_DAY_MILLISECONDS = 86400000;

	/**
	 * �Ե�ǰʱ�䣬����һ��DateUtil
	 */
	public DateUtil() {
		cald = GregorianCalendar.getInstance();
	}

	/**
	 * �Ը�����long�͵�ʱ���������һ��DateUtil�����ʱ����Ǻͱ���time zone �޹ص�
	 * 
	 * @param time
	 */
	public DateUtil(long time) {
		cald = GregorianCalendar.getInstance();
		setTime(time);
	}

	/**
	 * ���ַ���ʱ��������һ��DateUtil
	 * 
	 * @param dt
	 * @throws ParseException
	 */
	public DateUtil(String dt) throws ParseException {
		cald = GregorianCalendar.getInstance();
		setTime(dt);
	}

	/**
	 * �ø�����ʱ�䣬����һ��dateUtil��
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @param hour
	 * @param minute
	 * @param second
	 */
	public DateUtil(int year, int month, int day, int hour, int minute, int second) {
		cald = GregorianCalendar.getInstance();
		setTime(year, month, day, hour, minute, second);
	}

	/**
	 * ��һ��ʱ������趨��ǰ��DateUtil�������ʱ����Ǻͱ���time zone �޹ص�
	 * 
	 * @param time
	 */
	public void setTime(long time) {
		Date datetime = new Date(time);
		cald.setTime(datetime);
	}

	/**
	 * ��һ���ַ���ʱ�����趨DateUtil��ʱ�䣬���ַ����������һ���ĸ�ʽ��ȱʡ�� ��ʽ��yyyy-MM-dd
	 * hh:mm:ss�� �����ʽ��һ��������ͨ������ setDateTimeStringFormat �趨
	 * 
	 * @param dt
	 * @throws ParseException
	 */
	@SuppressLint("SimpleDateFormat")
	public void setTime(String dt) throws ParseException {
		SimpleDateFormat mDateFormat = new SimpleDateFormat(mDateTimeStringFormat);
		Date datetime = mDateFormat.parse(dt);
		cald.setTime(datetime);
	}

	/**
	 * �����ꡢ�¡��ա�ʱ���֡��룬ע����Щ��local time zone ʱ��
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @param hour
	 * @param minute
	 * @param second
	 */
	public void setTime(int year, int month, int day, int hour, int minute, int second) {
		cald.set(year, month, day, hour, minute, second);
	}

	/**
	 * �����ꡢ�¡���
	 * 
	 * @param year
	 * @param month
	 * @param day
	 */
	public void setDateTime(int year, int month, int day) {
		cald.set(year, month, day);
	}

	/**
	 * ����ʱ���֡���
	 * 
	 * @param hour
	 * @param minute
	 * @param second
	 */
	public void setTimeTime(int hour, int minute, int second) {
		cald.set(Calendar.HOUR_OF_DAY, hour);
		cald.set(Calendar.MINUTE, minute);
		cald.set(Calendar.SECOND, second);
	}

	/**
	 * ����ʱ����
	 * 
	 * @param hour
	 * @param minute
	 * @param second
	 */
	public void setShortTimeTime(int hour, int minute) {
		cald.set(Calendar.HOUR, hour);
		cald.set(Calendar.MINUTE, minute);
	}

	@SuppressLint("SimpleDateFormat")
	public String getDateString() {
		SimpleDateFormat mDateFormat = new SimpleDateFormat(mDateStringFormat);
		return mDateFormat.format(cald.getTime());
	}

	@SuppressLint("SimpleDateFormat")
	public String getTimeString() {
		SimpleDateFormat mDateFormat = new SimpleDateFormat(mTimeStringFormat);
		return mDateFormat.format(cald.getTime());
	}

	@SuppressLint("SimpleDateFormat")
	public String getDateTimeString() {
		SimpleDateFormat mDateFormat = new SimpleDateFormat(mDateTimeStringFormat);
		return mDateFormat.format(cald.getTime());
	}

	/**
	 * ����unixʱ���������Ǻ�time zone û�й�ϵ��
	 * 
	 * @return
	 */
	public long getMillsecond() {
		return cald.getTime().getTime();
	}

	public int getYear() {
		return cald.get(Calendar.YEAR);
	}

	public int getMonth() {
		return cald.get(Calendar.MONTH);
	}

	public int getDay() {
		return cald.get(Calendar.DAY_OF_MONTH);
	}

	public int getWeek() {
		return cald.get(Calendar.DAY_OF_WEEK);
	}

	public int getHour() {
		return cald.get(Calendar.HOUR_OF_DAY);
	}

	public int getMinute() {
		return cald.get(Calendar.MINUTE);
	}

	public int getSecond() {
		return cald.get(Calendar.SECOND);
	}

	public void setDateStringFormat(String dsf) {
		mDateStringFormat = dsf;
	}

	public void setTimeStringFormat(String tsf) {
		mTimeStringFormat = tsf;
	}

	public void setDateTimeStringFormat(String tsf) {
		mDateTimeStringFormat = tsf;
	}

	public long getDayStartTick() {
		setTimeTime(0, 0, 0);
		return ((long) (getMillsecond() / 1000.0)) * 1000;// ȥ��С��һ����ɵĲ�ͬ
	}

	public long getDayEndTick() {
		return getDayStartTick() + ONE_DAY_MILLISECONDS;
	}

	public float getLiveDay(long birthday) {
		long curr = System.currentTimeMillis();
		float days = (curr - birthday) / (24 * 60 * 60 * 1000);
		return days;
	}

	public String getGmtTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		String timeStr = sdf.format(cald.getTime());
		return timeStr;
	}

	public static long GmtToDate(String gmt) {
		SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
		try {
			Date date = format.parse(gmt);
			return date.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}

	}

}