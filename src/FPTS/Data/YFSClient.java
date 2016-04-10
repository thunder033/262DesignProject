package FPTS.Data;

import FPTS.Models.MarketEquity;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gjr8050 on 4/6/2016.
 * Handles interactions with Yahoo Financial Services API
 */
public class YFSClient {

    private float maxCacheAge = 1000 * 60 * 5;
    private Map<String, CachedPrice> cache;
    private static YFSClient instance = null;

    /**
     * Holds a requested equity price and when it was requested
     */
    private class CachedPrice {
        final float sharePrice;
        final Date date;

        public CachedPrice(float sharePrice){
            this.sharePrice = sharePrice;
            date = new Date();
        }

        public float getSharePrice(){
            return sharePrice;
        }

        boolean isExpired(){
            long diff = new Date().getTime() - date.getTime();
            return diff > maxCacheAge;
        }
    }

    private YFSClient(){
        cache = new HashMap<>();
    }

    public static YFSClient instance(){
        if(instance == null){
            instance = new YFSClient();
        }

        return instance;
    }

    /**
     * Set the maximum time before a cached share price expires
     * @param maxCacheAge max age in seconds
     */
    public void setMaxCacheAge(float maxCacheAge){
        this.maxCacheAge = maxCacheAge * 1000;
    }

    /**
     * Requests a share price for an equity, caching the result. If a non-expired cached price is present, this is return instead
     * @param equity the equity to look up
     * @return the share price of the equity
     */
    public float getSharePrice(MarketEquity equity) throws NumberFormatException, IOException{
        String ticker = equity.getTickerSymbol();
        if(cache.containsKey(ticker) && !cache.get(ticker).isExpired()){
            System.out.println("Return cached " + ticker + ":" + cache.get(ticker).getSharePrice());
            return cache.get(ticker).getSharePrice();
        }

        float sharePrice = 0;

        try {

            String stockParameter = "%22" + ticker + "%22";
            String mainURL = "http://query.yahooapis.com/v1/public/yql?q=select%20LastTradePriceOnly" +
                    "%20from%20yahoo.finance.quotes%20where%20symbol%20in%20" +
                    "(" + stockParameter + ")&env=store://datatables.org/alltableswithkeys";
            // Create a URL and open a connection
            URL YahooURL = new URL(mainURL);
            HttpURLConnection con = (HttpURLConnection) YahooURL.openConnection();

            try {
                // Set the HTTP Request type method to GET
                con.setRequestMethod("GET");
                con.setConnectTimeout(10000);
                con.setReadTimeout(10000);

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(con.getInputStream());
                Element root = doc.getDocumentElement();
                root.normalize();

                // Get current share price attribute
                NodeList nodeList = doc.getElementsByTagName("quote");
                Node node;
                int i = 0;
                do {
                    node = nodeList.item(i);
                    i++;
                } while (node.getNodeType() != Node.ELEMENT_NODE);
                Element eElement = (Element) node;
                sharePrice = Float.valueOf(eElement.getElementsByTagName("LastTradePriceOnly").item(0).getTextContent());
                CachedPrice price = new CachedPrice(sharePrice);
                cache.put(ticker, price);

            } catch (ParserConfigurationException | SAXException e) {
                e.printStackTrace();
            } finally {
                if(con != null){
                    con.getInputStream().close();
                }
            }
        } catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e) {
            if(cache.containsKey(ticker)){
                sharePrice = cache.get(ticker).sharePrice;
            }else {
                throw new IOException();
            }
        }



        return sharePrice;
    }

}
