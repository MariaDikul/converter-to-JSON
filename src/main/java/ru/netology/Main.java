package ru.netology;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
//        String fileName = "data.csv";
//        List<Employee> list = parseCSV(columnMapping, fileName);
//        String json = listToJson(list);
//        writeString(json);

        List<Employee> list2 = parseXML("data.xml");
        String json2 = listToJson(list2);
        writeString(json2);
    }


    public static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader).withMappingStrategy(strategy).build();
            return csv.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String listToJson(List list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List>() {
        }.getType();
        String json = gson.toJson(list, listType);
        return json;
    }

    public static void writeString(String json) {
        try (FileWriter file = new
                FileWriter("data2.json")) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Employee> parseXML(String fileName) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(fileName));
            Node root = doc.getDocumentElement();
            NodeList nodeList = root.getChildNodes();
            List<Employee> staff = new ArrayList<>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node_ = nodeList.item(i);
                if (Node.ELEMENT_NODE == node_.getNodeType()) {
                    Element element = (Element) node_;
                    NodeList nlID = element.getElementsByTagName("id");
                    for (int j = 0; j < nlID.getLength(); j++) {
                        staff.add(new Employee(Long.parseLong(nlID.item(j).getTextContent()),
                                element.getElementsByTagName("firstName").item(j).getTextContent(),
                                element.getElementsByTagName("lastName").item(j).getTextContent(),
                                element.getElementsByTagName("country").item(j).getTextContent(),
                                Integer.parseInt(element.getElementsByTagName("age").item(j).getTextContent())));
                    }
                }
            }
            return staff;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.xml.sax.SAXException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}