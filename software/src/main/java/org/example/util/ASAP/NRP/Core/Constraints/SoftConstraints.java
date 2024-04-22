package org.example.util.ASAP.NRP.Core.Constraints;

import org.example.util.ASAP.NRP.Core.Employee;

public class SoftConstraints {
  public static boolean UpdateViolationDescriptions = false;
  
  public static int CalculatePenalty(Employee employee, boolean updateViolationDescriptions) {
    boolean temp = UpdateViolationDescriptions;
    UpdateViolationDescriptions = updateViolationDescriptions;
    if (updateViolationDescriptions)
      for (int i = 0; i < employee.ViolationDescriptions.length; i++)
        employee.ViolationDescriptions[i] = "";  
    int penalty = CalculatePenalty(employee);
    UpdateViolationDescriptions = temp;
    return penalty;
  }
  
  public static int CalculatePenalty(Employee employee) {
    int totalPenalty = 0;
    int i;
    for (i = 0; i < employee.ConstraintViolationPenalties.length; i++)
      employee.ConstraintViolationPenalties[i] = 0; 
    for (i = 0; i < employee.EmployeeDescription.AllSoftConstraints.length; i++)
      totalPenalty += employee.EmployeeDescription.AllSoftConstraints[i].Calculate(employee); 
    return totalPenalty;
  }
  
  public static int CalculatePenalty(Employee employee, int startDay, int endDay) {
    int totalPenalty = 0;
    int i;
    for (i = 0; i < employee.ConstraintViolationPenalties.length; i++)
      employee.ConstraintViolationPenalties[i] = 0; 
    for (i = 0; i < employee.EmployeeDescription.AllSoftConstraints.length; i++)
      totalPenalty += employee.EmployeeDescription.AllSoftConstraints[i].Calculate(employee, startDay, endDay); 
    return totalPenalty;
  }
  
  public static int CalculatePenalty(Employee employee, int startDay, int endDay, boolean updateStructure) {
    int totalPenalty = 0;
    int i;
    for (i = 0; i < employee.ConstraintViolationPenalties.length; i++)
      employee.ConstraintViolationPenalties[i] = 0; 
    for (i = 0; i < employee.EmployeeDescription.AllSoftConstraints.length; i++)
      totalPenalty += employee.EmployeeDescription.AllSoftConstraints[i].Calculate(employee, startDay, endDay, updateStructure); 
    return totalPenalty;
  }
  
  public static int CalculatePenalty(Employee employee, int maxPenalty) {
    int totalPenalty = 0;
    int i;
    for (i = 0; i < employee.ConstraintViolationPenalties.length; i++)
      employee.ConstraintViolationPenalties[i] = 0; 
    for (i = 0; i < employee.EmployeeDescription.AllSoftConstraints.length; i++) {
      totalPenalty += employee.EmployeeDescription.AllSoftConstraints[i].Calculate(employee);
      if (totalPenalty >= maxPenalty)
        break; 
    } 
    return totalPenalty;
  }
  
  public static int CalculatePenalty(Employee employee, int maxPenalty, int startDay, int endDay, boolean updateStructure) {
    int totalPenalty = 0;
    int i;
    for (i = 0; i < employee.ConstraintViolationPenalties.length; i++)
      employee.ConstraintViolationPenalties[i] = 0; 
    for (i = 0; i < employee.EmployeeDescription.AllSoftConstraints.length; i++) {
      totalPenalty += employee.EmployeeDescription.AllSoftConstraints[i].Calculate(employee, startDay, endDay, updateStructure);
      if (totalPenalty >= maxPenalty)
        break; 
    } 
    return totalPenalty;
  }
}
