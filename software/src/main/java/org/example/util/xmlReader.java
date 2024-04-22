package org.example.util;
import org.example.obj.Event;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.*;


public class xmlReader {

    public String solution_path = "solution.xml";
    public String instance_path = "BCV-3.46.1.xml";

    public xmlReader(String solution_path, String instance_path) {
        this.solution_path = solution_path;
        this.instance_path = instance_path;
    }


    public xmlReader() {
    }

    public int[] statistics(){
        List<Map<String, Map<String, String>>> dataList = this.getinstance();
        int contract = dataList.get(1).size();
        int employees = dataList.get(2).size();
        int shiftTypes = dataList.get(0).size() - 1;
        int totalShift = 0;
        try {
            File solution = new File(this.solution_path);
            DocumentBuilderFactory solution_factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder solution_builder = solution_factory.newDocumentBuilder();
            Document solution_doc = solution_builder.parse(solution);
            solution_doc.getDocumentElement().normalize();
            NodeList solution_assignmentList = solution_doc.getElementsByTagName("Assignment");
            totalShift = solution_assignmentList.getLength();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new int[]{contract,employees,shiftTypes,totalShift};
    }

    public List<Event> getsolution(){
        List<Map<String, Map<String, String>>> dataList = this.getinstance();
        List<Event> eventList = new ArrayList<>();
        String[] colors = new String[]{
            "#8DB6C7","#C1B38E","#D1C68F","#CA9F92","#F9CD97","#E3D9B0","#B1C27A","#B2E289",
        };
        try {
            File solution = new File(this.solution_path);
            DocumentBuilderFactory solution_factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder solution_builder = solution_factory.newDocumentBuilder();
            Document solution_doc = solution_builder.parse(solution);

            solution_doc.getDocumentElement().normalize();

            NodeList solution_assignmentList = solution_doc.getElementsByTagName("Assignment");
            System.out.println(solution_assignmentList.getLength());
            for (int i = 0; i < solution_assignmentList.getLength(); i++) {
                Node assignmentNode = solution_assignmentList.item(i);
                if (assignmentNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element assignmentElement = (Element) assignmentNode;
                    String date = assignmentElement.getElementsByTagName("Date").item(0).getTextContent();
                    String employeeID = assignmentElement.getElementsByTagName("EmployeeID").item(0).getTextContent();
                    String shiftID = assignmentElement.getElementsByTagName("ShiftID").item(0).getTextContent();
                    eventList.add(new Event(Integer.toString(i),date+"T"+dataList.get(0).get(shiftID).get("StartTime"),
                            date+"T"+dataList.get(0).get(shiftID).get("EndTime"),employeeID,"#0080FF", "primary"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return eventList;
    }

    public List<Map<String, Map<String, String>>> getinstance(){
        try {
            // Load XML file
            File inputFile = new File(this.instance_path);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            // Create maps for storing data
            List<Map<String, Map<String, String>>> dataList = new ArrayList<>();
            Map<String, Map<String, String>> shiftTypes = new HashMap<>();
            Map<String, Map<String, String>> contracts = new HashMap<>();
            Map<String, Map<String, String>> employees = new HashMap<>();

            // Parse Shift Types
            NodeList shiftTypeList = doc.getElementsByTagName("Shift");
            for (int i = 0; i < shiftTypeList.getLength(); i++) {
                Node shiftNode = shiftTypeList.item(i);
                if (shiftNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element shiftElement = (Element) shiftNode;
                    String id = shiftElement.getAttribute("ID");
                    Map<String, String> shiftAttributes = new HashMap<>();
                    NodeList childNodes = shiftElement.getChildNodes();
                    for (int j = 0; j < childNodes.getLength(); j++) {
                        Node childNode = childNodes.item(j);
                        if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                            String attributeName = childNode.getNodeName();
                            String attributeValue = childNode.getTextContent();
                            shiftAttributes.put(attributeName, attributeValue);
                        }
                    }
                    shiftTypes.put(id, shiftAttributes);
                }
            }
            // Parse Contracts
            NodeList contractList = doc.getElementsByTagName("Contract");
            for (int i = 0; i < contractList.getLength(); i++) {
                Node contractNode = contractList.item(i);
                if (contractNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element contractElement = (Element) contractNode;
                    String id = contractElement.getAttribute("ID");
                    Map<String, String> contractAttributes = new HashMap<>();
                    NodeList childNodes = contractElement.getChildNodes();
                    for (int j = 0; j < childNodes.getLength(); j++) {
                        Node childNode = childNodes.item(j);
                        if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                            String attributeName = childNode.getNodeName();
                            String attributeValue = childNode.getTextContent();
                            contractAttributes.put(attributeName, attributeValue);
                        }
                    }
                    contracts.put(id, contractAttributes);
                }
            }

            // Parse Employees
            NodeList employeeList = doc.getElementsByTagName("Employee");
            for (int i = 0; i < employeeList.getLength(); i++) {
                Node employeeNode = employeeList.item(i);
                if (employeeNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element employeeElement = (Element) employeeNode;
                    String id = employeeElement.getAttribute("ID");
                    Map<String, String> employeeAttributes = new HashMap<>();
                    NodeList childNodes = employeeElement.getChildNodes();
                    for (int j = 0; j < childNodes.getLength(); j++) {
                        Node childNode = childNodes.item(j);
                        if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                            String attributeName = childNode.getNodeName();
                            String attributeValue = childNode.getTextContent();
                            employeeAttributes.put(attributeName, attributeValue);
                        }
                    }
                    employees.put(id, employeeAttributes);
                }
            }

            dataList.add(shiftTypes);
            dataList.add(contracts);
            dataList.add(employees);


            // Print the maps
            //System.out.println("Shift Types:");
            //System.out.println(shiftTypes.size() - 1);
            //printMap(shiftTypes);

            //System.out.println("\nContracts:");
            //System.out.println(contracts.size());
            //printMap(contracts);

            //System.out.println("\nEmployees:");
            //System.out.println(employees.size());
            //printMap(employees);


            return dataList;

        } catch (Exception e) {
            List<Map<String, Map<String, String>>> dataList = new ArrayList<>();
            e.printStackTrace();
            return dataList;
        }
    }

    private static void printMap(Map<String, Map<String, String>> map) {
        for (String id : map.keySet()) {
            Map<String, String> attributes = map.get(id);
            System.out.println("ID: " + id);
            for (String attributeName : attributes.keySet()) {
                String attributeValue = attributes.get(attributeName);
                System.out.println(attributeName + ": " + attributeValue);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        xmlReader r = new xmlReader();
        r.getinstance();
    }

}
