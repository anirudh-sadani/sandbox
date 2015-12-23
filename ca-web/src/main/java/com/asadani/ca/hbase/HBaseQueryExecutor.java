package com.asadani.ca.hbase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.PrefixFilter;

public class HBaseQueryExecutor {

	HBaseConnectionManager manager;

	public HBaseQueryExecutor(HBaseConnectionManager manager) {
		this.manager = manager;
	}
	
	public List<Map<byte[], byte[]>> fetchData(String tableName,
			String columnFamily, String fromrowkey, String torowkey) {
		Table table = null;
		List<Map<byte[], byte[]>> resultList = new ArrayList();
		try {
			table = manager.getConnection().getTable(
					TableName.valueOf(tableName));
			Scan scan = new Scan(Bytes.toBytes(fromrowkey),
					Bytes.toBytes(torowkey));
			scan.addFamily(Bytes.toBytes(columnFamily));
			ResultScanner scanner = table.getScanner(scan);
			for (Result result = scanner.next(); result != null; result = scanner
					.next()) {
				NavigableMap<byte[], byte[]> familyMap = result
						.getFamilyMap(Bytes.toBytes(columnFamily));
				familyMap.put(Bytes.toBytes("rowkey"), result.getRow());
				resultList.add(familyMap);
			}
			return resultList;
		} catch (IOException e) {

		} finally {
			if (table != null)
				try {
					table.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

		}
		return resultList;
	}

	public List<Map<byte[], byte[]>> fetchDataWithPrefixFilter(
			String tableName, String columnFamily, String fromRowKeyPattern,
			String toRowKeyPattern) {
		Table table = null;
		List<Map<byte[], byte[]>> resultList = new ArrayList();
		try {
			table = manager.getConnection().getTable(
					TableName.valueOf(tableName));

			Scan scan = new Scan();
			scan.setFilter(getIndividualFiltersforRange(fromRowKeyPattern,
					toRowKeyPattern));

			scan.addFamily(Bytes.toBytes(columnFamily));
			ResultScanner scanner = table.getScanner(scan);
			for (Result result = scanner.next(); result != null; result = scanner
					.next()) {
				NavigableMap<byte[], byte[]> familyMap = result
						.getFamilyMap(Bytes.toBytes(columnFamily));

				String rowKeyData = Bytes.toString(result.getRow());

				familyMap.put(Bytes.toBytes("rowkey"), rowKeyData.getBytes());

				if (rowKeyData.indexOf("_") != -1)
					familyMap.put(Bytes.toBytes("datekey"), rowKeyData
							.substring(0, rowKeyData.indexOf("_")).getBytes());

				resultList.add(familyMap);
			}
			return resultList;
		} catch (IOException e) {

		} finally {
			if (table != null)
				try {
					table.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

		}
		return resultList;
	}

	private FilterList getIndividualFiltersforRange(String fromRowKeyPattern,
			String toRowKeyPattern) {

		FilterList filterList = new FilterList(
				FilterList.Operator.MUST_PASS_ONE);

		String[] startDate = fromRowKeyPattern.split("-");

		int startDay = Integer.parseInt(startDate[0]);
		int startMonth = Integer.parseInt(startDate[1]);
		int startYear = Integer.parseInt(startDate[2]);

		String[] endDate = toRowKeyPattern.split("-");

		int endDay = Integer.parseInt(endDate[0]);
		int endMonth = Integer.parseInt(endDate[1]);
		int endYear = Integer.parseInt(endDate[2]);

		Calendar startCalendar = Calendar.getInstance();

		startCalendar.set(startYear, startMonth - 1, startDay);

		Calendar endCalendar = Calendar.getInstance();

		endCalendar.set(endYear, endMonth - 1, endDay);

		long numberOfDays = (endCalendar.getTimeInMillis() - startCalendar
				.getTimeInMillis()) / (1000 * 60 * 60 * 24);

		for (long i = 1; i <= numberOfDays; i++) {
			String dateFilterString = startCalendar.get(Calendar.YEAR)
					+ "-"
					+ (startCalendar.get(Calendar.MONTH) + 1)
					+ "-"
					+ (startCalendar.get(Calendar.DAY_OF_MONTH) <= 9 ? "0"
							+ startCalendar.get(Calendar.DAY_OF_MONTH)
							: startCalendar.get(Calendar.DAY_OF_MONTH));

			filterList.addFilter(new PrefixFilter(dateFilterString.getBytes()));

			startCalendar.add(Calendar.DAY_OF_MONTH, 1);
		}

		return filterList;
	}
}
