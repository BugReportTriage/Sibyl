package restservices.repository;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.datacollection.BugzillaDataset;
import restservices.repositoryBean.RepositoryProduct;
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
		String data = BugzillaDataset.getReports(Project.FIREFOX);
		//System.out.print(data);		
		//BugzillaDataset.writeToFile(Project.FIREFOX, data);
		return data;
	}
	
}
