import java.util.Map;
import java.util.Scanner;
import java.util.*;
import java.io.*;
import java.io.File;
import java.net.*;  
import java.util.stream.Collectors;
import java.util.TreeMap;  
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class App extends Application
{
    static Map<String,Integer> words=new HashMap<String, Integer>();
    static Integer threshold = 30; 
    static Integer percentTotal = 0;
 static void CountWords(Map< String, Integer> words) throws Exception
{try{
    
    
    URL url = new URL("https://www.gutenberg.org/files/2264/2264.txt");
   Scanner file = new Scanner(url.openStream());
while(file.hasNext()){
String word=file.next().replaceAll("[,*.~\"\';?%:)(0123456789]", "");
word=word.replaceAll("\\[", "").replaceAll("\\]","").toLowerCase();
if (word=="")
continue;
Integer count=words.get(word);
if(count!=null)
count++;
else
count=1;
words.put(word,count);
}
file.close();

        LinkedHashMap<String, Integer> temp = new LinkedHashMap<>();
        App.words.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> temp.put(x.getKey(), x.getValue()));
        App.words=temp;
        System.out.println(App.words);
        System.out.println("Data gathered:-");
        System.out.println("Total unique words present are:- "+App.words.size());
        Integer totalWords =0 ;
        Map<String,Double> wordsPercent=new HashMap<String, Double>();
        Iterator<Map.Entry< String, Integer> >iterator = App.words.entrySet().iterator();
         while (iterator.hasNext()) {
            Map.Entry< String, Integer> entry = iterator.next();
            totalWords += entry.getValue();}
        System.out.println("Total words present are:- "+totalWords);
        iterator = App.words.entrySet().iterator();
        System.out.println("The percentage graph is based on this data:- ");
        while (iterator.hasNext()) {
            Map.Entry< String, Integer> entry = iterator.next();
            if(entry.getValue()>100){
                System.out.println("Word:- "+entry.getKey()+", Count:-"+entry.getValue()+", Percentaage:-"+(entry.getValue()/Double.valueOf(totalWords))*100);
            wordsPercent.put(entry.getKey(), (entry.getValue()/Double.valueOf(totalWords))*100);}
        }
            System.out.println("wordsPercent");
            System.out.println(wordsPercent);


}
catch(Exception ex){
    System.out.println("nikhil"+ex);
}
}
public void start(Stage PrimaryStage){
    Pane root=new Pane();
    Iterator<Map.Entry< String, Integer> >
iterator = App.words.entrySet().iterator();
ArrayList<PieChart.Data> tempList = new ArrayList<PieChart.Data>();
Integer count=0;

while (iterator.hasNext() && count<App.threshold) {
    Map.Entry< String, Integer> entry = iterator.next();
tempList.add(new PieChart.Data(entry.getKey(), entry.getValue()));
App.percentTotal += entry.getValue();
count+=1;
}

ObservableList<PieChart.Data> valueList = FXCollections.observableArrayList(tempList);
    PieChart pieChart = new PieChart(valueList);
    pieChart.setTitle("Word Count Percent Piechart");
    pieChart.setLabelsVisible(true); 
    pieChart.getData().forEach(data->{
        String percent = String.format("% .2f%%",((data.getPieValue()/App.percentTotal)*100));
        Tooltip tooltip = new Tooltip(percent);
        Tooltip.install(data.getNode(), tooltip);
    });

    root.getChildren().addAll(pieChart);
    Scene scene = new Scene (root, 500, 500);
    try
      {
         scene.getStylesheets().add("chart.css");
      }
      catch (Exception ex)
      {
         System.err.println("Cannot acquire stylesheet: " + ex.toString());
      }
    PrimaryStage.setTitle("Word Count");
    PrimaryStage.setScene(scene);
    PrimaryStage.show();

}

public static void main(String[] args) throws Exception
{try{
CountWords(App.words);
launch(args);
}
catch(Exception ex){
System.out.println("ex :-"+ex);    
}
}
}