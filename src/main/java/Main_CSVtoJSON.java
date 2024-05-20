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
import org.xml.sax.SAXException;

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

public class Main_CSVtoJSON {

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        //Задание 1. CSV - JSON парсер;
        System.out.println("Приступаем к заданию 1: CSV - JSON парсер");
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        //имя для считываемого CSV файла:
        String fileName = "data.csv";
        //список сотрудников:
        List<Employee> list = parseCSV(columnMapping, fileName);
        String json = listToJson(list);
        //записываем список в JSON файл:
        String filesOut = "data.json";// куда записываем
        writeString(json, filesOut);
        System.out.println("Приступаем к заданию 2: XML - JSON парсер");

        //Задание 2. XML - JSON парсер;
        List<Employee> listXML = parseXML("data.xml");
        String jsonXML = listToJson(list);
        writeString(json, "data2.json");


    }

    private static List<Employee> parseCSV(String[] columnMapping, String fileName) {

        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();
            List<Employee> listRes = csv.parse();

            return listRes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void writeString(String json, String filesOut) {
        //Запись в JSON файл строки
        try (FileWriter file = new FileWriter(filesOut)) {
            file.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        String json = gson.toJson(list, listType);
        System.out.println(json);
        return json;
    }

    private static List<Employee> parseXML(String s) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(s));
        Node root = doc.getDocumentElement();
        NodeList nodeList = root.getChildNodes();
        List<Employee> list = new ArrayList<>();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (Node.ELEMENT_NODE == node.getNodeType()) {
                Element element = (Element) node;// получили элемент
                /*
                    element может ничего не содержать поэтому проверяю, что он не пустой
                */

                if (!element.getAttribute("id").isEmpty()) {

                    list.add(new Employee((Long.parseLong((element.getAttribute("id")))),
                            (element.getAttribute("firstName")),
                            (element.getAttribute("lastName")),
                            (element.getAttribute("country")),
                            (Integer.parseInt(element.getAttribute("age")))));
                }
//
            }
        }
//

        return list;
    }

}
