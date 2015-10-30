package com.asadani.pojo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.ThreadLocalRandom;

import com.asadani.pojo.utils.SessionInfo;
import com.asadani.pojo.utils.ZipRange;

public class JSONDataPojo {

	String zipcode;
	String originatingIPAddress;
	HTTPDataPojo httpData;
	long timestamp;

	static long counter = 1;
	static List<ZipRange> validZipRanges;
	static List<SessionInfo> sessionInfoList;

	public JSONDataPojo() {

		setOriginatingIPAddress(ThreadLocalRandom.current().nextInt(1, 256)
				+ "." + ThreadLocalRandom.current().nextInt(0, 256) + "."
				+ ThreadLocalRandom.current().nextInt(0, 256) + "."
				+ ThreadLocalRandom.current().nextInt(0, 256));

		populateZipcode();

		SessionInfo randomSession = sessionInfoList.get(ThreadLocalRandom.current().nextInt(0, sessionInfoList.size()));
		httpData = new HTTPDataPojo(randomSession.getUserId(), randomSession.getAuthToken());

	}

	static {
		List<ZipRange> populatedRanges = new ArrayList<ZipRange>();

		BufferedReader br = null;

		try {

			String sCurrentLine;
			br = new BufferedReader(
					new FileReader(
							"/home/IMPETUS/asadani/codebase/rampup/sandbox/data-generator/src/main/resources/zipranges.txt"));
			while ((sCurrentLine = br.readLine()) != null) {
				StringTokenizer str = new StringTokenizer(sCurrentLine, "-");
				populatedRanges.add(new ZipRange(Integer.parseInt(str
						.nextToken()), Integer.parseInt(str.nextToken())));
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		validZipRanges = populatedRanges;

		List<SessionInfo> sessionInforTemp = new ArrayList<SessionInfo>();
		
		sessionInfoList = new ArrayList<SessionInfo>();

		try {

			String sCurrentLine;
			br = new BufferedReader(
					new FileReader(
							"/home/IMPETUS/asadani/codebase/rampup/sandbox/data-generator/src/main/resources/sessionInfo.csv"));
			while ((sCurrentLine = br.readLine()) != null) {
				String[] sessionDetails = sCurrentLine.split(",");
				sessionInforTemp.add(new SessionInfo(sessionDetails[0],
						sessionDetails[1]));

			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		sessionInfoList = sessionInforTemp;
	}

	public void populateModel(Calendar startDate) {
		populateHTTPData();
		populateTimestamp(startDate);

	}

	private void populateTimestamp(Calendar startDate) {

		Calendar temp = Calendar.getInstance();

		// temp.setTimeInMillis(startDate.getTimeInMillis());
		temp.set(startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH),
				startDate.get(Calendar.DATE),
				Calendar.getInstance().get(Calendar.HOUR), Calendar
						.getInstance().get(Calendar.MINUTE), (int) (Calendar
						.getInstance().get(Calendar.SECOND) + counter));

		counter++;
		setTimestamp(temp.getTimeInMillis());

	}

	private void populateHTTPData() {
		this.httpData.populateModel();

	}

	private void populateZipcode() {
		ZipRange zipRange = validZipRanges.get(ThreadLocalRandom.current()
				.nextInt(0, validZipRanges.size()));

		setZipcode(zipRange.getStartZip()
				+ ThreadLocalRandom.current().nextInt(0,
						zipRange.getDifference() + 1) + "");
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getOriginatingIPAddress() {
		return originatingIPAddress;
	}

	public void setOriginatingIPAddress(String originatingIPAddress) {
		this.originatingIPAddress = originatingIPAddress;
	}

	public HTTPDataPojo getHttpData() {
		return httpData;
	}

	public void setHttpData(HTTPDataPojo httpData) {
		this.httpData = httpData;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String toString() {
		return "{" + "originatingIPAddress:'" + originatingIPAddress + "', "
				+ "timestamp:'" + timestamp + "', " + "httpData:"
				+ httpData.toString() + ", " + "zipcode:'" + zipcode + "'}";
	}

}
