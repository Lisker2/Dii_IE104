package org.example.util.ASAP.NRP.Core;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTime {
  public Calendar cal;
  
  public DateTime() {
    this.cal = Calendar.getInstance();
  }
  
  public DateTime(int year, int month, int day, int hour, int min, int sec) {
    this.cal = Calendar.getInstance();
    this.cal.set(year, month, day, hour, min, sec);
    this.cal.set(14, 0);
  }
  
  public static DateTime getNow() {
    return new DateTime();
  }
  
  public int getHour() {
    return this.cal.get(11);
  }
  
  public int getMinute() {
    return this.cal.get(12);
  }
  
  public int getSecond() {
    return this.cal.get(13);
  }
  
  public int getMonth() {
    return this.cal.get(2);
  }
  
  public int getDay() {
    return this.cal.get(5);
  }
  
  public int getYear() {
    return this.cal.get(1);
  }
  
  public int getDayOfWeek() {
    return this.cal.get(7);
  }
  
  public String getDayOfWeekString() {
    int d = this.cal.get(7);
    switch (d) {
      case 2:
        return "Monday";
      case 3:
        return "Tuesday";
      case 4:
        return "Wednesday";
      case 5:
        return "Thursday";
      case 6:
        return "Friday";
      case 7:
        return "Saturday";
      case 1:
        return "Sunday";
    } 
    return "";
  }
  
  public long getTicks() {
    return this.cal.getTimeInMillis();
  }
  
  public String ToLongDateString() {
    DateFormat dateFormatter = DateFormat.getDateInstance(1);
    return dateFormatter.format(this.cal.getTime());
  }
  
  public String ToShortDateString() {
    DateFormat dateFormatter = DateFormat.getDateInstance(3);
    return dateFormatter.format(this.cal.getTime());
  }
  
  public String ToShortTimeString() {
    DateFormat dateFormatter = DateFormat.getTimeInstance(3);
    return dateFormatter.format(this.cal.getTime());
  }
  
  public String ToString(String format) {
    SimpleDateFormat formatter = new SimpleDateFormat(format);
    return formatter.format(this.cal.getTime());
  }
  
  public String ToString(String format, Locale l) {
    SimpleDateFormat formatter = new SimpleDateFormat(format, l);
    return formatter.format(this.cal.getTime());
  }
  
  public DateTime AddDays(int days) {
    DateTime dt = new DateTime();
    dt.cal.setTime(this.cal.getTime());
    dt.cal.add(5, days);
    return dt;
  }
  
  public DateTime AddHours(int hours) {
    DateTime dt = new DateTime();
    dt.cal.setTime(this.cal.getTime());
    dt.cal.add(11, hours);
    return dt;
  }
  
  public DateTime AddHours(double hours) {
    int secs = (int)(hours * 60.0D * 60.0D);
    DateTime dt = new DateTime();
    dt.cal.setTime(this.cal.getTime());
    dt.cal.add(13, secs);
    return dt;
  }
  
  public DateTime AddMinutes(double mins) {
    int secs = (int)(mins * 60.0D);
    DateTime dt = new DateTime();
    dt.cal.setTime(this.cal.getTime());
    dt.cal.add(13, secs);
    return dt;
  }
  
  public DateTime AddSeconds(double secs) {
    int ms = (int)(secs * 1000.0D);
    DateTime dt = new DateTime();
    dt.cal.setTime(this.cal.getTime());
    dt.cal.add(14, ms);
    return dt;
  }
  
  public DateTime AddMilliseconds(int ms) {
    DateTime dt = new DateTime();
    dt.cal.setTime(this.cal.getTime());
    dt.cal.add(14, ms);
    return dt;
  }
  
  public static DateTime ParseDate(String str) {
    DateTime dt = new DateTime();
    try {
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
      Date d = formatter.parse(str);
      dt.cal.setTime(d);
      dt.cal.set(14, 0);
    } catch (Exception ex) {
      System.out.println("Error parsing date: " + ex.getLocalizedMessage());
    } 
    return dt;
  }
  
  public static DateTime ParseTime(String str) {
    DateTime dt = new DateTime();
    try {
      SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
      Date d = formatter.parse(str);
      dt.cal.setTime(d);
    } catch (Exception ex) {
      System.out.println("Error parsing time: " + ex.getLocalizedMessage());
    } 
    return dt;
  }
  
  public boolean isLessThan(DateTime d) {
    return (this.cal.getTimeInMillis() < d.cal.getTimeInMillis());
  }
  
  public boolean isLessThanOrEqual(DateTime d) {
    return (this.cal.getTimeInMillis() <= d.cal.getTimeInMillis());
  }
  
  public boolean isGreaterThan(DateTime d) {
    return (this.cal.getTimeInMillis() > d.cal.getTimeInMillis());
  }
  
  public boolean isGreaterThanOrEqual(DateTime d) {
    return (this.cal.getTimeInMillis() >= d.cal.getTimeInMillis());
  }
  
  public static long compare(DateTime d1, DateTime d2) {
    return d1.cal.getTimeInMillis() - d2.cal.getTimeInMillis();
  }
  
  public TimeSpan subtract(DateTime d) {
    return new TimeSpan(this.cal.getTimeInMillis() - d.cal.getTimeInMillis());
  }
}
