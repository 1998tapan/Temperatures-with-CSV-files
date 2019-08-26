import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.*;
/**
 * Write a description of CSVCold here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class CSVCold {
    
    public double averageTemperatureWithHighHumidityInFile(CSVParser parser, int value){
        double count=0,avgTemp=0;
        for(CSVRecord record:parser){
            int currentHumidity=Integer.parseInt(record.get("Humidity"));
            if(currentHumidity>=value){
                double currentTemp=Double.parseDouble(record.get("TemperatureF"));
                avgTemp+=currentTemp;
                count++;
            }
        }
        avgTemp/=count;
        return avgTemp;
    } 
    
    public void testAvgTempWithHighHumidity(){
        FileResource fr=new FileResource();
        CSVParser parser=fr.getCSVParser();
        double avgTempHighHumid=averageTemperatureWithHighHumidityInFile(parser,80);
        if(avgTempHighHumid>0){
            System.out.println("Average Temperature when high humidity: "+avgTempHighHumid);
        }
        else{
            System.out.println("No temperatures with that humidity");
        }
    }
    
    public double averageTemperatureInFile(CSVParser parser){
        double count=0,avgTemp=0;
        for(CSVRecord record:parser){
            double currentTemp=Double.parseDouble(record.get("TemperatureF"));
            avgTemp+=currentTemp;
            count++;
        }
        avgTemp/=count;
        return avgTemp;
    }
    
    public void testAvgTempInFile(){
       FileResource fr=new FileResource();
       CSVParser parser= fr.getCSVParser();
       System.out.println("Average Temp. in File is: "+averageTemperatureInFile(parser));
    }
    
    public CSVRecord lowestHumidityInFile(CSVParser parser){
        CSVRecord lowestHumiditySoFar=null;
        for(CSVRecord currentRecord:parser){
            if(lowestHumiditySoFar==null){
                lowestHumiditySoFar=currentRecord;
            }
            else{
                Double currentHumidity=Double.parseDouble(currentRecord.get("Humidity"));
                Double coldestHumidity=Double.parseDouble(lowestHumiditySoFar.get("Humidity"));
                if(currentHumidity<coldestHumidity){
                    lowestHumiditySoFar=currentRecord;
                }
            }
        }
        return lowestHumiditySoFar;
    }
    
    public void testLowestHumidityInFile(){
        FileResource fr=new FileResource();
        CSVRecord coldestRecord=lowestHumidityInFile(fr.getCSVParser());
        System.out.println("The temperature recorded was "+coldestRecord.get("TemperatureF")+" at "
        +/*coldestRecord.get("TimeEST")+*/" on "+coldestRecord.get("DateUTC")+". The weather was "+coldestRecord.get("Conditions")
        +" with humidity "+coldestRecord.get("Humidity"));
        
    }
    
    public CSVRecord lowestHumidityInManyFiles(){
        CSVRecord lowestHumidityRecordSoFar=null;
        DirectoryResource dr= new DirectoryResource();
        for(File f:dr.selectedFiles()){
            FileResource fr=new FileResource(f);
            CSVRecord currentRecord=lowestHumidityInFile(fr.getCSVParser());
            if(lowestHumidityRecordSoFar==null){
                lowestHumidityRecordSoFar = currentRecord;
            }

            lowestHumidityRecordSoFar=compareTwoFilesHumidity(lowestHumidityRecordSoFar,currentRecord);

        }
        return lowestHumidityRecordSoFar;
    }
    
    public void testLowestHumidityInManyFiles(){
        CSVRecord lowestHumidityFound=lowestHumidityInManyFiles();
        System.out.println("The temperature recorded was "+lowestHumidityFound.get("TemperatureF")+" at "
        +/*lowestHumidityFound.get("TimeEST")+*/" on "+lowestHumidityFound.get("DateUTC")+". The weather was "+lowestHumidityFound.get("Conditions")
        +" with humidity "+lowestHumidityFound.get("Humidity"));
    }
    
    public CSVRecord compareTwoFilesHumidity(CSVRecord lowestHumidityRecordSoFar,CSVRecord currentRecord){
        Double currentHumdity=Double.parseDouble(currentRecord.get("Humidity"));
        Double lowestHumidity=Double.parseDouble(lowestHumidityRecordSoFar.get("Humidity"));
        if(currentHumdity<lowestHumidity){
            lowestHumidityRecordSoFar=currentRecord;
        }
        return lowestHumidityRecordSoFar;
    }
    
    
    public CSVRecord coldestHourInFile(CSVParser parser){
        CSVRecord coldestSoFar=null;
        for(CSVRecord currentRecord:parser){
            if(coldestSoFar==null){
                coldestSoFar=currentRecord;
            }
            else{
                Double currentTemp=Double.parseDouble(currentRecord.get("TemperatureF"));
                Double coldestTemp=Double.parseDouble(coldestSoFar.get("TemperatureF"));
                if(currentTemp<coldestTemp){
                    coldestSoFar=currentRecord;
                }
            }
        }
        return coldestSoFar;
    }
    
    public void testColdestHourInFile(){
        FileResource fr=new FileResource();
        CSVRecord coldestRecord=coldestHourInFile(fr.getCSVParser());
        System.out.println("The coldest temperature recorded was "+coldestRecord.get("TemperatureF")+" at "
        +/*coldestRecord.get("TimeEST")+*/" on "+coldestRecord.get("DateUTC")+". The weather was "+coldestRecord.get("Conditions")
        +" with humidity "+coldestRecord.get("Humidity"));
        
    }
    
    public CSVRecord compareTwoFiles(CSVRecord coldestRecordSoFar, CSVRecord currentRecord){
        Double currentTemp=Double.parseDouble(currentRecord.get("TemperatureF"));
        Double coldestTemp=Double.parseDouble(coldestRecordSoFar.get("TemperatureF"));
        if(currentTemp<coldestTemp){
            coldestRecordSoFar=currentRecord;
        }
        return coldestRecordSoFar;
    }
    
    public String fileWithColdestTemperature(){
        CSVRecord coldestRecordSoFar=null;
        DirectoryResource dr= new DirectoryResource();
        File ColdestFile=null;
        for(File f:dr.selectedFiles()){
            FileResource fr=new FileResource(f);
            CSVRecord currentRecord=coldestHourInFile(fr.getCSVParser());
            if(coldestRecordSoFar==null){
                coldestRecordSoFar = currentRecord;
                ColdestFile=f;
            }
            //String tempCurrentUTC=currentRecord.get("DateUTC");
            coldestRecordSoFar=compareTwoFiles(coldestRecordSoFar,currentRecord);
            //String tempColdestUTC=coldestRecordSoFar.get("DateUTC");
            if((coldestRecordSoFar.get("DateUTC")).equals(currentRecord.get("DateUTC"))){
                ColdestFile=f;
            }
            
            
        }
        return ColdestFile.getName();
    }
    
    public void testFileWithColdestTemperature(){
        //FileResource fr=new FileResource();
        String coldestFileName=fileWithColdestTemperature();
        System.out.println("Coldest day was in file: "+coldestFileName);
        String path="nc_weather/"+coldestFileName.substring(8,12)+"/"+coldestFileName;
        //File f=new File(coldestFileName);
        FileResource fr=new FileResource(path);
        CSVRecord coldestRecord=coldestHourInFile(fr.getCSVParser());
        System.out.println("Coldest Temperature on that day was: "+coldestRecord.get("TemperatureF"));
        CSVParser parser=fr.getCSVParser();
        System.out.println("All the Temperatures on the coldest Day were:");
        for(CSVRecord record:parser){
            System.out.println(record.get("DateUTC")+" "+record.get("TemperatureF"));
        }
        /*System.out.println("The coldest temperature recorded was "+coldestRecord.get("TemperatureF")+" at "
        +coldestRecord.get("TimeEST")+" on "+coldestRecord.get("DateUTC")+". The weather was "+coldestRecord.get("Conditions")
        +" with humidity "+coldestRecord.get("Humidity"));*/
        
    }
    
}
