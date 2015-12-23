package com.asadani.ca.pig.udf;

import org.apache.pig.EvalFunc;
import org.apache.pig.backend.executionengine.ExecException;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DataType;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.impl.logicalLayer.schema.Schema;

public class SessionAnalyzer extends EvalFunc<Tuple> {

	public Tuple exec(Tuple input) throws ExecException {
		// get input bag from PIG script
		DataBag bagFromPigScript = (DataBag) input.get(0);

		String lastToPreviousView = "";
		String previousView = "";
		String currentView = "";
		int refresh = 0;
		int back = 0;
		Tuple currentTuple = TupleFactory.getInstance().newTuple();
		Tuple tupleToReturn = TupleFactory.getInstance().newTuple(7);
		long sessionStart = 0;
		long sessionEnd = 0;
		for (Tuple tuple : bagFromPigScript) {

			currentTuple = tuple;

			lastToPreviousView = ("".equals(lastToPreviousView)) ? (String) tuple
					.get(4) : previousView;
			previousView = ("".equals(previousView)) ? (String) tuple.get(4)
					: currentView;

			if (sessionStart == 0)
				sessionStart = Long.parseLong((String) tuple.get(1)) / 1000;

			sessionEnd = Long.parseLong((String) tuple.get(1)) / 1000;

			currentView = (String) tuple.get(4);

			if (previousView.equals(currentView))
				refresh++;

			if (lastToPreviousView.equals(currentView))
				back++;
		}

		tupleToReturn.set(0, currentTuple.get(8).toString() + "_"
				+ currentTuple.get(5).toString() + "_"
				+ currentTuple.get(6).toString());

		tupleToReturn.set(1, refresh);
		tupleToReturn.set(2, back);
		tupleToReturn.set(3, (sessionEnd - sessionStart));

		return tupleToReturn;
	}

	public Schema outputSchema(Schema input) {
		try {
			Schema tupleSchema = new Schema();
			tupleSchema.add(new Schema.FieldSchema("rowkeyForOp",
					DataType.CHARARRAY));
			tupleSchema.add(new Schema.FieldSchema("refreshEventCount",
					DataType.INTEGER));
			tupleSchema.add(new Schema.FieldSchema("backEventCount",
					DataType.INTEGER));
			tupleSchema.add(new Schema.FieldSchema("sessionDuration",
					DataType.LONG));
			return new Schema(new Schema.FieldSchema(getSchemaName(this
					.getClass().getName().toLowerCase(), input), tupleSchema,
					DataType.TUPLE));
		} catch (Exception e) {
			return null;
		}
	}
}
