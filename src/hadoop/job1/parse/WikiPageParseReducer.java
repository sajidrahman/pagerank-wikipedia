package hadoop.job1.parse;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class WikiPageParseReducer extends Reducer<Text, Text, Text, Text> {

    @Override
    public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        Configuration config = context.getConfiguration();
        Integer corpusSize = Integer.parseInt(config.get("size"));
        Double pageRank = 1.0 /corpusSize;
    	String initialPageRank = pageRank+"\t";
        String rankWithOutgoingLinks = initialPageRank;
        Set<String> linkSet = new HashSet<String>();
        
        for(Text val: values){
        	// remove duplication in outgoing links
        	linkSet.add(val.toString());
        }

        boolean first = true;
        Iterator<String> setIterator = linkSet.iterator();
        while (setIterator.hasNext()) {
        	
        	String value = setIterator.next();
            if(!first) rankWithOutgoingLinks += ",";

            rankWithOutgoingLinks += value;
            first = false;
        }

        context.write(key, new Text(rankWithOutgoingLinks));
    }
}
