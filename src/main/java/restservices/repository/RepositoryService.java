package restservices.repository;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

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
		logger.debug("In getRepositoryProduct "+repositoryURL);
		repositoryURL += "?method=Product.get&params=[{\"type\":\"accessible\",\"include_fields\":[\"name\"]}]";
		
		String response = HttpClient.sendGet(repositoryURL);
		ObjectMapper mapper = new ObjectMapper();
		RepositoryProduct rp = mapper.readValue(response, RepositoryProduct.class);
		return rp;
	}
	
	
}
