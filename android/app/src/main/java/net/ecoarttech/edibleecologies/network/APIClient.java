package net.ecoarttech.edibleecologies.network;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import net.ecoarttech.edibleecologies.model.Interview;

import org.json.JSONObject;

/**
 * Created by pkoronkevich on 4/2/15.
 */
public class APIClient {

    private static final String LOCAL_SERVER = "http://localhost:8888/ee";
    private static final String GLOBAL_SERVER = ""; //TODO
    private static final String SERVER_URL = LOCAL_SERVER;
    private static final String QUESTIONS_PATH = "current_questions.php";
    private static final String UPLOAD_PATH = "waypoint_upload.php";
    private static final String WAYPOINTS_PATH = "waypoints_by_location.php";

    private static final String URL_POST_INTERVIEW = SERVER_URL + UPLOAD_PATH;
    private static final String URL_GET_QUESTIONS = SERVER_URL + QUESTIONS_PATH;
    private static final String URL_GET_WAYPOINTS = SERVER_URL + WAYPOINTS_PATH;

    public static void getQuestions(NetworkListener<JSONObject> listener){
        NetworkManager.sendRequest(new JsonObjectRequest(
                URL_GET_QUESTIONS,
                null,
                listener,
                listener));
    }

    public static void getWaypoints(double lat, double lng, NetworkListener<JSONObject> listener){
        String url = String.format(URL_GET_WAYPOINTS+"?lat=%f&lng=%f", lat, lng);
        NetworkManager.sendRequest(new JsonObjectRequest(
                url,
                null,
                listener,
                listener));
    }

    public static void postInterview(Interview interview, NetworkListener<JSONObject> listener) {
        NetworkManager.sendRequest(new JsonObjectRequest(
                Request.Method.POST,
                URL_POST_INTERVIEW,
                interview.toJson(),
                listener,
                listener));
    }
}
