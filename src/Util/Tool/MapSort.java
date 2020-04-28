package Util.Tool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import TFIDF.SensitiveMethod;

public class MapSort<E, T> {
		public MapSort(){}
		public List<Map.Entry<E, T>>  sortMapByValue (Map<E, T> testMap){
			List<Map.Entry<E, T>> infoIds =
				    new ArrayList<Map.Entry<E, T>>(testMap.entrySet());
			Collections.sort(infoIds, new Comparator<Map.Entry<E, T>>() {   
			    public int compare(Map.Entry<E, T> o1, Map.Entry<E, T> o2) {      
			    	Double reDouble=Double.valueOf(o2.getValue().toString())-Double.valueOf(o1.getValue().toString());
			    	if(reDouble>0){
			    		return 1;
			    	}
			    	else if(reDouble<0){
			    		return -1;
			    	}
			    	else {
						return 0;
					}
			    }
			}); 
			return infoIds;
		}
	
}
