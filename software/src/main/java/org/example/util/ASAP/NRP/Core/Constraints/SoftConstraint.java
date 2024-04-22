package org.example.util.ASAP.NRP.Core.Constraints;

import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;

public interface SoftConstraint {
  int Calculate(Employee paramEmployee);
  
  int Calculate(Employee paramEmployee, int paramInt1, int paramInt2);
  
  int Calculate(Employee paramEmployee, int paramInt1, int paramInt2, boolean paramBoolean);
  
  String GetDescription(Employee paramEmployee);
  
  void Delete(EmployeeDescription paramEmployeeDescription);
  
  String ToXml(EmployeeDescription paramEmployeeDescription);
  
  String getTitle();
  
  int getWeight();
}
