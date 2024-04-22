package org.example.util.ASAP.NRP.Core;

public class TimeSpan {
  public double TotalDays;
  
  public double TotalHours;
  
  public double TotalMinutes;
  
  public double TotalSeconds;
  
  public TimeSpan(long millisecs) {
    double ms = millisecs;
    this.TotalDays = ms / 8.64E7D;
    this.TotalHours = ms / 3600000.0D;
    this.TotalMinutes = ms / 60000.0D;
    this.TotalSeconds = ms / 1000.0D;
    this.TotalDays = roundDouble(this.TotalDays, 1);
  }
  
  public static final double roundDouble(double d, int places) {
    return Math.round(d * Math.pow(10.0D, places)) / Math.pow(10.0D, 
        places);
  }
}
