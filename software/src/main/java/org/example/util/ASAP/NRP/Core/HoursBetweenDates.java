package org.example.util.ASAP.NRP.Core;

public class HoursBetweenDates {
  public int Hours;
  
  public int StartDay;
  
  public int EndDay;
  
  public HoursBetweenDates(int startDay, int endDay, int hours) {
    this.StartDay = startDay;
    this.EndDay = endDay;
    this.Hours = hours;
  }
}
