package org.example.util.ASAP.NRP.Core;

public class DayPeriod implements Comparable<Object> {
  public int Index;
  
  public Period Period;
  
  public DayPeriod(int index, Period period) {
    this.Index = -1;
    this.Index = index;
    this.Period = period;
  }
  
  public int compareTo(Object obj) {
    long diff = DateTime.compare(getStart(), ((DayPeriod)obj).getStart());
    if (diff > 0L)
      return 1; 
    if (diff < 0L)
      return -1; 
    return 0;
  }
  
  public String getDurationLabel() {
    return this.Period.DurationLabel;
  }
  
  public DateTime getStart() {
    return this.Period.getStartTime();
  }
  
  public DateTime getEnd() {
    return this.Period.getEndTime();
  }
}
