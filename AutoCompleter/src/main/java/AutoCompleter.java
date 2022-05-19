import wiremock.net.minidev.json.JSONArray;
import wiremock.net.minidev.json.parser.JSONParser;
import wiremock.net.minidev.json.parser.ParseException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;


public class AutoCompleter
{
    int numOfMaps = 10;
    HashMap<String, LinkedList>[] mapPrefix = new HashMap[numOfMaps];

    public LinkedList giveCitiesWithPrefix(String prefix) throws IOException {

        int N = prefix.length() - 1;
        LinkedList list;
        JSONArray res = new JSONArray();

        FileWriter file = new FileWriter("./files/completeResult.json");

        if(N >= numOfMaps)
        {
            list = mapPrefix[numOfMaps-1].get(prefix);
        }
        else
        {
            list = mapPrefix[N].get(prefix);
        }

        res.add(list);
        file.write(res.toJSONString());

        file.close();
        return list;
    }

    private void parseCitiesFile(String fileName) throws FileNotFoundException {

        JSONParser parser = new JSONParser();

        try {

            JSONArray array = (JSONArray) parser.parse(new FileReader(System.getProperty("user.dir")+ "\\files\\"+ fileName));

            System.out.println(array.size());

            for(int i=0; i<array.size(); i++)
            {
                HashMap<String,String> map = (HashMap<String, String>) array.get(i);

                String name = map.get("name");
                int N = name.length();

                for(int j=0; j<Math.min(N,numOfMaps); j++)
                {
                    String subs = name.substring(0,j+1);
                    if(mapPrefix[j].containsKey(subs))
                    {
                        LinkedList list = mapPrefix[j].get(subs);
                        list.add(name);
                    }
                    else
                    {
                        LinkedList list = new LinkedList();
                        list.add(name);
                        mapPrefix[j].put(subs,list);
                    }
                }

                if(N > numOfMaps)
                {
                    for(int j=numOfMaps; j<N; j++)
                    {
                        String subs = name.substring(0,j+1);
                        if(mapPrefix[numOfMaps-1].containsKey(subs))
                        {
                            LinkedList list = mapPrefix[numOfMaps-1].get(subs);
                            list.add(name);
                        }
                        else
                        {
                            LinkedList list = new LinkedList();
                            list.add(name);
                            mapPrefix[numOfMaps-1].put(subs,list);
                        }
                    }
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public AutoCompleter() throws FileNotFoundException {
        for(int i=0; i<numOfMaps; i++)
        {
            mapPrefix[i] = new HashMap<>();
        }
        parseCitiesFile("priorCities.json");
        parseCitiesFile("cities.json");
    }
}
