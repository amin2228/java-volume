package vlm.http;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import vlm.services.TimeService;

import javax.servlet.http.HttpServletRequest;

import static vlm.http.common.ResultFields.TIME_RESPONSE;

public final class GetTime extends APIServlet.APIRequestHandler {

    private final TimeService timeService;

    GetTime(TimeService timeService) {
        super(new APITag[]{APITag.INFO});
        this.timeService = timeService;
    }

    @Override
    JsonElement processRequest(HttpServletRequest req) {
        JsonObject response = new JsonObject();
        response.addProperty(TIME_RESPONSE, timeService.getEpochTime());

        return response;
    }

}
