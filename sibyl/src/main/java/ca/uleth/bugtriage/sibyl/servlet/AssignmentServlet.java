package ca.uleth.bugtriage.sibyl.servlet;

import java.util.Map;
import java.util.Random;

import ca.uleth.bugtriage.sibyl.servlet.util.Recorder;
import ca.uleth.bugtriage.sibyl.servlet.util.SurveyUtils;
import ca.uleth.bugtriage.sibyl.servlet.webpage.AssignmentChoiceForm;
import ca.uleth.bugtriage.sibyl.servlet.webpage.GratitudePage;
import ca.uleth.bugtriage.sibyl.utils.EmailNotification;

public class AssignmentServlet extends AbstractRecommenderServlet {

	/**
	 * Generated serializtion id
	 */
	private static final long serialVersionUID = 4281646103644440690L;

	@Override
	protected String createPage() {
		// this.printData();
		Recorder.recordAssignmentSurvey(this.user, this.data, this.messages);
		String notApplicable = this.data
				.get(AssignmentChoiceForm.NOT_APPLICABLE_LABEL);
		if (notApplicable != null) {
			EmailNotification.send("User #" + this.user.getId()
					+ " said assignment survey not applicable.");
			return GratitudePage.get("Thanks anyways.");
		}

		SurveyUtils.writeSurveyToFile(this.user, this.data, ".assignment.survey", this.messages);
		EmailNotification.send("User #" + this.user.getId()
				+ " submitted assignment survey.", SurveyUtils.formatData(this.data));
		return GratitudePage.get("Thank-you for responding to the survey.");
	}

	

}
