package vlm.http;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import vlm.Transaction;
import vlm.TransactionProcessor;
import vlm.VolumeException;
import vlm.services.ParameterService;
import vlm.services.TransactionService;
import vlm.util.Convert;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Level;
import java.util.logging.Logger;

import static vlm.http.common.Parameters.TRANSACTION_BYTES_PARAMETER;
import static vlm.http.common.Parameters.TRANSACTION_JSON_PARAMETER;
import static vlm.http.common.ResultFields.*;

public final class BroadcastTransaction extends APIServlet.APIRequestHandler {

    private static final Logger logger = Logger.getLogger(BroadcastTransaction.class.getSimpleName());

    private final TransactionProcessor transactionProcessor;
    private final ParameterService parameterService;
    private final TransactionService transactionService;

    public BroadcastTransaction(TransactionProcessor transactionProcessor, ParameterService parameterService, TransactionService transactionService) {
        super(new APITag[]{APITag.TRANSACTIONS}, TRANSACTION_BYTES_PARAMETER, TRANSACTION_JSON_PARAMETER);

        this.transactionProcessor = transactionProcessor;
        this.parameterService = parameterService;
        this.transactionService = transactionService;
    }

    @Override
    JsonElement processRequest(HttpServletRequest req) throws VolumeException {

        String transactionBytes = Convert.emptyToNull(req.getParameter(TRANSACTION_BYTES_PARAMETER));
        String transactionJSON = Convert.emptyToNull(req.getParameter(TRANSACTION_JSON_PARAMETER));
        Transaction transaction = parameterService.parseTransaction(transactionBytes, transactionJSON);
        JsonObject response = new JsonObject();
        try {
            transactionService.validate(transaction);
            response.addProperty(NUMBER_PEERS_SENT_TO_RESPONSE, transactionProcessor.broadcast(transaction));
            response.addProperty(TRANSACTION_RESPONSE, transaction.getStringId());
            response.addProperty(FULL_HASH_RESPONSE, transaction.getFullHash());
        } catch (VolumeException.ValidationException | RuntimeException e) {
            logger.log(Level.INFO, e.getMessage(), e);
            response.addProperty(ERROR_CODE_RESPONSE, 4);
            response.addProperty(ERROR_DESCRIPTION_RESPONSE, "Incorrect transaction: " + e.toString());
            response.addProperty(ERROR_RESPONSE, e.getMessage());
        }
        return response;

    }

    @Override
    boolean requirePost() {
        return true;
    }

}
