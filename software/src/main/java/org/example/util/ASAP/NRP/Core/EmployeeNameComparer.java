package org.example.util.ASAP.NRP.Core;

public class EmployeeNameComparer {
  int compare(Object x, Object y) {
    String s1 = ((Employee)x).EmployeeDescription.getName();
    String s2 = ((Employee)y).EmployeeDescription.getName();
    return s1.compareTo(s2);
  }
}
