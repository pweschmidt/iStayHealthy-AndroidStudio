package com.pweschmidt.healthapps;
import java.util.Vector;
import java.util.Iterator;
import android.widget.CheckBox;

public class StringMap extends Vector<StringCheckBoxPair>{
	public static final long serialVersionUID = 100L;
	public void put(StringCheckBoxPair pair){
		add(pair);
	}

	public void put(String key, CheckBox value){
		add(new StringCheckBoxPair(key, value));
	}
	/**
	 * 
	 * @param box
	 * @return
	 */
	public boolean containsCheckBox(CheckBox box){
		Iterator <StringCheckBoxPair> iterator = this.iterator();
		while(iterator.hasNext()){
			StringCheckBoxPair pair = (StringCheckBoxPair)iterator.next();
			if(box.equals(pair.getValue())){
				return true;
			}
		}
		return false;
	}

	/**
	 * returns null if checkbox not found
	 * @param box
	 * @return
	 */
	public String getStringForCheckBox(CheckBox box){
		String key = null;
		Iterator <StringCheckBoxPair> iterator = this.iterator();
		while(iterator.hasNext()){
			StringCheckBoxPair pair = (StringCheckBoxPair)iterator.next();
			if(box.equals(pair.getValue())){
				return pair.getKey();
			}
		}		
		return key;
	}

	/**
	 * 
	 * @param box
	 */
	public void removeFromList(CheckBox box){
		StringCheckBoxPair pairToBeRemoved = null;
		Iterator <StringCheckBoxPair> iterator = this.iterator();
		while(iterator.hasNext()){
			StringCheckBoxPair pair = (StringCheckBoxPair)iterator.next();
			if(box.equals(pair.getValue())){
				pairToBeRemoved = pair;
			}
		}		
		if(null != pairToBeRemoved){
			remove(pairToBeRemoved);
		}
	}
}
