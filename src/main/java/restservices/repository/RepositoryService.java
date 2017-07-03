package restservices.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.datacollection.BugzillaDataset;
import ca.uleth.bugtriage.sibyl.report.BugReport;
import restservices.repositoryBean.RepositoryProduct;
import restservices.repositoryBean.TriageRecommender;
import restservices.utilities.HttpClient;

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
		List<BugReport> reports = BugzillaDataset.getData(Project.FIREFOX);
		//System.out.print(data);	
		//export report method is giving error
		BugzillaDataset.exportReports(Project.FIREFOX, reports);
		return "success";
	}
	@Path("readRecommenderData")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public TriageRecommender readRecommenderData() throws Exception {
		logger.info("In readRecommenderData ");
		List<BugReport> reports = BugzillaDataset.getData(Project.FIREFOX);
		Map<String, Integer> fixingFreq = new HashMap<String, Integer>();
		for(BugReport b : reports){
			Integer n = fixingFreq.get(b.getAssigned());
			n = (n == null) ? 1 : ++n;			
			fixingFreq.put(b.getAssigned(), n);
		}
		TriageRecommender rec = new TriageRecommender();
		rec.setFrequencyCutoff(fixingFreq);
		return rec;
	}
}
