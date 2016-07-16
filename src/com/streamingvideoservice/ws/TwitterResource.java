package com.streamingvideoservice.ws;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlElement;

import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

import com.streamingvideoservice.statics.TwitterInterface;
import com.sun.jersey.spi.resource.Singleton;

@Path("twitter")
@Singleton
public class TwitterResource {

	@GET
	@XmlElement(name = "tweet")
	@Path("/retrieveTweets")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Tweet> retrieve(@QueryParam("lat") Double Latitude, @QueryParam("lon") Double Longitude, @QueryParam("rad") Double Radius, @QueryParam("from") String From, @QueryParam("to") String To) {
		//List<Status> tweets = null;
		List<Tweet> lTweets = new ArrayList<Tweet>();
		boolean status = false;
		
		Twitter twitter = new TwitterFactory().getInstance();

	    AccessToken accessToken = new AccessToken(TwitterInterface.ACCESS_TOKEN, TwitterInterface.ACCESS_TOKEN_SECRET);
	    twitter.setOAuthConsumer(TwitterInterface.CONSUMER_KEY, TwitterInterface.CONSUMER_SECRET);
	    twitter.setOAuthAccessToken(accessToken);

	    try {
	        Query query = new Query("");
	        GeoLocation geo =  new GeoLocation(Latitude, Longitude);
	        query.setGeoCode(geo, Radius/1000, Query.KILOMETERS);
	        query.setCount(100);
	        query.setSince(From);
	        query.setUntil(To);
	        QueryResult result;
	        result = twitter.search(query);
	        List<Status>tweets = result.getTweets();
	        for (Status tweet : tweets) {
	            System.out.println("@" + tweet.getUser().getScreenName() + " - " + tweet.getText() + " - " + tweet.getCreatedAt());
	            Tweet t = new Tweet();
	            t.setUser(tweet.getUser().getScreenName());
	            t.setText(tweet.getText());
	            lTweets.add(t);
	        }
	    }
	    catch (TwitterException te) {
	        te.printStackTrace();
	        System.out.println("Failed to search tweets: " + te.getMessage());
	        System.exit(-1);
	    }
	    
	    return lTweets;
		
	}
	
	public static void WriteTweets(String str) {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("Tweets_"+System.currentTimeMillis()+".txt", "UTF-8");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		writer.println(str);
		writer.close();
    }
	
	public static void main(String[] args) {
		Twitter twitter = new TwitterFactory().getInstance();

	    AccessToken accessToken = new AccessToken(TwitterInterface.ACCESS_TOKEN, TwitterInterface.ACCESS_TOKEN_SECRET);
	    twitter.setOAuthConsumer(TwitterInterface.CONSUMER_KEY, TwitterInterface.CONSUMER_SECRET);
	    twitter.setOAuthAccessToken(accessToken);

	    try {
	        Query query = new Query("");
	        GeoLocation geo =  new GeoLocation(30.0444, 31.2357);
	        query.setGeoCode(geo, 1000, Query.KILOMETERS);
	        query.setCount(100);
	        query.setSince("2013-11-1");
	        //query.setUntil("2013-12-31");
	        QueryResult result;
	        result = twitter.search(query);
	        List<Status> tweets = result.getTweets();
	        String file = "";
	        for (Status tweet : tweets) {
	            System.out.println("@" + tweet.getUser().getScreenName() + " - " + tweet.getText() + " - " + tweet.getCreatedAt());
	            file += "@" + tweet.getUser().getScreenName() + " - " + tweet.getText() + " - " + tweet.getCreatedAt() + "\n\n";
	        }
	        WriteTweets(file);
	    }
	    catch (TwitterException te) {
	        te.printStackTrace();
	        System.out.println("Failed to search tweets: " + te.getMessage());
	        System.exit(-1);
	    }
	}
}