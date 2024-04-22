package org.example.util.ASAP.NRP.Core;

public class Period {
  DateTime start;
  
  DateTime end;
  
  public boolean SpansMidnight = false;
  
  public double Duration = 0.0D;
  
  public String DurationLabel = "";
  
  public Period(DateTime start, DateTime end) {
    this.start = start;
    this.end = end;
    TimesChanged();
  }
  
  public DateTime getStartTime() {
    return this.start;
  }
  
  public void setStartTime(DateTime time) {
    this.start = time;
    TimesChanged();
  }
  
  public DateTime getEndTime() {
    return this.end;
  }
  
  public void setEndTime(DateTime time) {
    this.end = time;
    TimesChanged();
  }
  
  private void TimesChanged() {
    this.DurationLabel = String.valueOf(this.start.ToShortTimeString()) + " - " + this.end.ToShortTimeString();
    this.start = new DateTime(2008, 2, 19, this.start.getHour(), this.start.getMinute(), 0);
    this.end = new DateTime(2008, 2, 19, this.end.getHour(), this.end.getMinute(), 0);
    if (this.end.isLessThan(this.start)) {
      this.SpansMidnight = true;
      this.end = this.end.AddDays(1);
    } else {
      this.SpansMidnight = false;
    } 
    TimeSpan span = this.end.subtract(this.start);
    this.Duration = span.TotalHours;
  }
  
  public boolean equals(Object obj) {
    if (obj == null)
      return false; 
    Period p2 = (Period)obj;
    if (p2.start.getHour() == this.start.getHour() && 
      p2.start.getMinute() == this.start.getMinute() && 
      p2.end.getHour() == this.end.getHour() && 
      p2.end.getMinute() == this.end.getMinute())
      return true; 
    return false;
  }
}
