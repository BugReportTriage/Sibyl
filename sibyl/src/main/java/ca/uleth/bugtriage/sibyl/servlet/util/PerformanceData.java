package ca.uleth.bugtriage.sibyl.servlet.util;

public class PerformanceData {

	private double reportTime;

	private double recommendationsTime;

	private double buildPageTime;

	private double searchResultsTime;

	private final static String GET_REPORT = "getReport";

	private final static String GET_RECOMMENDATIONS = "getRecommendations";

	private final static String BUILD_PAGE = "buildPage";

	private final static String SEARCH_PAGE = "getSearchPage";

	public PerformanceData(){}
	
	/*
	 * @arg data the string from PerformanceData.toString() e.g. getReport=1.007
	 * getRecommendations=0.0 buildPage=0.99
	 */
	public PerformanceData(String data) {
		String[] pairs = data.split(" ");
		for (String string : pairs) {
			String[] keyValue = string.split("=");
			if (keyValue[0].equals(GET_REPORT)) {
				this.setReportTime(Double.parseDouble(keyValue[1]));
			}
			if (keyValue[0].equals(GET_RECOMMENDATIONS)) {
				this.setRecommendationsTime(Double.parseDouble(keyValue[1]));
			}
			if (keyValue[0].equals(BUILD_PAGE)) {
				this.setBuildPageTime(Double.parseDouble(keyValue[1]));
			}
			if (keyValue[0].equals(SEARCH_PAGE)) {
				this.setSearchResultsTime(Double.parseDouble(keyValue[1]));
			}
		}
	}

	public void setBuildPageTime(double time) {
		this.buildPageTime = time;
	}

	public void setRecommendationsTime(double time) {
		this.recommendationsTime = time;
	}

	public void setReportTime(double time) {
		this.reportTime = time;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(" " + GET_REPORT + "=" + this.reportTime);
		sb.append(" " + GET_RECOMMENDATIONS + "=" + this.recommendationsTime);
		sb.append(" " + BUILD_PAGE + "=" + this.buildPageTime);
		sb.append(" " + SEARCH_PAGE + "=" + this.searchResultsTime);
		return sb.toString();
	}

	public double getBuildPageTime() {
		return this.buildPageTime;
	}

	public double getRecommendationsTime() {
		return this.recommendationsTime;
	}

	public double getReportTime() {
		return this.reportTime;
	}

	public double getSearchResultsTime() {
		return searchResultsTime;
	}

	public void setSearchResultsTime(double searchResultsTime) {
		this.searchResultsTime = searchResultsTime;
	}

}
