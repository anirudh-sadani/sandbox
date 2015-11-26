package com.asadani.ca.pig.udf;

import org.apache.pig.EvalFunc;
import org.apache.pig.backend.executionengine.ExecException;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DataType;
import org.apache.pig.data.Tuple;
import org.apache.pig.impl.logicalLayer.schema.Schema;

public class BagIterator extends EvalFunc<Tuple> {

	public Tuple exec(Tuple input) throws ExecException {
		// get input bag from PIG script
		DataBag bagFromPigScript = (DataBag) input.get(0);
		
		String lastToPreviousView = "";
		String previousView = "";
		String currentView = "";
		int refresh = 0;
		int back = 0;
		Tuple currentTuple = null;
		// Converting tuples to string and adding them to an arraylist
		for (Tuple tuple : bagFromPigScript) {
			
			currentTuple = currentTuple==null?tuple:currentTuple;
			
			lastToPreviousView = ("".equals(lastToPreviousView)) ? (String)tuple.get(4):previousView;
			previousView = ("".equals(previousView)) ? (String)tuple.get(4):currentView;
			
			currentView = (String)tuple.get(4);
			
			if (previousView.equals(currentView))
				refresh ++;
			
			if (lastToPreviousView.equals(currentView))
				back ++;

		}
		
		currentTuple.append(refresh);
		currentTuple.append(back);


		return currentTuple;
		//DataBag internalBag = BagFactory.getInstance().newDefaultBag();
		//internalBag.add(currentTuple);
		//return internalBag;
	}
	
	 public Schema outputSchema(Schema input) {
	        try{
	            Schema tupleSchema = new Schema();
	            tupleSchema.add(input.getField(1));
	            tupleSchema.add(input.getField(0));
	            return new Schema(new Schema.FieldSchema(getSchemaName(this.getClass().getName().toLowerCase(), input),tupleSchema, DataType.TUPLE));
	        }catch (Exception e){
	                return null;
	        }
	    }
}
