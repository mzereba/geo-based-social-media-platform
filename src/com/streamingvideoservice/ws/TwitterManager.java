/**
 * Apr 1, 2014
 * TwitterManager.java 
 * @author mzereba
 */
package com.streamingvideoservice.ws;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

import com.streamingvideoservice.statics.TwitterInterface;

/**
 *
 * mzereba
 */
public class TwitterManager {
	
	private static String tweets;
	
	/**
	 * @return the tweets
	 */
	public static String getTweets() {
		return tweets;
	}

	/**
	 * @param tweets the tweets to set
	 */
	public static void setTweets(String str) {
		tweets = str;
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
	        //query.setSince("2013-11-1");
	        //query.setUntil("2013-12-31");
	        QueryResult result;
	        result = twitter.search(query);
	        List<Status> tweetList = result.getTweets();
	        String file = "";
	        for (Status tweet : tweetList) {
	            System.out.println("@" + tweet.getUser().getScreenName() + " - " + tweet.getText() + " - " + tweet.getCreatedAt());
	            file += "@" + tweet.getUser().getScreenName() + " - " + tweet.getText() + " - " + tweet.getCreatedAt() + "\n\n";
	        }
	        setTweets(file);
	        WriteTweets();
	    }
	    catch (TwitterException te) {
	        te.printStackTrace();
	        System.out.println("Failed to search tweets: " + te.getMessage());
	        System.exit(-1);
	    }
	}
	
	public static void WriteTweets() {
		PrintWriter writer = null;
		try {
			String filename = "Tweets_"+System.currentTimeMillis()+".txt";
			writer = new PrintWriter(filename, "UTF-8");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		writer.println(getTweets());
		writer.close();
    }

}
