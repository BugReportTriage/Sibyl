package ca.uleth.bugtriage.sibyl.servlet;

import java.util.Map;

import ca.uleth.bugtriage.sibyl.servlet.util.Recorder;
import ca.uleth.bugtriage.sibyl.servlet.util.SurveyUtils;
import ca.uleth.bugtriage.sibyl.servlet.webpage.GratitudePage;
import ca.uleth.bugtriage.sibyl.sibyl.SibylUser;
import ca.uleth.bugtriage.sibyl.utils.EmailNotification;
import ca.uleth.bugtriage.sibyl.utils.Messages;

public class RecommenderServlet extends AbstractRecommenderServlet {

	/**
	 * Generated id
	 */
	private static final long serialVersionUID = 2251020370506214855L;

	@Override
	protected String createPage() {
		Recorder.recordRecommenderSurvey(this.user, this.data, this.messages);
		SurveyUtils.writeSurveyToFile(this.user, this.data, ".recommender.survey",this.messages);
		EmailNotification.send("User #" + this.user.getId() + " submitted recommender survey.", SurveyUtils.formatData(this.data));
		return GratitudePage.get("Thank-you for providing information on your experience using the recommenders.");
	}
}
