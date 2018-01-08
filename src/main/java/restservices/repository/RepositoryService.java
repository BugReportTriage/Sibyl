package restservices.repository;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.activity.events.ResolutionType;
import ca.uleth.bugtriage.sibyl.datacollection.BugzillaDataset;
import ca.uleth.bugtriage.sibyl.report.BugReport;
import restservices.repositoryBean.RepositoryProduct;
import restservices.repositoryBean.TriageRecommender;
import restservices.utilities.HttpClient;
import restservices.utilities.SortMapByValue;

@Path("repository")
public class RepositoryService 
{
	private static final Logger logger = Logger.getLogger(RepositoryService.class);

	@Path("getRepositoryProduct")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public RepositoryProduct getRepositoryProduct(@QueryParam("repositoryURL") String repositoryURL) throws Exception {
		logger.info("In getRepositoryProduct "+repositoryURL);
		repositoryURL += "?method=Product.get&params=[{\"type\":\"accessible\",\"include_fields\":[\"name\"]}]";
		
		String response = HttpClient.sendGet(repositoryURL);
		ObjectMapper mapper = new ObjectMapper();
		RepositoryProduct rp = mapper.readValue(response, RepositoryProduct.class);
		return rp;
	}
	@Path("saveRecommenderData")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String saveRecommenderData(@QueryParam("reportDate") String reportDate,@QueryParam("reportLimit") String reportLimit,@QueryParam("recommenderName") String recommenderName,@QueryParam("recommenderPath") String recommenderPath) throws Exception {
		logger.info("In saveRecommenderData "+reportDate+"  "+reportLimit+"  "+recommenderName+"  "+recommenderPath);
		//TODO set url report start end date limit, recommender dir		
		//we need to change project constructor
		BugzillaDataset b = new BugzillaDataset(Project.FIREFOX);
		Set<BugReport> reports = b.getData();
		File f = new File("D:\\UofL\\CASTR_Workspace_New\\Sibyl\\data\\recommender.json");
		b.exportReports(f);
		return "success";
	}
	@Path("readRecommenderData")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public TriageRecommender readRecommenderData() throws Exception {
		logger.info("In readRecommenderData ");
		File f = new File("D:\\UofL\\CASTR_Workspace_New\\Sibyl\\data\\recommender.json");
		BugzillaDataset b = new BugzillaDataset(Project.FIREFOX);
		Set<BugReport> reports = b.importReports(f);
		Map<String, Integer> fixingFreq = new HashMap<String, Integer>();
		Map<String, Integer> resolutionGroup = new HashMap<String, Integer>();
		int totalBug = 0;
		for(BugReport br : reports){
			Integer n = fixingFreq.get(br.getAssigned());
			n = (n == null) ? 1 : ++n;			
			fixingFreq.put(br.getAssigned(), n);
			
			Integer m = resolutionGroup.get(br.getResolution().getValue());
			m = (m == null) ? 1 : ++m;			
			resolutionGroup.put(br.getResolution().getValue(), m);
			totalBug++;
		}
		Map<String, Integer> sortedFixingFreq = SortMapByValue.sortByComparator(fixingFreq, false); 
		Map<String, Integer> sortedResolutionGroup = SortMapByValue.sortByComparator(resolutionGroup, false); 
		TriageRecommender rec = new TriageRecommender();
		rec.setFrequencyCutoff(sortedFixingFreq);
		rec.setResolutionGroup(sortedResolutionGroup);
		rec.setResolutionTypes(Arrays.asList(ResolutionType.values()));
		rec.setTotalBugReport(totalBug);
		return rec;
	}
}
