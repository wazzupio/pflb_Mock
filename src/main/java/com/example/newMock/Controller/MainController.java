package com.example.newMock.Controller;

import com.example.newMock.Model.RequestDTO;
import com.example.newMock.Model.ResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

import static java.math.BigDecimal.ROUND_HALF_UP;

@RestController
public class MainController {
    private Logger log = LoggerFactory.getLogger(MainController.class);

    ObjectMapper mapper = new ObjectMapper();

    @PostMapping(
            value = "/info/postBalances",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )

    public Object postBalances(@RequestBody RequestDTO requestDTO){
        try {
            String clientId = requestDTO.getClientId();
            char firstDigit = clientId.charAt(0);
            BigDecimal maxLim;
            BigDecimal randBalance;

            ResponseDTO responseDTO = new ResponseDTO();

            if (firstDigit == '8') {
                maxLim = new BigDecimal(2000);
                responseDTO.setCurrency("US");
                randBalance = maxLim.multiply(BigDecimal.valueOf(Math.random()));
                responseDTO.setBalance(randBalance.setScale(0, ROUND_HALF_UP));
            } else if (firstDigit == '9') {
                maxLim = new BigDecimal(1000);
                responseDTO.setCurrency("EU");
                randBalance = maxLim.multiply(BigDecimal.valueOf(Math.random()));
                responseDTO.setBalance(randBalance.setScale(0, ROUND_HALF_UP));
            } else {
                maxLim = new BigDecimal(10000);
                responseDTO.setCurrency("RUB");
                randBalance = maxLim.multiply(BigDecimal.valueOf(Math.random()));
                responseDTO.setBalance(randBalance.setScale(0, ROUND_HALF_UP));
            }

            responseDTO.setRqUID(requestDTO.getRqUID());
            responseDTO.setClientId(clientId);
            responseDTO.setAccount(requestDTO.getAccount());
            responseDTO.setMaxLimit(maxLim);

            log.info("********** RequestDTO **********{}",
                    mapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestDTO));
            log.info("********** ResponseDTO **********{}",
                    mapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseDTO));

            return responseDTO;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
