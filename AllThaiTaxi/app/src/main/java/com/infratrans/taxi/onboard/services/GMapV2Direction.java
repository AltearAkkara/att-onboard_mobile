package com.infratrans.taxi.onboard.services;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.infratrans.taxi.onboard.util.Global;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Thanisak Piyasaksiri on 2/7/15 AD.
 */
public class GMapV2Direction {

    public final static String MODE_DRIVING = "driving";
    public final static String MODE_WALKING = "walking";

    public GMapV2Direction() {

    }

    public String getGoogleDirectionAPI(LatLng start, LatLng end, String mode) {

        /*return "http://maps.googleapis.com/maps/api/directions/xml?"
                + "origin=" + start.latitude + "," + start.longitude
                + "&destination=" + end.latitude + "," + end.longitude
                + "&sensor=false&units=metric&mode=driving";*/

        // Add by Beat
        if(start != null && end != null) {
            return "http://122.155.197.207/passenger/api/get_direction.php?origin=" + start.latitude
                    + "," + start.longitude + "&destination=" + end.latitude + "," + end.longitude;
        }
        else return "";
    }

    public Document getDocument(LatLng start, LatLng end, String mode) {


        String url = "http://122.155.197.207/passenger/api/get_direction.php?origin=" + start.latitude
                + "," + start.longitude + "&destination=" + end.latitude + "," + end.longitude;


        try {

            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(url);
            HttpResponse response = httpClient.execute(httpPost, localContext);
            InputStream in = response.getEntity().getContent();
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(in);
            return doc;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getDurationText(Document doc) {

        try {

            NodeList nl1 = doc.getElementsByTagName("duration");

            Global.printLog(false, "nl1", String.valueOf(nl1.getLength()));
            //Node node1 = nl1.item(0);
            Node node1 = nl1.item(nl1.getLength() - 1);
            NodeList nl2 = node1.getChildNodes();
            Node node2 = nl2.item(getNodeIndex(nl2, "text"));
            Log.i("DurationText", node2.getTextContent());
            return node2.getTextContent();

        } catch (Exception e) {
            return "0";
        }
    }

    public int getDurationValue(Document doc) {

        try {

            NodeList nl1 = doc.getElementsByTagName("duration");
            Node node1 = nl1.item(0);
            NodeList nl2 = node1.getChildNodes();
            Node node2 = nl2.item(getNodeIndex(nl2, "value"));
            Log.i("DurationValue", node2.getTextContent());
            return Integer.parseInt(node2.getTextContent());

        } catch (Exception e) {
            return -1;
        }
    }

    public String getDistanceText(Document doc) {
    /*
     * while (en.hasMoreElements()) { type type = (type) en.nextElement();
     *
     * }
     */
        try {

            NodeList nl1;
            nl1 = doc.getElementsByTagName("distance");

            Node node1 = nl1.item(nl1.getLength() - 1);
            NodeList nl2 = null;
            nl2 = node1.getChildNodes();
            Node node2 = nl2.item(getNodeIndex(nl2, "value"));
            Log.d("DistanceText", node2.getTextContent());
            return node2.getTextContent();

        } catch (Exception e) {
            return "-1";
        }

    /*
     * NodeList nl1; if(doc.getElementsByTagName("distance")!=null){ nl1=
     * doc.getElementsByTagName("distance");
     *
     * Node node1 = nl1.item(nl1.getLength() - 1); NodeList nl2 = null; if
     * (node1.getChildNodes() != null) { nl2 = node1.getChildNodes(); Node
     * node2 = nl2.item(getNodeIndex(nl2, "value")); Log.d("DistanceText",
     * node2.getTextContent()); return node2.getTextContent(); } else return
     * "-1";} else return "-1";
     */
    }

    public int getDistanceValue(Document doc) {

        try {

            NodeList nl1 = doc.getElementsByTagName("distance");
            Node node1 = null;
            node1 = nl1.item(nl1.getLength() - 1);
            NodeList nl2 = node1.getChildNodes();
            Node node2 = nl2.item(getNodeIndex(nl2, "value"));
            Log.i("DistanceValue", node2.getTextContent());
            return Integer.parseInt(node2.getTextContent());

        } catch (Exception e) {
            return -1;
        }
    /*
     * NodeList nl1 = doc.getElementsByTagName("distance"); Node node1 =
     * null; if (nl1.getLength() > 0) node1 = nl1.item(nl1.getLength() - 1);
     * if (node1 != null) { NodeList nl2 = node1.getChildNodes(); Node node2
     * = nl2.item(getNodeIndex(nl2, "value")); Log.i("DistanceValue",
     * node2.getTextContent()); return
     * Integer.parseInt(node2.getTextContent()); } else return 0;
     */
    }

    public String getStartAddress(Document doc) {

        try {

            NodeList nl1 = doc.getElementsByTagName("start_address");
            Node node1 = nl1.item(0);
            Log.i("StartAddress", node1.getTextContent());
            return node1.getTextContent();

        } catch (Exception e) {
            return "-1";
        }
    }

    public String getEndAddress(Document doc) {

        try {

            NodeList nl1 = doc.getElementsByTagName("end_address");
            Node node1 = nl1.item(0);
            Log.i("StartAddress", node1.getTextContent());
            return node1.getTextContent();

        } catch (Exception e) {
            return "-1";
        }
    }

    public String getCopyRights(Document doc) {

        try {

            NodeList nl1 = doc.getElementsByTagName("copyrights");
            Node node1 = nl1.item(0);
            Log.i("CopyRights", node1.getTextContent());
            return node1.getTextContent();

        } catch (Exception e) {
            return "-1";
        }
    }

    public ArrayList<LatLng> getDirection(Document doc) {

        NodeList nl1, nl2, nl3;
        ArrayList<LatLng> listGeopoints = new ArrayList<LatLng>();
        nl1 = doc.getElementsByTagName("step");

        if (nl1.getLength() > 0) {

            for (int i = 0; i < nl1.getLength(); i++) {

                Node node1 = nl1.item(i);
                nl2 = node1.getChildNodes();

                Node locationNode = nl2.item(getNodeIndex(nl2, "start_location"));
                nl3 = locationNode.getChildNodes();
                Node latNode = nl3.item(getNodeIndex(nl3, "lat"));

                double lat = Double.parseDouble(latNode.getTextContent());
                Node lngNode = nl3.item(getNodeIndex(nl3, "lng"));

                double lng = Double.parseDouble(lngNode.getTextContent());
                listGeopoints.add(new LatLng(lat, lng));

                locationNode = nl2.item(getNodeIndex(nl2, "polyline"));
                nl3 = locationNode.getChildNodes();
                latNode = nl3.item(getNodeIndex(nl3, "points"));

                ArrayList<LatLng> arr = decodePoly(latNode.getTextContent());
                for (int j = 0; j < arr.size(); j++) {
                    listGeopoints.add(new LatLng(arr.get(j).latitude, arr.get(j).longitude));
                }

                locationNode = nl2.item(getNodeIndex(nl2, "end_location"));
                nl3 = locationNode.getChildNodes();
                latNode = nl3.item(getNodeIndex(nl3, "lat"));
                lat = Double.parseDouble(latNode.getTextContent());
                lngNode = nl3.item(getNodeIndex(nl3, "lng"));
                lng = Double.parseDouble(lngNode.getTextContent());
                listGeopoints.add(new LatLng(lat, lng));
            }
        }
        return listGeopoints;
    }

    //This method added by Beat
    public ArrayList<String> getHtmlInstruction(Document doc) {
        NodeList nl1, nl2;
        ArrayList<String> htmlInstruction = new ArrayList<String>();
        nl1 = doc.getElementsByTagName("step");

        if (nl1.getLength() > 0) {

            for (int i = 0; i < nl1.getLength(); i++) {

                Node node1 = nl1.item(i);
                nl2 = node1.getChildNodes();

                Node htmlInstructionNode = nl2.item(getNodeIndex(nl2, "html_instructions"));
                htmlInstruction.add(htmlInstructionNode.getTextContent() + "");
            }
        }
        return htmlInstruction;
    }

    //This method added by Beat
    public ArrayList<String> getManeuver(Document doc) {
        NodeList nl1, nl2;
        ArrayList<String> maneuver = new ArrayList<String>();
        nl1 = doc.getElementsByTagName("step");

        if (nl1.getLength() > 0) {

            for (int i = 0; i < nl1.getLength(); i++) {

                Node node1 = nl1.item(i);
                nl2 = node1.getChildNodes();

                Node maneuverNode = nl2.item(getNodeIndex(nl2, "maneuver"));
                maneuver.add(maneuverNode.getTextContent() + "");
            }
        }
        return maneuver;
    }

    //This method added by Beat
    public ArrayList<LatLng> getEndLocation(Document doc) {
        NodeList nl1, nl2, nl3;
        ArrayList<LatLng> listEndLocation = new ArrayList<LatLng>();
        nl1 = doc.getElementsByTagName("step");

        if (nl1.getLength() > 0) {

            for (int i = 0; i < nl1.getLength(); i++) {

                Node node1 = nl1.item(i);
                nl2 = node1.getChildNodes();

                Node locationNode = nl2.item(getNodeIndex(nl2, "end_location"));
                nl3 = locationNode.getChildNodes();
                Node latNode = nl3.item(getNodeIndex(nl3, "lat"));
                double lat = Double.parseDouble(latNode.getTextContent());
                Node lngNode = nl3.item(getNodeIndex(nl3, "lng"));
                double lng = Double.parseDouble(lngNode.getTextContent());
                listEndLocation.add(new LatLng(lat, lng));
            }
        }
        return listEndLocation;
    }

    //This method added by Beat
    public int getStepSize(Document doc) {
        NodeList nl1 = doc.getElementsByTagName("step");
        return nl1.getLength();
    }

    //This method added by Beat
    public ArrayList<GoogleDirectionStep> getDirectionStep(Document doc) {
        ArrayList<GoogleDirectionStep> googleDirectionStepArrayList = new ArrayList<GoogleDirectionStep>();
        GoogleDirectionStep googleDirectionStep;
        NodeList nl1, nl2, nl3;
        Node tmpNode, latNode, lngNode;
        nl1 = doc.getElementsByTagName("step");

        if(nl1.getLength() > 0) {
            for (int i = 0; i < nl1.getLength(); i++) {
                googleDirectionStep = new GoogleDirectionStep();
                Node node1 = nl1.item(i);
                nl2 = node1.getChildNodes();

                // Get travel mode
                tmpNode = nl2.item(getNodeIndex(nl2, "travel_mode"));
                googleDirectionStep.setTravelMode(tmpNode.getTextContent());

                // Get start location
                nl3 = nl2.item(getNodeIndex(nl2, "start_location")).getChildNodes();
                latNode = nl3.item(getNodeIndex(nl3, "lat"));
                lngNode = nl3.item(getNodeIndex(nl3, "lng"));
                double lat = Double.parseDouble(latNode.getTextContent());
                double lng = Double.parseDouble(lngNode.getTextContent());
                googleDirectionStep.setStartLocation(new LatLng(lat, lng));

                // Get end location
                nl3 = nl2.item(getNodeIndex(nl2, "end_location")).getChildNodes();
                latNode = nl3.item(getNodeIndex(nl3, "lat"));
                lngNode = nl3.item(getNodeIndex(nl3, "lng"));
                lat = Double.parseDouble(latNode.getTextContent());
                lng = Double.parseDouble(lngNode.getTextContent());
                googleDirectionStep.setEndLocation(new LatLng(lat, lng));

                // Get duration
                nl3 = nl2.item(getNodeIndex(nl2, "duration")).getChildNodes();
                tmpNode = nl3.item(getNodeIndex(nl3, "text"));
                googleDirectionStep.setDuration(tmpNode.getTextContent());

                // Get html instructions
                tmpNode = nl2.item(getNodeIndex(nl2, "html_instructions"));
                googleDirectionStep.setHtmlInstructions(tmpNode.getTextContent());

                // Get distance
                nl3 = nl2.item(getNodeIndex(nl2, "distance")).getChildNodes();
                tmpNode = nl3.item(getNodeIndex(nl3, "value"));
                googleDirectionStep.setDistance(tmpNode.getTextContent());

                // Get maneuver
                try {
                    tmpNode = nl2.item(getNodeIndex(nl2, "maneuver"));
                    googleDirectionStep.setManeuver(tmpNode.getTextContent());
                } catch (Exception ex) {
                    googleDirectionStep.setManeuver("");
                }

                // Get geopoint
                ArrayList<LatLng> listGeopoints = new ArrayList<LatLng>();

                Node locationNode = nl2.item(getNodeIndex(nl2, "start_location"));
                nl3 = locationNode.getChildNodes();
                latNode = nl3.item(getNodeIndex(nl3, "lat"));

                lat = Double.parseDouble(latNode.getTextContent());
                lngNode = nl3.item(getNodeIndex(nl3, "lng"));

                lng = Double.parseDouble(lngNode.getTextContent());
                listGeopoints.add(new LatLng(lat, lng));

                locationNode = nl2.item(getNodeIndex(nl2, "polyline"));
                nl3 = locationNode.getChildNodes();
                latNode = nl3.item(getNodeIndex(nl3, "points"));

                ArrayList<LatLng> arr = decodePoly(latNode.getTextContent());
                for (int j = 0; j < arr.size(); j++) {
                    listGeopoints.add(new LatLng(arr.get(j).latitude, arr.get(j).longitude));
                }

                locationNode = nl2.item(getNodeIndex(nl2, "end_location"));
                nl3 = locationNode.getChildNodes();
                latNode = nl3.item(getNodeIndex(nl3, "lat"));
                lat = Double.parseDouble(latNode.getTextContent());
                lngNode = nl3.item(getNodeIndex(nl3, "lng"));
                lng = Double.parseDouble(lngNode.getTextContent());
                listGeopoints.add(new LatLng(lat, lng));

                googleDirectionStep.setPolyline(listGeopoints);

                googleDirectionStepArrayList.add(googleDirectionStep);
            }
        }
        return googleDirectionStepArrayList;
    }

    private int getNodeIndex(NodeList nl, String nodename) {

        for (int i = 0; i < nl.getLength(); i++) {

            if (nl.item(i).getNodeName().equals(nodename))
                return i;
        }
        return -1;
    }

    private ArrayList<LatLng> decodePoly(String encoded) {

        ArrayList<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {

            int b, shift = 0, result = 0;

            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;

            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng position = new LatLng((double) lat / 1E5, (double) lng / 1E5);
            poly.add(position);
        }
        return poly;
    }

}
