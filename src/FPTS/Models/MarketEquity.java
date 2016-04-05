package FPTS.Models;

import FPTS.Core.Model;
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

/**
 * @author: Greg
 * Created: 3/9/16
 * Revised: 3/13/16
 * Description: Stores the ticker
 * symbol, name, and share price
 * of given equity on the market.
 */
public class MarketEquity extends Model {
    protected String _name;
    protected float _sharePrice;

    //Equity index;
    protected MarketEquity(String tickerSymbol)
    {
        super(tickerSymbol);
    }

    public String getTickerSymbol() {
        return id;
    }

    public String getName() {
        return _name;
    }

    public float getSharePrice() {

        float sharePrice = 0;

        try {

            String stockParameter = "%22" + this.getTickerSymbol() + "%22";
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

            } catch (IOException | ParserConfigurationException | SAXException e) {
                e.printStackTrace();
            } finally {
                con.getInputStream().close();
            }
        } catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e) {
            sharePrice = _sharePrice;
            e.printStackTrace();
        }
        return sharePrice;
    }
}
